package javaProject.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 注解类:标识该注解只能作用于方法之上
 * 	注解类要起作用必须标注元注解
 * @author 56525
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyValue {
	public String value() default "";
}
