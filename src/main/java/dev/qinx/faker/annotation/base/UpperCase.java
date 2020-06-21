package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.StringProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generate a string with each placeholder in pattern replaced according to the following rules:
 *
 * <ul>
 *     <li>Question marks (‘?’) are replaced with a random character from letters.</li>
 * </ul>
 * <p>
 * By default, letters contains all uppercase ASCII letters.
 * <p>
 * The default pattern is '<code>?</code>'
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface UpperCase {
    String pattern() default "?";

    String letters() default "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    String digits() default "";

    String seed() default "";

    Class<? extends CanProvide> provider() default StringProvider.class;
}
