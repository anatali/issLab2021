package it.unibo.interaction;

import java.lang.annotation.*;

@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE   })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface UniboRobotApplication {
}
