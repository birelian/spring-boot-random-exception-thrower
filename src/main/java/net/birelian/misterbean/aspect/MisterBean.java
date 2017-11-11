package net.birelian.misterbean.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MisterBean annotation.
 *
 * Methods annotated with @MisterBean may throw (or not) an exception of a type defined in the application properties.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MisterBean {
}
