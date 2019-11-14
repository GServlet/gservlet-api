package org.gservlet.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.servlet.annotation.WebInitParam;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Servlet {

	String name() default "";
    String[] value() default {};
    String[] urlPatterns() default {};
    int loadOnStartup() default -1;
    WebInitParam [] initParams() default {};
    boolean asyncSupported() default false;
    String smallIcon() default "";
    String largeIcon() default "";
    String description() default "";
    String displayName() default "";

}