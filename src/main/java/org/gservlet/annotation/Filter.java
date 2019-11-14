package org.gservlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.servlet.DispatcherType;
import javax.servlet.annotation.WebInitParam;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

public @interface Filter {

    String description() default "";
    String displayName() default "";
    WebInitParam[] initParams() default {};
    String filterName() default "";
    String smallIcon() default "";
    String largeIcon() default "";
    String[] servletNames() default {};
    String[] value() default {};
    String[] urlPatterns() default {};
    DispatcherType[] dispatcherTypes() default {DispatcherType.REQUEST};
    boolean asyncSupported() default false;

}