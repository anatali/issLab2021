package it.unibo.radarGui21;

import java.io.IOException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.elements.exception.ConnectorException;

/*
class MyHandler implements CoapHandler {
	public MyHandler ( ) {		 
	}
	@Override public void onLoad(CoapResponse response) {
		String content = response.getResponseText();
		System.out.println("MyHandler | NOTIFICATION: " + content);
 	}					
	@Override public void onError() {
		System.err.println("MyHandler  |  FAILED (press enter to exit)");
	}
}
*/
public class CoapSupport {
private CoapClient client;
private CoapObserveRelation relation = null;

	public CoapSupport( String address, String path) { //"coap://localhost:5683/" + path
		String url = address + "/"+ path;
		client = new CoapClient( url );
		System.out.println("CoapSupport | STARTS url=" +  url ); //+ " client=" + client );
		client.setTimeout( 1000L );		 
	}
	//public CoapSupport( String address ) {  this(address, Resource.path); }
	
	public String readResource(   ) throws ConnectorException, IOException {
		CoapResponse respGet = client.get( );
		System.out.println("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode());		
		return respGet.getResponseText();
	}



	public String readResource( String path  ) throws ConnectorException, IOException {
		CoapResponse respGet = client.get( );
		System.out.println("CoapSupport | readResource with path RESPONSE CODE: " + respGet.getCode());
		return respGet.getResponseText();
	}

	public void removeObserve() {
		relation.proactiveCancel();	
	}
	public void  observeResource( CoapHandler handler  ) {
		relation = client.observe( handler );
	}

	public void updateResource( String msg ) throws ConnectorException, IOException {
		CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
//			if( resp != null ) System.out.println("CoapSupport | updateResource RESPONSE CODE: " + resp.getCode());	
//			else System.out.println("CoapSupport | updateResource FAILURE: "  );
	}
	
	

}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/