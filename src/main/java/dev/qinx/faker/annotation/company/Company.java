package dev.qinx.faker.annotation.company;

import dev.qinx.faker.enums.Locale;
import dev.qinx.faker.internal.CanProvide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Provide a company name.
 *
 * <p>code: default false, set to true if you want the company code instead of name</p>
 * <p>real: default true, true to return a real S&P 500 company</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Company {
    boolean code() default false;
    boolean real() default true;
    Locale locale() default Locale.en;
    String seed() default "";
    Class<? extends CanProvide> provider() default dev.qinx.faker.provider.company.CompanyProvider.class;
}
