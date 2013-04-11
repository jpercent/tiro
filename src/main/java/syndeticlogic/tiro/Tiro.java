package syndeticlogic.tiro;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

import syndeticlogic.tiro.controller.IOControllerFactory;
import syndeticlogic.tiro.jdbc.BaseJdbcDao;
import syndeticlogic.tiro.model.Controller;
import syndeticlogic.tiro.model.ControllerMeta;
import syndeticlogic.tiro.model.Trial;
import syndeticlogic.tiro.model.TrialMeta;
import syndeticlogic.tiro.trial.TrialRunner;
import syndeticlogic.tiro.trial.TrialRunnerFactory;

public class Tiro {
	public enum Platform { Linux, OSX, Windows }
	private static Platform platform;
    private final Options options = new Options();
    private final CommandLineParser parser;
    private final String usage;
    private final String header;
    private String[] args;
    private static final Log log = LogFactory.getLog(Tiro.class);
    private Map<String, Object> config;
    private final BaseJdbcDao baseJdbcDao;
    private int retries;
    private boolean concurrent;
    private boolean reinit;

    public Tiro(String[] args) throws Exception {
        if (SystemUtils.IS_OS_MAC_OSX)
            Tiro.setPlatform(Platform.OSX);
        else if (SystemUtils.IS_OS_LINUX)
            Tiro.setPlatform(Platform.Linux);
        else if (SystemUtils.IS_OS_WINDOWS)
            Tiro.setPlatform(Platform.Windows);
        else
            throw new RuntimeException("unsupported platform");

        this.args = args;
        usage = "catena-analyzer.sh -config <config-file-path> [options]...";
        header = "Runs various performance trials based on settings in the properties file."
                + System.getProperty("line.separator")
                + System.getProperty("line.separator") + "Options:";

        Option help = new Option("help", "Print this message and exit.");

        Option concurrent = new Option(
                "concurrent",
                "Turns on concurent trial execution.  Generally, trials will be run sequentially, but when this flag is supplied all trials will run concurrently");

        Option init = new Option("reinit",
                "Recreates the enviroment necessary to use the tool (the enviroment is automatically created).  Any previous data will be destroyed.");

        @SuppressWarnings("static-access")
        Option properties = OptionBuilder
                .withArgName("file-path")
                .hasArg()
                .withDescription("Path to JSON file that describes the trials to run.  See the example config in the documentation for configuration options.")
                .create("config");

        @SuppressWarnings("static-access")
        Option retries = OptionBuilder
                .withArgName("n")
                .hasArg()
                .withDescription("Retries.  Repeats the trials described in the properties file n times where is bounded between 1 and 50 inclusive.")
                .create("retries");

        options.addOption(help);
        options.addOption(concurrent);
        options.addOption(init);
        options.addOption(properties);
        options.addOption(retries);
        parser = new GnuParser();
        baseJdbcDao = BaseJdbcDao.createJdbcDao(Tiro.loadProperties());
    }

    @SuppressWarnings("unchecked")
    private boolean parse() throws Exception {
        boolean ret = false;
        if (args == null  || args.length <= 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printUsage(new PrintWriter(System.out), 80, usage);
            formatter = new HelpFormatter();
            formatter.printHelp(80, usage, header, options, "");
            return ret;
        }

		CommandLine line = null;
		try {
			line = parser.parse(options, args);
			assert line != null;
			if (line.hasOption("help")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp(80, usage, header, options, "");
				return false;
			}

			if (line.hasOption("reinit")) {
				this.reinit = true;
			}

			if (line.hasOption("concurrent")) {
				this.concurrent = true;
			}

			if (line.hasOption("config")) {
				String jsonConfigFile = line.getOptionValue("config");
				ObjectMapper mapper = new ObjectMapper(new JsonFactory() {
					@Override
					public JsonParser createJsonParser(File file) throws IOException, JsonParseException {
						JsonParser p = super.createJsonParser(file);
						p.enable(JsonParser.Feature.ALLOW_COMMENTS);
						p.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
						p.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
						p.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
						return p;
					}
				});
				
				try {
					config = mapper.readValue(new File(jsonConfigFile), Map.class);
				} catch (IOException e) {
					log.error("Error parsing configuration file.  The configuration file is JSON; ensure that the syntax is\n\t valid: http://json.org/.  Details of the exception follow.\n", e);
					return false;
				}
				if (line.hasOption("retries")) {
					String retries = line.getOptionValue("retries");
					if (retries != null && !retries.equals("")) {
						this.retries = new Integer(retries.trim()).intValue();
						if (this.retries <= 0 || this.retries > 50) {
							throw new org.apache.commons.cli.ParseException(
									"retries is bounded between 1 and 50 inclusive");
						}
					}
				}
				ret = true;
			} else {
				log.fatal("No config file set");
			}
		} catch (org.apache.commons.cli.ParseException e) {
			log.error("Exception parsing arguments", e);
		}
		return ret;
    }

