package it.unibo.interaction;
import java.lang.annotation.Annotation;

public class UniboRobotApplicationStarter {
    private static final String protcolConfigFName = "IssProtocolConfig.txt";
    private static final String robotConfigFName   = "IssRobotConfig.txt";

    public static void run( Class<?> clazz ) {
        try {
            System.out.println("UniboRobotApplicationStarter | run " + clazz.getName());
            Annotation[] annotations = clazz.getAnnotations();

            System.out.println("UniboRobotApplicationStarter | annotations " + annotations.length);
            for (Annotation annot : annotations) {
                if (annot instanceof UniboRobotApplication) {
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protcolConfigFName);
                    IssOperations commSupport        = IssCommsFactory.create( p.protocol.toString(), p.url  );
                    System.out.println("UniboRobotApplicationStarter | commSupport=" + commSupport );
                    IssOperations arilRobotSupport   = new IssArilRobotSupport( robotConfigFName, commSupport );
                    System.out.println("UniboRobotApplicationStarter | arilRobotSupport=" + arilRobotSupport );
                    IssAppOperations rs              = new IssAppRobotSupport( arilRobotSupport );
                    System.out.println("UniboRobotApplicationStarter | rs=" + rs  );
                    Object obj = clazz.getDeclaredConstructor(IssAppOperations.class).newInstance(rs);
                    //System.out.println("UniboRobotApplicationStarter | obj=" + obj  );
                }
            }
        }catch( Exception e){
            System.out.println("UniboRobotApplicationStarter | ERROR: " + e.getMessage()  );
        }
    }

    public static Object createInstance( Class<?> clazz ) {
        try {
            System.out.println("UniboRobotApplicationStarter | createInstance " + clazz.getName());
            Annotation[] annotations = clazz.getAnnotations();
            //System.out.println("UniboRobotApplicationStarter | annotations " + annotations.length);
            for (Annotation annot : annotations) {
                if (annot instanceof UniboRobotApplication) {
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protcolConfigFName);
                    IssOperations commSupport        = IssCommsFactory.create( p.protocol.toString(), p.url  );
                    System.out.println("UniboRobotApplicationStarter | createInstance commSupport=" + commSupport );
                    IssOperations arilRobotSupport   = new IssArilRobotSupport( robotConfigFName, commSupport );
                    System.out.println("UniboRobotApplicationStarter | createInstance arilRobotSupport=" + arilRobotSupport );
                    IssAppOperations rs              = new IssAppRobotSupport( arilRobotSupport );
                    System.out.println("UniboRobotApplicationStarter | rs=" + rs  );
                    Object obj = clazz.getDeclaredConstructor(IssAppOperations.class).newInstance(rs);
                    //System.out.println("UniboRobotApplicationStarter | obj=" + obj  );
                    return  obj;
                }
             }
            return null;
        }catch( Exception e){
            System.out.println("UniboRobotApplicationStarter | createInstance ERROR: " + e.getMessage()  );
            return null;
        }
    }
}
