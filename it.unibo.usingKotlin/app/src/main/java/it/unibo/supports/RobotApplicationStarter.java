package it.unibo.supports;

import it.unibo.annotations.ArilRobotSpec;
import it.unibo.annotations.IssAnnotationUtil;
import it.unibo.annotations.ProtocolInfo;
import it.unibo.annotations.VirtualRobotSpec;
import it.unibo.interaction.IssOperations;

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
                    IssCommSupport commSupport = IssCommsSupportFactory.create(p.getProtocol(), p.getUrl());
                    System.out.println("RobotApplicationStarter | commSupport=" + commSupport);
                    IssCommSupport rs = new IssArilRobotSupport(robotConfigFName, commSupport);
                    Object obj = clazz.getDeclaredConstructor(IssOperations.class).newInstance(rs);
                    //System.out.println("RobotApplicationStarter ArilRobotSpec | obj=" + obj  );
                    return obj;
                }
            }//for
            return null;
        }catch( Exception e){
            System.out.println("RobotApplicationStarter | createInstance ERROR: " + e.getMessage()  );
            return null;
        }
    }
}
