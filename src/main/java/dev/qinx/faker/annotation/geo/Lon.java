package dev.qinx.faker.annotation.geo;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.geo.CoordinatesProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Lon {
    double min() default -180D;
    double max() default 180D;
    String onLand() default "false";
    Class<? extends CanProvide> provider() default CoordinatesProvider.class;
}
