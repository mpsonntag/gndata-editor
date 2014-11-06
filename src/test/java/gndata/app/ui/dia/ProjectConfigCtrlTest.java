package gndata.app.ui.dia;

import static org.junit.Assert.*;

import gndata.lib.config.ProjectConfig;
import org.junit.*;

public class ProjectConfigCtrlTest {

    ProjectConfig config;
    ProjectConfigCtrl ctrl;

    @Before
    public void setUp() throws Exception {
        config = new ProjectConfig();
        config.setName("MyName");
        config.setDescription("MyDescription");
        ctrl = new ProjectConfigCtrl(config);
    }

    @Test
    public void testCancel() throws Exception {
        ctrl.setCancelled(false);
        ctrl.cancel();
        assertTrue(ctrl.isCancelled());
    }

    @Test
    public void testOk() throws Exception {
        ctrl.setCancelled(true);
        ctrl.ok();
        assertFalse(ctrl.isCancelled());
    }

    @Test
    public void testGetResult() throws Exception {
        ProjectConfig result = ctrl.getValue();
        assertEquals("MyName", result.getName());
        assertEquals("MyDescription", result.getDescription());
        assertNotEquals(result, config);
    }
}