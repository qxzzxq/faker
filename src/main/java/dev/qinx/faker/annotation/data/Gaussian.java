package dev.qinx.faker.annotation.data;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.data.GaussianProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate pseudorandom, Gaussian ("normally") distributed double values with a default mean 0.0 and default
 * standard deviation 1.0.
 *
 * This annotation could also be applied on a field of String or a field of Option[Double]
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Gaussian {
    double std() default 1D;
    double mean() default 0D;
    String seed() default "";
    Class<? extends CanProvide> provider() default GaussianProvider.class;
}
