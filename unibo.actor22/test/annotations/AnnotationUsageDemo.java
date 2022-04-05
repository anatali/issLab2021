package annotations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import unibo.actor22.annotations.AnnotUtil;
import unibo.actor22.annotations.ProtocolSpec;

@ProtocolSpec(
        protocol = ProtocolSpec.issProtocol.HTTP,
        url      = "http://localHost:8090/api/move"
)
public class AnnotationUsageDemo {
	
	public AnnotationUsageDemo() {
		AnnotUtil.readProtocolAnnotation( this );	
		
	}


	
	public static void main( String[] args) {
		new AnnotationUsageDemo();
	}
}
