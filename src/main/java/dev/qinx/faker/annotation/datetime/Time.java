package dev.qinx.faker.annotation.datetime;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.datetime.LocalTimeProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Time {
    String format() default "";
    String seed() default "";
    Class<? extends CanProvide> provider() default LocalTimeProvider.class;
}
