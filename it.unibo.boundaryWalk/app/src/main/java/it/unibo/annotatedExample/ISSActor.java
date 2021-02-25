/*
ISSActor.java
 */
package it.unibo.annotatedExample;



//@ISSActorSpec( actorName = "actordefault" )
public abstract class ISSActor {
    protected String myname = "unknown";

    @ISSActorSpec
    public ISSActor( ){
        myname = UniboAnnotationUtil.getActorName( this.getClass() );
        UniboAnnotationUtil.initializeObject( this );   //calls a method of the user class
        System.out.println("ISSActor | create " + myname);
    }
/*
    @InitSpec
    public void show(){
        System.out.println( myname + " ISSActor show | myname= " + myname);
    }*/

    public abstract void show();
}
