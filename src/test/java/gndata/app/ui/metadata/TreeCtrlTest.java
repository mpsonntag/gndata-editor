package gndata.app.ui.metadata;

import java.nio.file.*;

import static org.junit.Assert.assertNull;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.state.ProjectState;
import gndata.app.ui.metadata.tree.TreeCtrl;
import gndata.lib.config.ProjectConfig;
import gndata.lib.util.FakeRDFModel;
import org.apache.commons.io.FileUtils;
import org.junit.*;


public class TreeCtrlTest {

    private static final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-project");

    ProjectState ps = new ProjectState();

    @Before
    public void setUp() throws Exception {
        ProjectConfig config = ProjectConfig.load(tmpPath.toString());
        config.setName("MyName");
        config.setDescription("MyDescription");

        ps.setConfig(config); // creates initial project structure

        ClassLoader cl = TreeCtrlTest.class.getClassLoader();

        Path foaf = Paths.get(cl.getResource("testfiles/foaf_example.rdf").getPath());
        Path meta = tmpPath.resolve("metadata/annotations/metadata.rdf");
        Files.copy(foaf, meta, StandardCopyOption.REPLACE_EXISTING);

        ps.setConfig(ProjectConfig.load(tmpPath.toString())); // to reload project state and metadata service
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @Test
    public void testListen() throws Exception {
        TreeCtrl ctrl = new TreeCtrl(ps);
        assertNull(ctrl.getTree());

        /* TODO find the way to inject FXML
        ctrl.initialize();
        TreeItem<Resource> root = ctrl.getTree().getRoot();
        assertNotNull(root);

        ProjectConfig config = ProjectConfig.load(tmpPath.toString());
        ps.setConfig(config);

        assertNotEquals(ctrl.getTree().getRoot(), root);
        */
    }

    @Test
    public void testRootClasses() throws Exception {
        TreeCtrl ctrl = new TreeCtrl(ps);

        Model annotations = ps.getMetadata().getAnnotations();

        Resource person = annotations.getResource("http://xmlns.com/foaf/0.1/Person");
        assert(ctrl.getRootClasses().stream().anyMatch(a -> a.getValue().equals(person)));

        Resource tbl_node = annotations.getResource(FakeRDFModel.tbl);
        assert(ctrl.getRootClasses("tim").stream().anyMatch(a -> a.getValue().equals(tbl_node)));
    }
}