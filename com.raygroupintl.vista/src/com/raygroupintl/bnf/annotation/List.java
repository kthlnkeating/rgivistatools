package com.raygroupintl.bnf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface List {
	public String value();
	public String delim() default "";
	public String left() default "";
	public String right() default "";
	public boolean empty() default false;
	public boolean none() default false;
	public boolean adderror() default false;
}
