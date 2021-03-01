/**
 ApplISSActor.java
 =========================================================================
 An annotated class that exploits annotations (instead of inheritance)
 to introduce built-in behavior in declarative way
 =========================================================================
 */
package it.unibo.annotatedExample;


@ISSActorSpec( actorName = "actor0" )
public class ApplISSActor extends ISSActor{

    //@InitSpec
    public void show(){
        //super.show();
        System.out.println( myname + " ApplISSActor | myname= " + myname);
    }

    public static void main(String args[]) throws Exception {
        new ApplISSActor( ); //.show(); ;
    }

}
