package gndata.app.ui.dia;

import static org.assertj.core.api.Assertions.assertThat;

import gndata.lib.config.ProjectConfig;
import javafx.beans.property.SimpleStringProperty;
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

        ctrl.initialize(null, null);
    }

    // TODO pass an onAction event to the cancel and ok methods
    // TODO required after refactoring of the DialogViewClass
/*
    @Test
    public void testCancel() throws Exception {
        ctrl.setCancelled(false);
        ctrl.cancel();
        assertThat(ctrl.isCancelled()).isTrue();
    }

    @Test
    public void testOk() throws Exception {
        ctrl.setCancelled(true);
        ctrl.ok();
        assertThat(ctrl.isCancelled()).isFalse();
    }
*/

    @Test
    public void testNameBinding() {
        ctrl.nameProperty().set("Changed");
        assertThat(ctrl.get().getName()).isEqualTo("Changed");
    }

    @Test
    public void testDescriptionBinding() {
        ctrl.descriptionProperty().set("Changed");
        assertThat(ctrl.get().getDescription()).isEqualTo("Changed");
    }

    @Test
    public void testGetResult() throws Exception {
        ProjectConfig result = ctrl.get();

        assertThat(result.getName()).isEqualTo("MyName");
        assertThat(result.getDescription()).isEqualTo("MyDescription");

        assertThat(result).isNotEqualTo(config);
    }
}