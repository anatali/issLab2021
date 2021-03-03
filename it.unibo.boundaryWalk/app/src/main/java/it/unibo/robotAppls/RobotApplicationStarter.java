package it.unibo.robotAppls;
import it.unibo.annotations.*;
import it.unibo.interaction.IssAppOperations;
import it.unibo.interaction.IssOperations;
import it.unibo.robotSupports.IssAppRobotSupport;
import it.unibo.robotSupports.IssArilRobotSupport;
import it.unibo.robotSupports.IssCommsSupportFactory;

import java.lang.annotation.Annotation;

public class RobotApplicationStarter {
    private static final String protcolConfigFName = "IssProtocolConfig.txt";
    private static final String robotConfigFName   = "IssRobotConfig.txt";

    public static Object createInstance( Class<?> clazz ) {
        try {
            System.out.println("RobotApplicationStarter | createInstance " + clazz.getName());
            Annotation[] annotations = clazz.getAnnotations();
            //System.out.println("RobotApplicationStarter | annotations " + annotations.length);
            for (Annotation annot : annotations) {
                if (annot instanceof UniboRobotSpec) {
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protcolConfigFName);
                    IssOperations commSupport = IssCommsSupportFactory.create(p.getProtocol(), p.getUrl());
                    System.out.println("RobotApplicationStarter | createInstance commSupport=" + commSupport);
                    IssOperations arilRobotSupport = new IssArilRobotSupport(robotConfigFName, commSupport);
                    System.out.println("RobotApplicationStarter | createInstance arilRobotSupport=" + arilRobotSupport);
                    IssAppOperations rs = new IssAppRobotSupport(arilRobotSupport);
                    System.out.println("RobotApplicationStarter | rs=" + rs);
                    Object obj = clazz.getDeclaredConstructor(IssAppOperations.class).newInstance(rs);
                    //System.out.println("RobotApplicationStarter UniboRobotSpec | obj=" + obj  );
                    return obj;
                }
                if (annot instanceof VirtualRobotSpec) {
                    ProtocolInfo p   = IssAnnotationUtil.checkProtocolConfigFile(protcolConfigFName);
                    IssOperations rs = IssCommsSupportFactory.create(p.getProtocol(), p.getUrl());
                    System.out.println("RobotApplicationStarter | commSupport=" + rs);
                    Object obj = clazz.getDeclaredConstructor(IssOperations.class).newInstance(rs);
                    //System.out.println("RobotApplicationStarter VirtualRobotSpec | obj=" + obj  );
                    return obj;
                }
                if (annot instanceof ArilRobotSpec) {
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protcolConfigFName);
                    IssOperations commSupport = IssCommsSupportFactory.create(p.getProtocol(), p.getUrl());
                    System.out.println("RobotApplicationStarter | commSupport=" + commSupport);
                    IssOperations rs = new IssArilRobotSupport(robotConfigFName, commSupport);
                    Object obj = clazz.getDeclaredConstructor(IssAppOperations.class).newInstance(rs);
                    //System.out.println("RobotApplicationStarter ArilRobotSpec | obj=" + obj  );
                    return obj;
                }            }//for
            return null;
        }catch( Exception e){
            System.out.println("RobotApplicationStarter | createInstance ERROR: " + e.getMessage()  );
            return null;
        }
    }
}
/*
    public static void run( Class<?> clazz ) {
        try {
            System.out.println("RobotApplicationStarter | run " + clazz.getName());
            Annotation[] annotations = clazz.getAnnotations();

            System.out.println("RobotApplicationStarter | annotations " + annotations.length);
            for (Annotation annot : annotations) {
                if (annot instanceof UniboRobotSpec) {
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protcolConfigFName);
                    IssOperations commSupport        = IssCommsFactory.create( p.protocol.toString(), p.url  );
                    System.out.println("RobotApplicationStarter | commSupport=" + commSupport );
                    IssOperations arilRobotSupport   = new IssArilRobotSupport( robotConfigFName, commSupport );
                    System.out.println("RobotApplicationStarter | arilRobotSupport=" + arilRobotSupport );
                    IssAppOperations rs              = new IssAppRobotSupport( arilRobotSupport );
                    System.out.println("RobotApplicationStarter | rs=" + rs  );
                    Object obj = clazz.getDeclaredConstructor(IssAppOperations.class).newInstance(rs);
                    //System.out.println("RobotApplicationStarter | obj=" + obj  );
                    return;
                }
                if (annot instanceof VirtualRobotSpec) {
                    ProtocolInfo p = IssAnnotationUtil.checkProtocolConfigFile(protcolConfigFName);
                    IssOperations commSupport   = IssCommsFactory.create( p.protocol.toString(), p.url  );
                    System.out.println("RobotApplicationStarter | commSupport=" + commSupport );
                    IssOperations rs            = new IssArilRobotSupport( robotConfigFName, commSupport );
                    Object obj = clazz.getDeclaredConstructor(IssAppOperations.class).newInstance(rs);
                    //System.out.println("RobotApplicationStarter | obj=" + obj  );
                    return;

                }
            }
        }catch( Exception e){
            System.out.println("RobotApplicationStarter | ERROR: " + e.getMessage()  );
        }
    }
*/