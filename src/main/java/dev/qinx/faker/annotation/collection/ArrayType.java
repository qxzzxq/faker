package dev.qinx.faker.annotation.collection;

import dev.qinx.faker.provider.Provider;
import dev.qinx.faker.provider.collection.ArrayProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate an array for the annotated field. Can be used with other annotation, for example: <code>@ArrayType @Gaussian</code>
 *
 * <p>
 * By default, the generated array will contain 3 elements. This could be adjusted by setting <code>totalLength = N</code>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ArrayType {
    int length() default 3;

    String seed() default "";

    Class<? extends Provider> provider() default ArrayProvider.class;
}
