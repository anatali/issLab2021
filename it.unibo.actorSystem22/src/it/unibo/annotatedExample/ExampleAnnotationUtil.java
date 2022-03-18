package it.unibo.annotatedExample;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import it.unibo.actorComm.utils.ColorsOut;
 

public class ExampleAnnotationUtil {
	
    public static String getActorName( AnnotatedElement element ){
        try {
           ColorsOut.outappl("getActorName element=" + element, ColorsOut.BLUE);
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
        ColorsOut.outappl("initializeObject object=" + object, ColorsOut.BLUE);
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(InitSpec.class)) {
                method.setAccessible(true);
                try{
                    ColorsOut.outappl("initializeObject invoke " + method, ColorsOut.BLUE);
                    method.invoke(object);
                }catch( Exception e ){
                    e.printStackTrace();
                }
            }
        }
    }

}