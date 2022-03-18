package it.unibo.annotatedExample;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.CONSTRUCTOR, ElementType.METHOD} )
@Inherited
public @interface InitSpec {
}


