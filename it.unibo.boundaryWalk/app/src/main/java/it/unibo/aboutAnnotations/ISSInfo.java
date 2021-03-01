package it.unibo.aboutAnnotations;

import java.lang.annotation.*;

@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ISSInfo {
    public enum Level {CRITICAL,IMPORTANT,TRIVIAL} ;
    Level infoLevel( ) default Level.TRIVIAL;
    String item( );
    String author( );
}