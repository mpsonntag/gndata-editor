package gndata.app.ui.metadata;

import java.nio.file.*;

import static org.junit.Assert.assertNull;

import com.hp.hpl.jena.rdf.model.*;
import gndata.app.state.*;
import gndata.app.ui.metadata.tree.RDFTreeCtrl;
import gndata.lib.config.ProjectConfig;
import gndata.lib.util.FakeRDFModel;
import org.apache.commons.io.FileUtils;
import org.junit.*;


public class RDFTreeCtrlTest {

    private static final Path tmpPath = Paths.get(System.getProperty("java.io.tmpdir"), "test-project");

    ProjectState projectState;
    MetadataState metadataState;
    RDFTreeCtrl treeCtrl;

    @Before
    public void setUp() throws Exception {
        metadataState = new MetadataState();
        projectState = new ProjectState();

        ProjectConfig config = ProjectConfig.load(tmpPath.toString());
        config.setName("MyName");
        config.setDescription("MyDescription");

        projectState.setConfig(config); // creates initial project structure

        ClassLoader cl = RDFTreeCtrlTest.class.getClassLoader();

        Path foaf = Paths.get(cl.getResource("testfiles/foaf_example.rdf").getPath());
        Path meta = tmpPath.resolve("metadata/annotations/metadata.rdf");
        Files.copy(foaf, meta, StandardCopyOption.REPLACE_EXISTING);

        projectState.setConfig(ProjectConfig.load(tmpPath.toString())); // to reload project state and metadata service

        treeCtrl = new RDFTreeCtrl(projectState, metadataState);
    }

    @After
    public void tearDown() throws Exception {
        if (Files.exists(tmpPath)) {
            FileUtils.deleteDirectory(tmpPath.toFile());
        }
    }

    @Test
    public void testListen() throws Exception {
        assertNull(treeCtrl.getTree());

        /* TODO find the way to inject FXML
        treeCtrl.initialize();
        TreeItem<Resource> root = treeCtrl.getTree().getRoot();
        assertNotNull(root);

        ProjectConfig config = ProjectConfig.load(tmpPath.toString());
        projectState.setConfig(config);

        assertNotEquals(treeCtrl.getTree().getRoot(), root);
        */
    }

    @Test
    public void testRootClasses() throws Exception {
        Model annotations = projectState.getMetadata().getAnnotations();

        Resource person = annotations.getResource("http://xmlns.com/foaf/0.1/Person");
        assert (treeCtrl.getRootClasses().stream().anyMatch(a -> a.getValue().equals(person)));

        Resource tbl_node = annotations.getResource(FakeRDFModel.tbl);
        assert (treeCtrl.getRootClasses("tim").stream().anyMatch(a -> a.getValue().equals(tbl_node)));
    }
}