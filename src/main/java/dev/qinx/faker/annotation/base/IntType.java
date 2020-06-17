package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.IntProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a random int value for the annotated filed.
 * The value will be uniformly distributed between Integer.MIN_VALUE and Integer.MAX_VALUE
 * from this random number generator's sequence.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface IntType {
    int min() default Integer.MIN_VALUE;
    int max() default Integer.MAX_VALUE;
    Class<? extends CanProvide> provider() default IntProvider.class;
}
