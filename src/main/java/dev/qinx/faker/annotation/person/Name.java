package dev.qinx.faker.annotation.person;

import dev.qinx.faker.enums.Gender;
import dev.qinx.faker.enums.Local;
import dev.qinx.faker.internal.CanProvide;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Name {
    Local local() default Local.en;
    Gender gender() default Gender.All;
    String firstName() default "true";
    String lastName() default "true";
    Class<? extends CanProvide> provider() default dev.qinx.faker.provider.person.Name.class;
}
