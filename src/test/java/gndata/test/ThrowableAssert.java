package gndata.test;

import gndata.test.ThrowableAssert.ThrowingCallable;
import org.assertj.core.api.*;

/**
 * AssertJ like custom assertion for exception checks.
 */
public class ThrowableAssert extends AbstractAssert<ThrowableAssert, ThrowingCallable> {

    protected ThrowableAssert(ThrowingCallable actual) {
        super(actual, ThrowableAssert.class);
    }


    public static ThrowableAssert assertThat(ThrowingCallable actual) {
        return new ThrowableAssert(actual);
    }

    public void wasThrowing(Class<?> exception) {
        isNotNull();

        StringBuilder msg = new StringBuilder("Was expected to throw ")
                .append(exception.getSimpleName())
                .append(", ");

        try {
            actual.call();
            msg.append("but nothing was thrown!");
            failWithMessage(msg.toString());
        } catch (Throwable e) {
            msg.append("but instead ")
                    .append(e.getClass().getSimpleName())
                    .append(" was thrown!");

            Assertions.assertThat(e)
                    .isInstanceOf(exception)
                    .overridingErrorMessage(msg.toString());
        }
    }

    public void wasThrowingAny(Class<?> ...exceptions) {
        isNotNull();

        StringBuilder msg = new StringBuilder("Was expected to throw ");

        for (Class<?> t : exceptions) {
            msg.append(t.getSimpleName()).append(", ");
        }

        try {
            actual.call();
            msg.append("but nothing was thrown!");
            failWithMessage(msg.toString());
        } catch (Throwable e) {
            msg.append("but instead ")
                    .append(e.getClass().getSimpleName())
                    .append(" was thrown!");

            Assertions.assertThat(e)
                    .isInstanceOfAny(exceptions)
                    .overridingErrorMessage(msg.toString());
        }
    }


    public interface ThrowingCallable {

        void call() throws Throwable;

    }
}
