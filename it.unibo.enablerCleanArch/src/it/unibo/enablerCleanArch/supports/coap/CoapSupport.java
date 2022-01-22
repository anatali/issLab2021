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
private String name = "CoapSprt";

	public CoapSupport( String name, String address, String path) {  
 		this.name = name;
 		setCoapClient(address,path);
 	}
	public CoapSupport( String address, String path) { //"coap://localhost:5683/" + path
 		setCoapClient(address,path);
	}
	
	protected void setCoapClient(String address, String path) {
		url            = "coap://"+address + ":5683/"+ path;
		client          = new CoapClient( url );
 		client.useExecutor(); //To be shutdown
		Colors.out(name + " | STARTS client url=" +  url,Colors.ANSI_YELLOW  ); //+ " client=" + client );
		client.setTimeout( 1000L );		 		
	}
 	
	public void removeObserve(CoapObserveRelation relation) {
		relation.proactiveCancel();	
 		Colors.out(name + " | removeObserve !!!!!!!!!!!!!!!" + relation ,Colors.ANSI_YELLOW  );
	}
	public CoapObserveRelation observeResource( CoapHandler handler  ) {
		CoapObserveRelation relation = client.observe( handler ); 
		Colors.out(name + " | added " + handler + " relation=" + relation + relation,Colors.ANSI_YELLOW  );
 		return relation;
	}


	
//From Interaction2021
	@Override
	public void forward(String msg)   {
		//Colors.out(name + " | forward " + url + " msg=" + msg,Colors.ANSI_YELLOW);
		if( client != null ) {
			CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN); //Blocking!
 			if( resp != null ) Colors.out(name + " | forward " + msg + " resp=" + resp.getCode(),Colors.ANSI_YELLOW  );
		    //else { Colors.outerr(name + " | forward - resp null "   ); }  //?????
		} 
	}

	@Override
	public String receiveMsg() throws Exception {
 		throw new Exception(name + " | receiveMsg not allowed");
	}
	
	@Override
	public String request(String query)   {
  		Colors.out(name + " | request query=" + query + " url="+url );
		String param = query.isEmpty() ? "" :  "?q="+query;
  		Colors.out(name + " | param=" + param );
		client.setURI(url+param);
		CoapResponse respGet = client.get(  );
		if( respGet != null ) {
	 		Colors.out(name + " | request=" + query 
	 				+" RESPONSE CODE: " + respGet.getCode() + " answer=" + respGet.getResponseText(),Colors.ANSI_YELLOW);
			return respGet.getResponseText();
		}else {
	 		Colors.out(name + " | request=" + query +" RESPONSE NULL ",Colors.ANSI_YELLOW);
			return "0";
		}
	}
	
	//https://phoenixnap.com/kb/install-java-raspberry-pi
	
	@Override
	public void reply(String reqid) throws Exception {
	} 

	@Override
	public void close()  {
		Colors.out(name + " | client shutdown=" + client);		
		client.shutdown();	
	}

}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/