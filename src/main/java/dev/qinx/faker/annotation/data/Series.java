package dev.qinx.faker.annotation.data;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.data.SeriesProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Series {
    String id() default "";
    int length() default 3;
    String crossJoin() default "";

    Class<? extends CanProvide> provider() default SeriesProvider.class;
}
