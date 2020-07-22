package dev.qinx.faker.annotation.base;

import dev.qinx.faker.provider.Provider;
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
 * <p>By default, letters contains all ASCII letters, uppercase and lowercase and digits contains [0-9],
 * and the default pattern is '<code>## ??</code>'</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Text {
    String pattern() default "## ??";

    String letters() default "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    String digits() default "0123456789";

    String seed() default "";

    Class<? extends Provider> provider() default StringProvider.class;
}
