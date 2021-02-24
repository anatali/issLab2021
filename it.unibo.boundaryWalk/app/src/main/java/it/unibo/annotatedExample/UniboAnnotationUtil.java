package it.unibo.annotatedExample;

import it.unibo.aboutAnnotations.ISSInfo;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

public class UniboAnnotationUtil {
    public static String getActorName( AnnotatedElement element ){
        try {
            Annotation[] classAnnotations = element.getAnnotations();
            for (Annotation annotation : classAnnotations) {
                if (annotation instanceof ISSActorSpec) {
                    ISSActorSpec info = (ISSActorSpec) annotation;
                    return  info.actorName();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "error about actorName";
    }


    public static void initializeObject(Object object)   {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(InitSpec.class)) {
                method.setAccessible(true);
                try{
                    System.out.println("initializeObject invoke");
                    method.invoke(object);
                }catch( Exception e ){
                    e.printStackTrace();
                }
            }
        }
    }

}