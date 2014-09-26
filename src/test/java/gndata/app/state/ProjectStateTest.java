package gndata.app.state;

import gndata.lib.config.ProjectConfig;
import gndata.lib.srv.MetadataService;
import gndata.lib.srv.ProjectService;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ProjectStateTest {

    private ProjectState state;
    private ProjectConfig config, otherConfig;

    private static final Path fooPath = Paths.get(System.getProperty("java.io.tmpdir"), "foo");
    private static final Path barPath = Paths.get(System.getProperty("java.io.tmpdir"), "bar");

    @Before
    public void setUp() throws Exception {
        state = new ProjectState();
    }

    @After
    public void tearDown() throws Exception {
        for (Path p : Arrays.asList(fooPath, barPath)) {
            if (Files.exists(p)) {
                FileUtils.deleteDirectory(p.toFile());
            }
        }
    }

    @Test
    public void testSetEmptyConfig() throws Exception {
        config = new ProjectConfig();

        assertFalse(state.isConfigured());
        assertNull(state.getConfig());
        assertNull(state.getService());

        try {
            state.setConfig(config);
            throw new Exception("Empty config should raise exception");
        } catch (IOException e) {
            // pass test
        }
    }

    @Test
    public void testSetConfig() throws Exception {
        assertFalse(state.isConfigured());
        assertNull(state.getConfig());
        assertNull(state.getService());
        assertNull(state.getMetadata());

        config = ProjectConfig.load(fooPath.toString());

        state.setConfig(config);
        assertTrue(state.isConfigured());
        assertEquals(state.getConfig(), config);
        ProjectService srv01 = state.getService();
        assertNotNull(srv01);
        MetadataService srv02 = state.getMetadata();
        assertNotNull(srv02);

        otherConfig = ProjectConfig.load(barPath.toString());

        state.setConfig(otherConfig);
        state.setConfig(config);
        assertTrue(state.isConfigured());
        assertEquals(state.getConfig(), config);
        assertNotNull(state.getService());
        assertNotEquals(srv01, state.getService());
        assertNotNull(state.getMetadata());
        assertNotEquals(srv02, state.getMetadata());

        state.setConfig(null);
        assertFalse(state.isConfigured());
        assertNull(state.getConfig());
        assertNull(state.getService());
        assertNull(state.getMetadata());
    }

    // TODO write tests for listeners
}