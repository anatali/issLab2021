/**
 IssCommunications.java
 ===============================================================
 Creates a support that provides high-level IssOperations
 according to the annotation related to the given object
 or according to a protocol-configuration file
 ===============================================================
 */
package it.unibo.interaction;

public class IssCommsFactory {
    //Factory Method
    public static IssOperations create( Object obj ){
        ProtocolInfo protocolInfo = IssAnnotationUtil.getProtocol(  obj );
        System.out.println("IssCommunications | create protocolInfo=" + protocolInfo.protocol + " " + protocolInfo.url );
        switch( protocolInfo.protocol ){
            case HTTP  : {  return new IssHttpSupport( protocolInfo.url );  }
            case WS    : {  return new IssWsSupport( protocolInfo.url );    }
            default: return new IssHttpSupport( protocolInfo.url ); //TODO Exception?
        }
    }
}

