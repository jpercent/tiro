package syndeticlogic.tiro.jdbc;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import syndeticlogic.tiro.Tiro;
import syndeticlogic.tiro.jdbc.BaseJdbcDao;
import syndeticlogic.tiro.model.ControllerMeta;
import syndeticlogic.tiro.model.TrialMeta;

public class TrialResultsJdbcDaoTest {
    BaseJdbcDao baseJdbcDao;
    
    @Before
    public void setup() throws Exception {
        File file = new File("tiro-test.db");
        file.delete();
        Properties props = Tiro.loadProperties();
        props.setProperty("jdbc-url", "jdbc:sqlite:tiro-test.db;foreign keys=true");
        baseJdbcDao = new BaseJdbcDao(props);
        baseJdbcDao.createTables();
        baseJdbcDao.initialize();

    }
    
    @After
    public void teardown() {
    }
    
    @Test
    public void testInsertTrialMeta() {
        TrialMeta meta = new TrialMeta("test-trial-meta");
        assertEquals(-1, meta.getId());
        baseJdbcDao.insertTrialMeta(meta);
        assertEquals(1, meta.getId());
        List<?> trialsMeta = baseJdbcDao.adHocQuery("select * from trials_meta", new TrialMeta.TrialMetaRowMapper());
        System.out.println("TrialMetas = "+trialsMeta);
        assertEquals(1, trialsMeta.size());
        assertTrue(meta != trialsMeta.get(0));
        assertTrue(meta.equals(trialsMeta.get(0)));
    }
    
    @Test
    public void testInsertControllerMeta() {
        ControllerMeta meta = new ControllerMeta("sequentialscan", "syncfilechannel", "java", "device");
        assertEquals(-1, meta.getId());
        baseJdbcDao.insertControllerMeta(meta);
        assertEquals(1, meta.getId());
        List<?> controllersMeta = baseJdbcDao.adHocQuery("select * from controllers_meta", new ControllerMeta.ControllerMetaRowMapper());
        System.out.println("ControllersMeta = "+controllersMeta);
        assertEquals(1, controllersMeta.size());
        assertTrue(meta != controllersMeta.get(0));
        assertTrue(meta.equals(controllersMeta.get(0)));
    }
    
    @Test
    public void testTrial() {
    }

    @Test
    public void testController() {
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
}
