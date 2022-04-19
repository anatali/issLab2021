package unibo.wenvUsage22.annot;

import java.lang.annotation.*;


@Target (ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Transition {
	String[] state()  ;
	String[] msgId()  ;
}
