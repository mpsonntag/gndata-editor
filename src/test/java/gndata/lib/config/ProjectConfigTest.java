package gndata.lib.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ProjectConfigTest {

    ProjectConfig conf;
    Path tmpPath;

    @Before
    public void setUp() throws Exception {
        tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "config.json");
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(tmpPath);
    }

    @Test
    public void testLoadStore() throws Exception {
        conf = ProjectConfig.load(tmpPath.toString());
        assert(conf.getName() == null);
        assert(conf.getDescription() == null);
        conf.setName("myName");
        conf.setDescription("myDescription");
        conf.store(tmpPath.toString());
        conf = ProjectConfig.load(tmpPath.toString());
        assertEquals(conf.getName(), "myName");
        assertEquals(conf.getDescription(), "myDescription");
    }

    @Test
    public void testMakeConfigPath() throws Exception {
        assert(ProjectConfig.makeConfigPath("somePath").endsWith("settings.json"));
    }

}