    public List<TrialRunner> buildTrials() {
    	
        LinkedList<TrialRunner> trialRunners = new LinkedList<TrialRunner>();
        baseJdbcDao.initialize();
        System.out.println(config);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> trialMetaJsons = (List<Map<String, Object>>) config.get("trials");
        for (Map<String, Object> trialMetaJson : trialMetaJsons) {
            @SuppressWarnings("unchecked")
            List<Map<String, String>> controllerMetaJsons = (List<Map<String, String>>) trialMetaJson.get("controllers");
            TrialMeta trialMeta = new TrialMeta(getPlatform().name(), (String) trialMetaJson.get("name"));
            baseJdbcDao.insertTrialMeta(trialMeta);
            Trial trial = new Trial(trialMeta);
            //baseJdbcDao.insertTrial(trial);

            Controller[] controllers = new Controller[controllerMetaJsons.size()];
            int i = 0;
            for (Map<String, String> controller : controllerMetaJsons) {
                ControllerMeta cmeta = new ControllerMeta(controller.get("controller"), controller.get("executor"), controller.get("memory"), controller.get("device"));
                baseJdbcDao.insertControllerMeta(cmeta);
                controllers[i++] = new Controller(trialMeta, cmeta);
            }
            //baseJdbcDao.insertControllers(controllers);
            TrialRunnerFactory trialRunnerFactory = new TrialRunnerFactory(new IOControllerFactory(), baseJdbcDao);
            trialRunners.add(trialRunnerFactory.createTrialRunner(trial, controllers));
            System.out.println("Trial = " + trialMetaJson);
        }
        return trialRunners;
    }
    
    public void cleanUpSystem() {
    }

    public void run() throws Exception {
        if (!parse()) {
            log.fatal("Failed to parse the command line - exiting.");
            return;
        }
        if(reinit)
        	;
       
        int count = 0;
        do {
            List<TrialRunner> runners = buildTrials();
            for (TrialRunner runner : runners) {
                runner.startTrial();
                if (!concurrent) {
                    runner.waitForTrialCompletion();
                }
                cleanUpSystem();
            }

            if (concurrent) {
                for (TrialRunner runner : runners) {
                    runner.waitForTrialCompletion();
                }
            }
        } while (++count < retries);
    }
    
    public static Platform getPlatform() {
    	return platform;
    }
    
    public static void setPlatform(Platform platform) {
    	Tiro.platform = platform;
    }
    
    public static TrialRunner createSequentialScanTrial() throws Exception {
        // Properties p = .load("catena-perf-sql.properties");
        // BaseJdbcDao results = new BaseJdbcDao(p);
        // results.insertTrialMeta();
        // results.insertTrial();
        // results.insertController();
        return null;
    }

    public static Properties loadProperties() throws Exception {
    	final String propertiesFileName;
        if (SystemUtils.IS_OS_MAC_OSX)
            propertiesFileName = "tiro-sqlite-osx.properties";
        else if (SystemUtils.IS_OS_LINUX)
            propertiesFileName = "tiro-sqlite-linux.properties";
        else if (SystemUtils.IS_OS_WINDOWS)
        	throw new RuntimeException("Windows is not supported at this time");
        else
        	throw new RuntimeException("unsupported platform");
        System.out.println(propertiesFileName);
        URL url = ClassLoader.getSystemResource(propertiesFileName);
        Properties props = new Properties();
        props.load(url.openStream());
        return props;
    }
    
    public static void main(String[] args) throws Exception {
        Tiro apprentice = new Tiro(args);
        apprentice.run();
    }
}
