package it.unibo.enablerCleanArch.supports.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
 
public class CoapSupport {
private CoapClient client;
private CoapObserveRelation relation = null;

	public CoapSupport( String address, String path) { //"coap://localhost:5683/" + path
		String url = "coap://"+address + ":5683/"+ path;
		client = new CoapClient( url );
		System.out.println("CoapSupport | STARTS client url=" +  url ); //+ " client=" + client );
		client.setTimeout( 1000L );		 
	}
 	
	public String readResource(   ) throws  Exception {
		CoapResponse respGet = client.get( );
		System.out.println("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode());		
		return respGet.getResponseText();
	}

	public String readResource( String path  ) throws  Exception {
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

	public void updateResource( String msg ) throws  Exception {
		//System.out.println("	CoapSupport | updateResource msg: " + msg);
		CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
		System.out.println("	CoapSupport | updateResource resp: " + resp.getCode() );
	}
	
	

}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/