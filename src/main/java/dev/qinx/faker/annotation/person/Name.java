package dev.qinx.faker.annotation.person;

import dev.qinx.faker.enums.Gender;
import dev.qinx.faker.enums.Locale;
import dev.qinx.faker.provider.Provider;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Name {
    Locale locale() default Locale.en;
    Gender gender() default Gender.All;
    String firstName() default "true";
    String lastName() default "true";
    String seed() default "";
    Class<? extends Provider> provider() default dev.qinx.faker.provider.person.NameProvider.class;
}
