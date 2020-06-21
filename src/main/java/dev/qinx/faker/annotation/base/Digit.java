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
 * </ul>
 * <p>
 * By default, digits contains [0-9].
 * <p>
 * The default pattern is '<code>#</code>'
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Digit {
    String pattern() default "#";

    String letters() default "";

    String digits() default "0123456789";

    String seed() default "";

    Class<? extends CanProvide> provider() default StringProvider.class;
}
