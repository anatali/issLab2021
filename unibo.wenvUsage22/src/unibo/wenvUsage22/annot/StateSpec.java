package unibo.wenvUsage22.annot;

import java.lang.annotation.*;


@Target (ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface StateSpec {
	String name() default "s0";
}
