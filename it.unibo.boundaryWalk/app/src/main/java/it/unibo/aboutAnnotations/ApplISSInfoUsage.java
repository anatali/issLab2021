package it.unibo.aboutAnnotations;

@ISSInfo (
        infoLevel = ISSInfo.Level.TRIVIAL,
        item      = "class ApplISSInfoUsage",
        author    = "XYZ"
)
public class ApplISSInfoUsage extends ISSInfoUsage{
    public void test(){
        System.out.println( "ApplISSInfoUsage | info level:" +  getInfoLevel() );
    }
}