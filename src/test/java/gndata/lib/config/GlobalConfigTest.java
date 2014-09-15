package gndata.lib.config;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static gndata.lib.config.GlobalConfig.ProjectItem;

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
    public void testAppendProject() throws Exception {
        assert(conf.getProjects().isEmpty());
        conf.appendProject("myName", "myPath");
        assert(conf.getProjects().size() == 1);
        ProjectItem item = conf.getProjects().get(0);
        assert(item.name.equals("myName"));
        assert(item.path.equals("myPath"));
    }

    @Test
    public void testLoadStore() throws Exception {
        conf = GlobalConfig.load(tmpPath);
        assert(conf.getProjects().isEmpty());
        conf.getProjects().add(new ProjectItem("myName", "myPath"));
        conf.store();
        conf = GlobalConfig.load(tmpPath);
        ProjectItem item = conf.getProjects().get(0);
        assert(item.name.equals("myName"));
        assert(item.path.equals("myPath"));
    }

    @Test
    public void testMakeConfigPath() throws Exception {
        assert(GlobalConfig.makeConfigPath().toString().contains(System.getProperty("user.home")));
    }

}