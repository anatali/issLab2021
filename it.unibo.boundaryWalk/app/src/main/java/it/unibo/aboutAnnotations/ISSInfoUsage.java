package it.unibo.aboutAnnotations;


@ISSInfo (
        infoLevel = ISSInfo.Level.IMPORTANT,
        item      = "class ISSInfoUsage",
        author    = "AN-DISI"
)
public class ISSInfoUsage {

    public String getInfoLevel(){
        return AnnotationReader.getInfoLevel( this.getClass() ) ;
    }
    public void useInfoLevel(String msg) {
        //AnnotationReader.readAnnotation(ISSInfoUsage.class);
        System.out.println( msg + " info level:" +  getInfoLevel() );
    }

}