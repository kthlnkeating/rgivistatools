package com.raygroupintl.parser.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface CharSpecified {
	public char[] chars() default {};
	public char[] ranges() default {};
	public char[] excludechars() default {};
	public char[] excluderanges() default{};
	public boolean single() default false;
}
