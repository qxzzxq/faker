package dev.qinx.faker.annotation.geo;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.datetime.LocalDateProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Lon {
    String onLand() default "false";
    Class<? extends CanProvide> provider() default LocalDateProvider.class;
}
