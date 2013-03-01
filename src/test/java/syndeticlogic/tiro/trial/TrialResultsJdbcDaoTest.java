package syndeticlogic.tiro.trial;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import syndeticlogic.tiro.Tiro;

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
        List trialMetas = jdbcDao.adHocQuery("select * from trials_meta", new TrialMeta.TrialMetaRowMapper());
        System.out.println("TrialMetas = "+trialMetas);
        assertEquals(1, trialMetas.size());
        assertTrue(meta != trialMetas.get(0));
        assertTrue(meta.equals(trialMetas.get(0)));
    }

    @Test
    public void testInsertControllerMeta() {
        fail("Not yet implemented");
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
