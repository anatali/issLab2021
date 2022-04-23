package unibo.wenvUsage22.annot;

import java.lang.annotation.*;


 
@Target (ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(TransitionsGuarded.class)
public @interface TransitionGuarded {
	String name() default "t0";
	String stateOk()  ;
	String stateKo()  ;
	String msgId()  ;
}
