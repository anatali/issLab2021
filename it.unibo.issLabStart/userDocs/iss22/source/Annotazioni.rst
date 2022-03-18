
======================================
Annotazioni
======================================


----------------------------------------
Annotazioni in Java
----------------------------------------
In Java, un tipo annotazione è una forma di interfaccia, 
quindi la si può implementare e utilizzare un'istanza. 

.. code:: 

    @Target( value = {ElementType.CONSTRUCTOR,ElementType.METHOD, ElementType.TYPE} )
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    public @interface IssProtocolSpec {
        enum issProtocol {UDP,TCP,HTTP,MQTT,COAP,WS} ;
        issProtocol protocol() default issProtocol.TCP;
        String url() default "unknown";
        String configFile() default "IssProtocolConfig.txt";
    }

++++++++++++++++++++++++++++++++++++++++
Meta-Annotation
++++++++++++++++++++++++++++++++++++++++

Per specificare il comportamento delle Annotation possiamo utilizzare altre Annotation, che 
vengono definite **Meta-Annotation**.

%%%%%%%%%%%%%%%%% 
@Target 
%%%%%%%%%%%%%%%%%

@Target: permette di definire a quale parte del codice può essere collegata l'Annotation


.. code:: 

    ElementType.PACKAGE	        Si applica alla definizione del package
    ElementType.TYPE	        Si applica alla definizione di classi,
                                interfacce ed enumeration
    ElementType.FIELD	        Si applica agli attributi
    ElementType.METHOD	        Si applica ai metodi
    ElementType.PARAMETER	    Si applica ai parametri dei metodi
    ElementType.CONSTRUCTOR	    Si applica al costruttore
    ElementType.LOCAL_VARIABLE	Si applica ad una variabile locale

%%%%%%%%%%%%%%%%% 
@Retention 
%%%%%%%%%%%%%%%%%

@Retention: specifica come saranno visibili le informazioni collegate all’Annotation.

%%%%%%%%%%%%%%%%% 
@Documented 
%%%%%%%%%%%%%%%%%

@Documented: serve per includere l’Annotation nel javadoc, visto che per default sono escluse.

%%%%%%%%%%%%%%%%% 
@Inherited 
%%%%%%%%%%%%%%%%%

@Inherited: una classe che utilizza l'Annotation, la fa ereditare anche alle classi figlie.


++++++++++++++++++++++++++++++++++++++++
Esempio di uso
++++++++++++++++++++++++++++++++++++++++


.. code:: 

    @IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url      = "http://localHost:8090/api/move"
    )
    public class TestClass {
        private IssOperations support;
        public TestClass(){
             
        }
    }

Dobbiamo vedere come poter recuperare le informazioni che abbiamo inserito tramite annotazioni
usando le Reflection API.

.. code:: 

    public class AnnotationReader {
        public static void main(String args[]) throws Exception {
            readAnnotation(TestClass.class);
        }

        static void readAnnotation(AnnotatedElement element) {
            try {
                Annotation[] classAnnotations = element.getAnnotations();
                for (Annotation annotation : classAnnotations) {
                    if (annotation instanceof Source) {
                        Source source = (Source) annotation;
                        System.out.println("Autore:" + source.autore());
                        System.out.println("Versione:" + source.versione());
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }







----------------------------------------
Annotazioni in Kotlin
----------------------------------------

In alternativa a questo meccanismo, Kotlin  consente di chiamare un costruttore 
di una classe  annotazione in codice arbitrario e allo stesso modo di utilizzare 
l'istanza risultante.


.. code:: 

    annotation class InfoMarker(val info: String)

    fun processInfo(marker: InfoMarker): Unit = TODO()

    fun main(args: Array<String>) {
        if (args.isNotEmpty())
            processInfo(getAnnotationReflective(args))
        else
            processInfo(InfoMarker("default"))
    }