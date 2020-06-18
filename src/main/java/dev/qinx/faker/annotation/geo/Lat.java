package dev.qinx.faker.annotation.geo;

import dev.qinx.faker.internal.CanProvide;
import dev.qinx.faker.provider.geo.CoordinatesProvider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Lat {
    double min() default -90D;
    double max() default 90D;
    String onLand() default "false";
    Class<? extends CanProvide> provider() default CoordinatesProvider.class;
}
