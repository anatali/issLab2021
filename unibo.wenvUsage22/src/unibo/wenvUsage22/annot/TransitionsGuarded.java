package unibo.wenvUsage22.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target (ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TransitionsGuarded {
	TransitionGuarded[] value();
}
