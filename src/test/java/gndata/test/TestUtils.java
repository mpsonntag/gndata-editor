package gndata.test;

import static org.junit.Assert.*;

/**
 * Utilities for testing.
 */
public class TestUtils {

    /**
     * Asserts that a certain exception is thrown.
     *
     * @param callable  Callable that should throw the exception.
     * @param cls       The exception class.
     * @param <T>       Exception class type.
     */
    public static <T extends Exception> void assertThrows(CallableThrowing callable, Class<T> cls) {
        try {
            callable.call();
            String msg = "Exception of type %s was expected but not thrown";
            fail(String.format(msg, cls.getSimpleName()));
        } catch (Exception e) {
            String msg = "Exception of type %s was expected but instead was %s";
            assertTrue(
                    String.format(msg, cls.getSimpleName(), e.getClass().getSimpleName()),
                    cls.isInstance(e));
        }
    }


    public static interface CallableThrowing {

        public void call() throws Exception;

    }
}
