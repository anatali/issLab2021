/**
 IssCommsFactory.java
 ===============================================================
 Creates a support that provides high-level IssOperations
 according to the annotation related to the given object
 or according to a protocol-configuration file
 ===============================================================
 */
package it.unibo.robotSupports;
import it.unibo.annotations.IssAnnotationUtil;
import it.unibo.interaction.IssOperations;
import it.unibo.annotations.ProtocolInfo;

public class IssCommsSupportFactory {
    //Factory Method
    public static IssOperations create(Object obj ){
        ProtocolInfo protocolInfo = IssAnnotationUtil.getProtocol(  obj );
        String protocol     = protocolInfo.getProtocol();
        String url          = protocolInfo.getUrl();
        return create(protocol,url);
        /*
        System.out.println("        IssCommsSupportFactory | protocolInfo=" + protocol + " " + url );
        switch( protocol ){
            case "HTTP"  : {  return new IssHttpSupport(  url );  }
            case "WS"    : {  return new IssWsSupport(  url );    }
            default: return new IssHttpSupport( url ); //TODO Exception?
        }

         */
    }
    //Factory Method
    public static IssOperations create(  String protocol, String url ){
         System.out.println("        IssCommsFactory | create protocol=" + protocol  );
        switch( protocol ){
            case "HTTP"  : {  return new IssHttpSupport(  url );  }
            case "WS"    : {  return new IssWsSupport( url );    }
            default: return new IssHttpSupport(  url ); //TODO Exception?
        }
    }
}

