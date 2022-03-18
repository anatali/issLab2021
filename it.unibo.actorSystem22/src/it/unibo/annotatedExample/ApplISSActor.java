/**
 ApplISSActor.java
 =========================================================================
 An annotated class that exploits annotations (instead of inheritance)
 to introduce built-in behavior in declarative way
 =========================================================================
 */
package it.unibo.annotatedExample;

/*
 * Classe che specializza una classe annotata ereditando l'annotazione
 * a livello di costruttore
 */
//@ISSActorSpec( actorName = "actor0" )
public class ApplISSActor extends ISSActor{

    //@InitSpec
    public void show(){
        System.out.println( myname + " ApplISSActor | myname= " + myname);
    }

}
