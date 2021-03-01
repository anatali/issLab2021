/**
 IssProtocolSpec.java
 ===============================================================
 User-defined annotation related to communication protocols
 ===============================================================
 */
package it.unibo.interaction;
import java.lang.annotation.*;

@Target(value = { ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE   })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface IssProtocolSpec {
    enum issProtocol {UDP,TCP,HTTP,MQTT,COAP,WS} ;
    issProtocol protocol() default issProtocol.TCP;
    String url() default "unknown";
    String configFile() default "IssProtocolConfig.txt";
}
