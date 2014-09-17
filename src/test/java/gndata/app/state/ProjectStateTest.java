package gndata.app.state;

import gndata.lib.config.ProjectConfig;
import gndata.lib.srv.ProjectService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProjectStateTest {

    private ProjectState state;
    private ProjectConfig config, otherConfig;

    @Before
    public void setUp() throws Exception {
        state = new ProjectState();
        config = new ProjectConfig();
        otherConfig = new ProjectConfig();
    }

    @Test
    public void testSetConfig() throws Exception {
        assertFalse(state.isConfigured());
        assertNull(state.getConfig());
        assertNull(state.getService());

        state.setConfig(config);
        assertTrue(state.isConfigured());
        assertEquals(state.getConfig(), config);
        ProjectService srv01 = state.getService();
        assertNotNull(srv01);

        state.setConfig(otherConfig);
        state.setConfig(config);
        assertTrue(state.isConfigured());
        assertEquals(state.getConfig(), config);
        ProjectService srv02 = state.getService();
        assertNotNull(srv02);
        assertNotEquals(srv01, srv02);

        state.setConfig(null);
        assertFalse(state.isConfigured());
        assertNull(state.getConfig());
        assertNull(state.getService());
    }
}