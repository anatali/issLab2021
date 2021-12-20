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
private String url;

	public CoapSupport( String address, String path) { //"coap://localhost:5683/" + path
		 url            = "coap://"+address + ":5683/"+ path;
		client          = new CoapClient( url );
//		clientforQuery  = new CoapClient( url+"?q="  );
		Colors.out("CoapSupport | STARTS client url=" +  url,Colors.ANSI_YELLOW  ); //+ " client=" + client );
		client.setTimeout( 1000L );		 
	}
 	
//	public String readResource(   ) throws  Exception {
//		client.setURI(url);
//		CoapResponse respGet = client.get( );
//		Colors.out("CoapSupport | readResource RESPONSE CODE: " + respGet.getCode(),Colors.ANSI_YELLOW );		
//		return respGet.getResponseText();
//	}

//	public String readResource( String query  ) throws  Exception {
//		//CoapClient myclient  = new CoapClient( url+"?q="+query );
//		client.setURI(url+"?q="+query);
//		CoapResponse respGet = client.get(  );
//		Colors.out("CoapSupport | readResource query=" + query 
//				+" RESPONSE CODE: " + respGet.getCode() + " answer=" + respGet.getResponseText(),Colors.ANSI_YELLOW);
//		return respGet.getResponseText();
// 	}

	public void removeObserve(CoapObserveRelation relation) {
		relation.proactiveCancel();	
 		Colors.out("CoapSupport | removeObserve !!!!!!!!!!!!!!!" + relation ,Colors.ANSI_YELLOW  );
	}
	public CoapObserveRelation  observeResource( CoapHandler handler  ) {
		CoapObserveRelation relation = client.observe( handler ); 
		Colors.out("CoapSupport | added observer relation=" + relation + relation,Colors.ANSI_YELLOW  );
 		return relation;
	}


	
//From Interaction2021
//	protected void updateResource( String msg ) throws  Exception {
//		Colors.out("CoapSupport | updateResource " + url + " msg=" + msg,Colors.ANSI_YELLOW);
//		CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
//		Colors.out("CoapSupport | updateResource " + msg + " resp=" + resp.getCode(),Colors.ANSI_YELLOW  );
//	}
	@Override
	public void forward(String msg)   {
		//updateResource(msg);		
		//Colors.out("CoapSupport | forward " + url + " msg=" + msg,Colors.ANSI_YELLOW);
		CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN);
		Colors.out("CoapSupport | forward " + msg + " resp=" + resp.getCode(),Colors.ANSI_YELLOW  );
	}

	@Override
	public String receiveMsg() throws Exception {
 		throw new Exception("CoapSupport | receiveMsg not allowed");
	}
	
	@Override
	public String request(String query)   {
 		 //return readResource(  query  );
		String param = query.isEmpty() ? "" : "?q="+query;
//		clientforQuery.setURI(url+param);
		client.setURI(url+param);
		CoapResponse respGet = client.get(  );
//		Colors.out("CoapSupport | request=" + query 
//				+" RESPONSE CODE: " + respGet.getCode() + " answer=" + respGet.getResponseText(),Colors.ANSI_YELLOW);
		return respGet.getResponseText();
	}
	@Override
	public void close()  {
		client.delete();	
	}

	

}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/