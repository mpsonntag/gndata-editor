package gndata.app.ui.util;

import java.util.*;

import static org.junit.Assert.*;
import static gndata.app.ui.util.NameConventions.*;
import static gndata.test.TestUtils.*;

import org.junit.*;

public class NameConventionsTest {

    private List<Class> classes;

    @Before
    public void setUp() throws Exception {
        classes = new LinkedList<>();
        Collections.addAll(classes, TestFoo.class, TestFoo.TestInner.class, TestFooView.class,
                TestFooView.TestInner.class, TestFooCtrl.class, TestFooCtrl.TestInner.class);
    }

    @Test
    public void testTemplatePath() throws Exception {
        for (Class c : classes) {
            assertEquals("gndata/app/ui/util/TestFooView.vtl", templatePath(c));
        }
    }

    @Test
    public void testTemplateResource() throws Exception {
        for (Class c : classes) {
            assertTrue(templateResource(c).toString().endsWith("gndata/app/ui/util/TestFooView.vtl"));
        }

        assertThrows(() -> templateResource(getClass()), RuntimeException.class);
    }

    @Test
    public void testFxmlPath() throws Exception {
        for (Class c : classes) {
            assertEquals("gndata/app/ui/util/TestFooView.fxml", fxmlPath(c));
        }
    }

    @Test
    public void testFxmlResource() throws Exception {
        for (Class c : classes) {
            assertTrue(fxmlResource(c).toString().endsWith("gndata/app/ui/util/TestFooView.fxml"));
        }

        assertThrows(() -> fxmlResource(getClass()), RuntimeException.class);
    }

    @Test
    public void testStylePath() throws Exception {
        for (Class c : classes) {
            assertEquals("gndata/app/ui/util/TestFooView.css", stylePath(c));
        }
    }

    @Test
    public void testStyleResource() throws Exception {
        for (Class c : classes) {
            assertTrue(styleResource(c).toString().endsWith("gndata/app/ui/util/TestFooView.css"));
            assertTrue(optionalStyleResource(c).isPresent());
            assertTrue(optionalStyleResource(c).get().toString().endsWith("gndata/app/ui/util/TestFooView.css"));
        }

        assertThrows(() -> styleResource(getClass()), RuntimeException.class);
        assertFalse(optionalStyleResource(getClass()).isPresent());
    }

    @Test
    public void testViewClassPath() throws Exception {
        for (Class c : classes) {
            assertEquals("gndata/app/ui/util/TestFooView", viewClassPath(c));
        }
    }

    @Test
    public void testViewClass() throws Exception {
        for (Class c : classes) {
            Class cv = viewClass(c);
            assertEquals(TestFooView.class, cv);
        }

        assertThrows(() -> viewClass(getClass()), ClassNotFoundException.class);
    }

    @Test
    public void testCtrlClassPath() throws Exception {
        for (Class c : classes) {
            assertEquals("gndata/app/ui/util/TestFooCtrl", ctrlClassPath(c));
        }
    }

    @Test
    public void testCtrlClass() throws Exception {
        for (Class c : classes) {
            Class cc = ctrlClass(c);
            assertEquals(TestFooCtrl.class, cc);
        }

        assertThrows(() -> ctrlClass(getClass()), ClassNotFoundException.class);
    }
}

class TestFooView {
    public static class TestInner {}
}

class TestFooCtrl {
    public static class TestInner {}
}

class TestFoo {
    public static class TestInner {}
}