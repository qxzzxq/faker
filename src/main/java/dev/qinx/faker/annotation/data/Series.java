package dev.qinx.faker.annotation.data;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.StringProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Series {
    String id() default "";

    Class<? extends CanProvide> provider() default StringProvider.class;
}
