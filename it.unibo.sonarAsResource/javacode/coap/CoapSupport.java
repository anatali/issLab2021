package coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.ApplMessageType;

 
class MyHandler implements CoapHandler {
	public MyHandler ( ) {		 
	}
	@Override public void onLoad(CoapResponse response) {
		String content = response.getResponseText();
		System.out.println("MyHandler | value: " + content);
 	}					
	@Override public void onError() {
		System.err.println("MyHandler  |  FAILED (press enter to exit)");
	}
}
 

public class CoapSupport {
private CoapClient client;
private CoapObserveRelation relation = null;

	public CoapSupport( String address, String path) { //"coap://localhost:5683/" + path
		String url = address + "/" + path;
		client = new CoapClient( url );
		System.out.println("CoapSupport | STARTS url=" +  url + " client=" + client );
		client.setTimeout( 1000L );	
		String rep = readResource();
		System.out.println("CoapSupport | initial rep=" +  rep );
		//observeResource( new MyHandler() );
	}
 
	
	public String readResource(   ) {
		CoapResponse respGet = client.get( );
		System.out.println("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode());		
		return respGet.getResponseText();
	}

	public void removeObserve() {
		relation.proactiveCancel();	
	}
	public void  observeResource( CoapHandler handler  ) {
		relation = client.observe( handler );
	}

	public boolean updateResource( String msg ) {
		System.out.println("CoapSupport | updateResource " + msg);
		CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
 			if( resp != null ) System.out.println("CoapSupport | updateResource RESPONSE CODE: " + resp.getCode());	
 			else System.out.println("CoapSupport | updateResource FAILURE: " + resp );
 			return resp != null;
	}
	
	
	public boolean updateResourceWithValue( String data ) {
     	ApplMessage m = new ApplMessage(
    	        "sonarrobot", ApplMessageType.event.toString(),
            	"support", "none", "sonar("+data+")", "1" , null);
		return updateResource(m.toString());
	}
	
	public void test() {
		String v = readResource();
		System.out.println("CoapSupport | PRE v=" + v);
		updateResourceWithValue("55");
		try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
 		v = readResource();
 		System.out.println("CoapSupport | POST v=" + v);		
	}
	
	public static void main(String[] args) {
		//CoapSupport cs = new CoapSupport("coap://localhost:5683","robot/sonar");
		CoapSupport cs = new CoapSupport("coap://localhost:8028","ctxsonarresource/sonarresource");
		cs.test();		
	}
	
}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/