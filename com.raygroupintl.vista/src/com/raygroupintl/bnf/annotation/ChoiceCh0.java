package com.raygroupintl.bnf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ChoiceCh0 {
	public String[] value() default {};
	public String[] preds() default {};
	public String def() default "";
}
