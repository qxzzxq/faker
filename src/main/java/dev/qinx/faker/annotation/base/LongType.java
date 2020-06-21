package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.LongProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a random long value for the annotated filed.
 * The value will be uniformly distributed between the min value (included) and the max value (included)
 * from this random number generator's sequence.
 * <p>
 * The default min is <code>Long.MIN_VALUE</code> and the max is <code>Long.MAX_VALUE</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LongType {
    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;

    String seed() default "";

    Class<? extends CanProvide> provider() default LongProvider.class;
}
