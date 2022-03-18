/*
ISSActor.java
 */
package it.unibo.annotatedExample;



@ISSActorSpec( actorName = "actordefault" )
public abstract class ISSActor {
    protected String myname = "unknown";

    @ISSActorSpec
    public ISSActor( ){
        myname = ExampleAnnotationUtil.getActorName( this.getClass() );
        ExampleAnnotationUtil.initializeObject( this );   //calls a method of the user class
        System.out.println("ISSActor | create " + myname);
    }

    public abstract void show();
}
