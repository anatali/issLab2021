package it.unibo.enablerCleanArch.supports.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
public class CoapSupport implements Interaction2021  {
private CoapClient client;
private CoapObserveRelation relation = null;
private String url;

	public CoapSupport( String address, String path) { //"coap://localhost:5683/" + path
		url = "coap://"+address + ":5683/"+ path;
		client = new CoapClient( url );
		Colors.out("CoapSupport | STARTS client url=" +  url,Colors.ANSI_YELLOW  ); //+ " client=" + client );
		client.setTimeout( 1000L );		 
	}
 	
	public String readResource(   ) throws  Exception {
		CoapResponse respGet = client.get( );
		Colors.out("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode(),Colors.ANSI_YELLOW );		
		return respGet.getResponseText();
	}

	public String readResource( String query  ) throws  Exception {
		CoapClient myclient = new CoapClient( url+"?q="+query );
		CoapResponse respGet = myclient.get( );
		Colors.out("CoapSupport | readResource query=" + query 
				+" RESPONSE CODE: " + respGet.getCode() + " answer=" + respGet.getResponseText(),Colors.ANSI_YELLOW);
		return respGet.getResponseText();
	}

	public void removeObserve() {
		relation.proactiveCancel();	
	}
	public void  observeResource( CoapHandler handler  ) {
		relation = client.observe( handler );
	}

	public void updateResource( String msg ) throws  Exception {
		Colors.out("CoapSupport | updateResource " + url + " msg=" + msg,Colors.ANSI_YELLOW);
		CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
		Colors.out("CoapSupport | updateResource " + msg + " resp=" + resp.getCode(),Colors.ANSI_YELLOW  );
	}

	
//From Interaction2021
	@Override
	public void forward(String msg) throws Exception {
		updateResource(msg);		
	}

	@Override
	public String receiveMsg() throws Exception {
 		throw new Exception("CoapSupport | receiveMsg alone not allowed");
	}
	
	@Override
	public String request(String query) throws Exception {
 		 return readResource(   );
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/