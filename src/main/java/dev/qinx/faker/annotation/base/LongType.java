package dev.qinx.faker.annotation.base;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.base.IntProvider;
import dev.qinx.faker.provider.base.LongProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface LongType {
    long min() default Long.MIN_VALUE;
    long max() default Long.MAX_VALUE;
    Class<? extends CanProvide> provider() default LongProvider.class;
}
