package it.unibo.aboutAnnotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationReader {

    static void readAnnotation(AnnotatedElement element) {
        try {
            Annotation[] classAnnotations = element.getAnnotations();
            for (Annotation annotation : classAnnotations) {
                if (annotation instanceof ISSInfo) {
                    ISSInfo info = (ISSInfo) annotation;
                    System.out.println("" + info.item() + " by:" + info.author() + " level=" + info.infoLevel() );
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static String getInfoLevel( AnnotatedElement element ){
        try {
            Annotation[] classAnnotations = element.getAnnotations();
            for (Annotation annotation : classAnnotations) {
                if (annotation instanceof ISSInfo) {
                    ISSInfo info = (ISSInfo) annotation;
                    return  info.infoLevel().toString() + " by:" + info.author();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "error about info level";
    }
}
