package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.IntProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a random int value for the annotated filed.
 * The value will be uniformly distributed between the min value (included) and the max value (included)
 * from this random number generator's sequence.
 * <p>
 * The default min is <code>Integer.MIN_VALUE</code> and the max is <code>Integer.MAX_VALUE</code>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IntType {
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
    String seed() default "";
    Class<? extends CanProvide> provider() default IntProvider.class;
}
