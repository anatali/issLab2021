package it.unibo.annotatedExample;

/*
A unibo class takes into account ISSActor annotations to implement the concept of Actor
 */

//@ISSActorSpec( actorName = "actordefault" )
public class ISSActor {
    protected String myname = "unknown";

    @ISSActorSpec
    public ISSActor( ){
        myname = UniboAnnotationUtil.getActorName( this.getClass() );
        UniboAnnotationUtil.initializeObject(myname);
        System.out.println("ISSActor | create " + myname);
    }

    @InitSpec
    public void show(){
        System.out.println( myname + "  | myname= " + myname);
    }
}
