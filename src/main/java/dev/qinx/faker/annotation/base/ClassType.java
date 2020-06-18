package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.ClassProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ClassType {
    Class<? extends CanProvide> provider() default ClassProvider.class;
}
