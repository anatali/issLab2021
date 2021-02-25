package it.unibo.annotatedExample;

import it.unibo.aboutAnnotations.ApplISSInfoUsage;
import it.unibo.aboutAnnotations.ISSInfo;
import it.unibo.aboutAnnotations.ISSInfoUsage;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

@ISSActorSpec( actorName = "actor0" )
public class ApplISSActor extends ISSActor{

    @InitSpec
    public void myshow(){
        System.out.println( myname + "  | myname= " + myname);
    }

    public static void main(String args[]) throws Exception {
        new ApplISSActor( ); //.show(); ;
    }

}
