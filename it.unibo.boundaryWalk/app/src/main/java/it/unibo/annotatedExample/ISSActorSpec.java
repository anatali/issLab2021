package it.unibo.annotatedExample;

import java.lang.annotation.*;

@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public  @interface ISSActorSpec {
     //String sendMsg( );
}

/*
TODO: a unibo class takes into account ISSActor annotations to implement the concept of Actor
 */