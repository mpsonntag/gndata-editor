package gndata.app.ui.util;

import java.util.*;

import static gndata.app.ui.util.NameConventions.*;
import static org.assertj.core.api.Assertions.assertThat;

import gndata.test.ThrowableAssert;
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
            assertThat(templatePath(c)).isEqualTo("gndata/app/ui/util/TestFooView.vtl");
        }
    }

    @Test
    public void testTemplateResource() throws Exception {
        for (Class c : classes) {
            assertThat(templateResource(c).toString())
                    .endsWith("gndata/app/ui/util/TestFooView.vtl");
        }

        ThrowableAssert.assertThat(() -> templateResource(getClass()))
                .wasThrowing(RuntimeException.class);
    }

    @Test
    public void testFxmlPath() throws Exception {
        for (Class c : classes) {
            assertThat(fxmlPath(c)).isEqualTo("gndata/app/ui/util/TestFooView.fxml");
        }
    }

    @Test
    public void testFxmlResource() throws Exception {
        for (Class c : classes) {
            assertThat(fxmlResource(c).toString())
                    .endsWith("gndata/app/ui/util/TestFooView.fxml");
        }

        ThrowableAssert.assertThat(() -> fxmlResource(getClass()))
                .wasThrowing(RuntimeException.class);
    }

    @Test
    public void testStylePath() throws Exception {
        for (Class c : classes) {
            assertThat(stylePath(c)).isEqualTo("gndata/app/ui/util/TestFooView.css");
        }
    }

    @Test
    public void testStyleResource() throws Exception {
        for (Class c : classes) {
            assertThat(styleResource(c).toString()).endsWith("gndata/app/ui/util/TestFooView.css");

            assertThat(optionalStyleResource(c).isPresent()).isTrue();
            assertThat(optionalStyleResource(c).get().toString()).endsWith("gndata/app/ui/util/TestFooView.css");
        }

        ThrowableAssert.assertThat(() -> styleResource(getClass())).wasThrowing(RuntimeException.class);
    }

    @Test
    public void testViewClassPath() throws Exception {
        for (Class c : classes) {
            assertThat(viewClassPath(c)).isEqualTo("gndata/app/ui/util/TestFooView");
        }
    }

    @Test
    public void testViewClass() throws Exception {
        for (Class c : classes) {
            assertThat(viewClass(c)).isEqualTo(TestFooView.class);
        }

        ThrowableAssert.assertThat(() -> viewClass(getClass())).wasThrowing(ClassNotFoundException.class);
    }

    @Test
    public void testCtrlClassPath() throws Exception {
        for (Class c : classes) {
            assertThat(ctrlClassPath(c)).isEqualTo("gndata/app/ui/util/TestFooCtrl");
        }
    }

    @Test
    public void testCtrlClass() throws Exception {
        for (Class c : classes) {
            assertThat(ctrlClass(c)).isEqualTo(TestFooCtrl.class);
        }

        ThrowableAssert.assertThat(() -> ctrlClass(getClass())).wasThrowing(ClassNotFoundException.class);
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