package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.FloatProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a random float value for the annotated filed.
 * The value will be uniformly distributed between Float.MIN_VALUE and Float.MAX_VALUE
 * from this random number generator's sequence.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FloatType {
    float min() default Float.MIN_VALUE;
    float max() default Float.MAX_VALUE;
    Class<? extends CanProvide> provider() default FloatProvider.class;
}
