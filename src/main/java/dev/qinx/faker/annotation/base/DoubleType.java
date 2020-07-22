package dev.qinx.faker.annotation.base;

import dev.qinx.faker.provider.Provider;
import dev.qinx.faker.provider.base.DoubleProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a random double for the annotated filed.
 * The value will be uniformly distributed between min and max value
 * from this random number generator's sequence.
 *
 * <p>The default range is <code>[0D, 1D]</code></p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DoubleType {
    double min() default 0D;

    double max() default 1D;

    String seed() default "";

    Class<? extends Provider> provider() default DoubleProvider.class;
}
