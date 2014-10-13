package gndata.lib.config;

import org.junit.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class GlobalConfigTest {

    GlobalConfig conf;
    Path tmpPath;

    @Before
    public void setUp() throws Exception {
        tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "config.json");
        conf = new GlobalConfig();
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(tmpPath);
    }

    @Test
    public void testSetProject() throws Exception {
        assertTrue(conf.getProjects().isEmpty());

        conf.setProject("MyPath", "MyName");
        assertEquals(1, conf.getProjects().size());
        assertTrue(conf.hasProject("MyPath"));
        assertEquals("MyName", conf.getProjectName("MyPath"));
    }

    @Test
    public void testLoadStore() throws Exception {
        conf = GlobalConfig.load(tmpPath.toString());
        assertTrue(conf.getProjects().isEmpty());
        conf.setProject("MyPath", "MyName");
        conf.store();
        conf = GlobalConfig.load(tmpPath.toString());
        assertEquals(1, conf.getProjects().size());
        assertTrue(conf.hasProject("MyPath"));
        assertEquals("MyName", conf.getProjectName("MyPath"));
    }

    @Test
    public void testMakeConfigPath() throws Exception {
        assertTrue(GlobalConfig.makeConfigPath().contains(System.getProperty("user.home")));
    }

}