package dev.qinx.faker.annotation.datetime;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.datetime.LocalTimeProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a LocalTime object with a given format for the annotated field. This annotation could also be used
 * on a String field.
 *
 * <p>The default format is the ISO-8601 extended time format, like "10:15:30"</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Time {
    String format() default "";

    String seed() default "";

    Class<? extends CanProvide> provider() default LocalTimeProvider.class;
}
