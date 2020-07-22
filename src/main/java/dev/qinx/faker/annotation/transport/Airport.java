package dev.qinx.faker.annotation.transport;

import dev.qinx.faker.provider.Provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Generate an Airport object (or an airport code if this annotation is applied to a String field).</p>
 *
 * <p>By default, any of the international large airports could be generated. User can control the generated airport
 * by providing an ISO country code like this: <code>@Airport(country = "FR")</code></p>
 *
 * <p>It's also possible to filter the generated airport by their size. This could be done by setting the following arguments:
 * </p>
 *
 * <ul>
 *     <li>largeAirport, default "true"</li>
 *     <li>mediumAirport, default "false"</li>
 *     <li>smallAirport, default "false"</li>
 *     <li>heliport, default "false"</li>
 * </ul>
 *
 * <p>The airport data come from https://ourairports.com/data/</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Airport {
    String country() default "";

    String largeAirport() default "true";

    String mediumAirport() default "false";

    String smallAirport() default "false";

    String heliport() default "false";

    String seed() default "";

    Class<? extends Provider> provider() default dev.qinx.faker.provider.transport.AirportProvider.class;
}
