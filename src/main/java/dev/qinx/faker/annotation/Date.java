package dev.qinx.faker.annotation;

import dev.qinx.faker.internal.CanProvide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Date {
    String format() default "yyyy-MM-dd";
    Class<? extends CanProvide> provider() default dev.qinx.faker.provider.LocalDateProvider.class;
}
