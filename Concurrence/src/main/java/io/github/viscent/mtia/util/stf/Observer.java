package io.github.viscent.mtia.util.stf;

import  java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import  java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Observer {
	Expect[] value();
}
