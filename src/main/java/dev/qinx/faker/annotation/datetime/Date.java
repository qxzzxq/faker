package dev.qinx.faker.annotation.datetime;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.datetime.LocalDateProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a LocalDate object with a given format for the annotated field. This annotation could also be used
 * on a String field.
 *
 * <p>The default format is the ISO-8601 extended date format, like "2011-12-03"</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Date {
    String format() default "";

    String seed() default "";

    Class<? extends CanProvide> provider() default LocalDateProvider.class;
}
