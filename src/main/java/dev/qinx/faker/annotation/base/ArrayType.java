package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.ArrayProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate an array for the annotated field.
 *
 * <p>
 * By default, the generated array will contain 3 elements. This could be adjusted by setting <code>length = N</code>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ArrayType {
    int length() default 3;

    String seed() default "";

    Class<? extends CanProvide> provider() default ArrayProvider.class;
}
