package dev.qinx.faker.annotation.base;

import dev.qinx.faker.provider.Provider;
import dev.qinx.faker.provider.base.FloatProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a random float for the annotated filed.
 * The value will be uniformly distributed between min and max value
 * from this random number generator's sequence.
 *
 * <p>The default range is <code>[0F, 1F]</code></p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FloatType {
    float min() default 0F;
    float max() default 1F;
    String seed() default "";
    Class<? extends Provider> provider() default FloatProvider.class;
}
