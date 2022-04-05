.. role:: red 
.. role:: blue 
.. role:: remark
.. role:: worktodo

.. _Java annotation: https://en.wikipedia.org/wiki/Java_annotation
.. _Java Reflection: https://www.oracle.com/technical-resources/articles/java/javareflection.html
.. _Java Type Annotations: https://docs.oracle.com/javase/tutorial/java/annotations/type_annotations.html
.. _Spring and Spring Boot: https://www.baeldung.com/spring-vs-spring-boot
.. _Spring Controllers: https://www.baeldung.com/spring-controllers

======================================
Annotazioni
======================================

:remark:`Le annotazioni sono una forma di metadati`

in quanto forniscono informazioni su un programma, e queste non fanno parte del programma stesso.

.. Le annotazioni non hanno alcun effetto diretto sul funzionamento del codice che annotano.

:remark:`Le annotazioni non influiscono direttamente sulla semantica del programma`

ma influiscono sul modo in cui i programmi vengono 
trattati da **strumenti** e **librerie**, che a loro volta possono influenzare la semantica del programma in esecuzione.

Le `Java annotation`_ sono state introdotte nella  release 5 (Tiger).
Prima del rilascio di Java SE 8, le annotazioni potevano essere applicate solo alle dichiarazioni. 
A partire dalla versione Java SE 8, le annotazioni possono essere applicate in ogni situazione in cui si ua un tipo
(si veda `Java Type Annotations`_)
allo scopo di realizzare uno più forte controllo di tipo (si pensi ad esempio a :blue:`@NonNull` ).

Oggi le annotazioni sono quasi ovunque e sono introdotte per diversi scopi:

- per dare semantica aggiuntiva a vari elementi di una classe, che 
  possono aiutare a meglio comprenderne l'intento;
- per permettere ulteriori controlli in fase di compilazione che garantiscono il rispetto di vari vincoli;
- per dare supporto ad analisi aggiuntiva del codice  tramite strumenti sensibili alle annotazioni.


Le annotazioni possono essere elaborate  in fase di compilazione e/o in fase di esecuzione,
sfruttando in questo caso le API di `Java Reflection`_, con possibile impatto sulle prestazioni, se non usate con attenzione.

Forse il più grande vantaggio delle annotazioni è 
dare supporto a un **paradigma di progettazione basato su configurazione esplicita**, il che 
permette di semplificare diversi aspetti della configurazione, con 
grande impatto sul processo di sviluppo. 

Framework molto diffusi che sfruttano questo aspetto sono `Spring and Spring Boot`_ che useremo più avanti, quando vorremo dotare 
le nostre applicazioni di una WebGUI.


Per il momento ci limitiamo a illustrare i meccanismi Java che permettono di sfruttare le annotazioni nelle nostre applicazioni.
 

----------------------------------------
Annotazioni in Java
----------------------------------------
In Java, un tipo annotazione è una forma di interfaccia,  quindi la si può implementare e utilizzare un'istanza. 

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