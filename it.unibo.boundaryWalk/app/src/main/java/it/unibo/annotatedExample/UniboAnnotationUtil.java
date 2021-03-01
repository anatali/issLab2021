package it.unibo.annotatedExample;
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
        //println("initializeObject object=" + object);
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            //System.out.println("initializeObject method=" + method);
            if (method.isAnnotationPresent(InitSpec.class)) {
                method.setAccessible(true);
                try{
                    //System.out.println("initializeObject invoke " + method);
                    method.invoke(object);
                }catch( Exception e ){
                    e.printStackTrace();
                }
            }
        }
    }

}