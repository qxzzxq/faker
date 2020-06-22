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
 *     <li>Number signs (‘#’) are replaced with a random digit from digits.</li>
 *     <li>Question marks (‘?’) are replaced with a random character from letters.</li>
 * </ul>
 *
 * <p>By default, letters contains all lowercase ASCII letters and digits contains [0-9].
 * The default pattern is '<code>?</code>'</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LowerCase {
    String pattern() default "?";

    String letters() default "abcdefghijklmnopqrstuvwxyz";

    String digits() default "0123456789";

    String seed() default "";

    Class<? extends CanProvide> provider() default StringProvider.class;
}
