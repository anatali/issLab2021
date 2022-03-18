/*
ISSActor.java
 */
package it.unibo.annotatedExample;


/*
 * 
 */

@ISSActorSpec( actorName = "actordefault" )  //Solo se ElementType.TYPE
public abstract class ISSActor {
    protected String myname = "unknown";

     
    public ISSActor( ){
        myname = ExampleAnnotationUtil.getActorName( this.getClass() );
        ExampleAnnotationUtil.initializeObject( this );   //calls a method of the user class
        System.out.println("ISSActor | create " + myname);
    }

    //@InitSpec
    public void setUp() {
        System.out.println("ISSActor | setUp " +  myname);   	
    }
    
    //@InitSpec
    public abstract void show();
}
