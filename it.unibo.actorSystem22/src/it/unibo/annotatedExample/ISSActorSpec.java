package it.unibo.annotatedExample;

import java.lang.annotation.*;

/*
 * Annotazione ereditabile applicabile al costruttore e ai metodi di una classe
 */
@Target(value = { ElementType.TYPE })   //ElementType.CONSTRUCTOR, ElementType.METHOD, 
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public  @interface ISSActorSpec {
     //String sendMsg( );
     String actorName() default "unknown";   //annotations and interfaces can't have constructors
}

