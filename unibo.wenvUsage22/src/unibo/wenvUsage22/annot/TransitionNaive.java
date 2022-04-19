package unibo.wenvUsage22.annot;

import java.lang.annotation.*;


@Target (ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface TransitionNaive {
	String[] state()  ;
	String[] msgId()  ;
}
