package dev.qinx.faker.annotation.datetime;

import dev.qinx.faker.provider.Provider;
import dev.qinx.faker.provider.datetime.LocalDateTimeProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a LocalDateTime object with a given format for the annotated field. This annotation could also be used
 * on a String field.
 *
 * <p>The default format is the ISO-8601 extended date-time format, like "2011-12-03T10:15:30"</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DateTime {
    String format() default "";

    String seed() default "";

    Class<? extends Provider> provider() default LocalDateTimeProvider.class;
}
