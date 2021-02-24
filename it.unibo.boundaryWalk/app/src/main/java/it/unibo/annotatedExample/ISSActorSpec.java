package it.unibo.annotatedExample;

import java.lang.annotation.*;

@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public  @interface ISSActorSpec {
     //String sendMsg( );
     String actorName() default "unknown";   //annotations and interfaces can't have constructors

}

