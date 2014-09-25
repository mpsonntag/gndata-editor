package gndata.app.ui.dia;

import gndata.lib.config.ProjectConfig;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        ProjectConfig result = ctrl.getResult();
        assertEquals("MyName", result.getName());
        assertEquals("MyDescription", result.getDescription());

        ctrl.name.set("NewName");
        ctrl.description.set("NewDescription");
        ctrl.setCancelled(true);

        // should not change because it was cancelled
        result = ctrl.getResult();
        assertEquals("MyName", result.getName());
        assertEquals("MyDescription", result.getDescription());

        // now it should change
        ctrl.setCancelled(false);
        result = ctrl.getResult();
        assertEquals("NewName", result.getName());
        assertEquals("NewDescription", result.getDescription());
    }
}