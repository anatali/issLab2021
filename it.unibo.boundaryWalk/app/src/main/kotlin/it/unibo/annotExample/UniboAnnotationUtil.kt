package it.unibo.annotExample
import java.lang.reflect.AnnotatedElement

object UniboAnnotationUtil {
    fun getActorName(element: AnnotatedElement): String {
        try {
            val classAnnotations = element.annotations
            for (annotation in classAnnotations) {
                if (annotation is ISSActorSpec) {
                    return annotation.actorName
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
        return "error about actorName"
    }

    fun initializeObject(obj : Any) {
        println("initializeObject object=" + obj)
        val clazz: Class<*> = obj.javaClass
        for (method in clazz.declaredMethods) {
            //println("initializeObject method=" + method);
            if (method.isAnnotationPresent(InitSpec::class.java)) {
                method.isAccessible = true
                try {
                    println("initializeObject invoke " + method);
                    method.invoke( obj )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}