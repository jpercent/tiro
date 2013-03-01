package syndeticlogic.tiro.trial;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import syndeticlogic.tiro.Tiro;
import syndeticlogic.tiro.controller.ControllerMeta;

public class TrialResultsJdbcDaoTest {
    TrialResultsJdbcDao jdbcDao;
    
    @Before
    public void setup() throws Exception {
        File file = new File("tiro-test.db");
        file.delete();
        Properties props = Tiro.load("tiro-sqlite.properties");
        props.setProperty("jdbc-url", "jdbc:sqlite:tiro-test.db;foreign keys=true");
        jdbcDao = new TrialResultsJdbcDao(props);
        jdbcDao.createTables();
        jdbcDao.initialize();

    }
    
    @After
    public void teardown() {
    }
    
    @Test
    public void testInsertTrialMeta() {
        TrialMeta meta = new TrialMeta("test-trial-meta");
        assertEquals(-1, meta.getId());
        jdbcDao.insertTrialMeta(meta);
        assertEquals(1, meta.getId());
        List trialsMeta = jdbcDao.adHocQuery("select * from trials_meta", new TrialMeta.TrialMetaRowMapper());
        System.out.println("TrialMetas = "+trialsMeta);
        assertEquals(1, trialsMeta.size());
        assertTrue(meta != trialsMeta.get(0));
        assertTrue(meta.equals(trialsMeta.get(0)));
    }

    @Test
    public void testInsertControllerMeta() {
        ControllerMeta meta = new ControllerMeta("sequentialscan", "syncfilechannel", "java", "device");
        assertEquals(-1, meta.getId());
        jdbcDao.insertControllerMeta(meta);
        assertEquals(1, meta.getId());
        List controllersMeta = jdbcDao.adHocQuery("select * from controllers_meta", new ControllerMeta.ControllerMetaRowMapper());
        System.out.println("ControllersMeta = "+controllersMeta);
        assertEquals(1, controllersMeta.size());
        assertTrue(meta != controllersMeta.get(0));
        assertTrue(meta.equals(controllersMeta.get(0)));
        
    }

    @Test
    public void testInsertTrial() {
        fail("Not yet implemented");
    }

    @Test
    public void testCompleteTrial() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsertController() {
        fail("Not yet implemented");
    }

    @Test
    public void testCompleteController() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsertIORecord() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsertIOStats() {
        fail("Not yet implemented");
    }

    @Test
    public void testInsertMemoryStats() {
        fail("Not yet implemented");
    }

    @Test
    public void testAdHocQuery() {
        fail("Not yet implemented");
    }

}
