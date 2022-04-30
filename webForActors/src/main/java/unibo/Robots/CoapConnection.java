package it.unibo.enablerCleanArch.supports.coap;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
 
public class CoapConnection implements Interaction2021  {
private CoapClient client;
private String url;
private String name = "CoapSprt";

//	public CoapConnection( String name, String address, String path) {  
// 		this.name = name;
// 		setCoapClient(address,path);
// 	}
	public CoapConnection( String address, String path) { //"coap://localhost:5683/" + path
 		setCoapClient(address,path);
	}
	
	protected void setCoapClient(String address, String path) {
		url            = "coap://"+address + ":5683/"+ path;
		client          = new CoapClient( url );
 		client.useExecutor(); //To be shutdown
		ColorsOut.out(name + " | STARTS client url=" +  url,ColorsOut.ANSI_YELLOW  ); //+ " client=" + client );
		client.setTimeout( 1000L );		 		
	}
 	
	public void removeObserve(CoapObserveRelation relation) {
		relation.proactiveCancel();	
 		ColorsOut.out(name + " | removeObserve !!!!!!!!!!!!!!!" + relation ,ColorsOut.ANSI_YELLOW  );
	}
	public CoapObserveRelation observeResource( CoapHandler handler  ) {
		CoapObserveRelation relation = client.observe( handler ); 
		ColorsOut.out(name + " | added " + handler + " relation=" + relation + relation,ColorsOut.ANSI_YELLOW  );
 		return relation;
	}


	
//From Interaction2021
	@Override
	public void forward(String msg)   {
		//Colors.out(name + " | forward " + url + " msg=" + msg,Colors.ANSI_YELLOW);
		if( client != null ) {
			CoapResponse resp = client.put(msg, MediaTypeRegistry.TEXT_PLAIN); //Blocking!
 			if( resp != null ) ColorsOut.out(name + " | forward " + msg + " resp=" + resp.getCode(),ColorsOut.ANSI_YELLOW  );
		    //else { Colors.outerr(name + " | forward - resp null "   ); }  //?????
		} 
	}

	
	@Override
	public String request(String query)   {
  		ColorsOut.out(name + " | request query=" + query + " url="+url );
		String param = query.isEmpty() ? "" :  "?q="+query;
  		ColorsOut.out(name + " | param=" + param );
		client.setURI(url+param);
		CoapResponse respGet = client.get(  );
		if( respGet != null ) {
	 		ColorsOut.out(name + " | request=" + query 
	 				+" RESPONSE CODE: " + respGet.getCode() + " answer=" + respGet.getResponseText(),ColorsOut.ANSI_YELLOW);
			return respGet.getResponseText();
		}else {
	 		ColorsOut.out(name + " | request=" + query +" RESPONSE NULL ",ColorsOut.RED);
			return "0";
		}
	}
	
	//https://phoenixnap.com/kb/install-java-raspberry-pi
	
	@Override
	public void reply(String reqid) throws Exception {
		throw new Exception(name + " | reply not allowed");
	} 

	@Override
	public String receiveMsg() throws Exception {
 		throw new Exception(name + " | receiveMsg not allowed");
	}

	@Override
	public void close()  {
		ColorsOut.out(name + " | client shutdown=" + client);		
		client.shutdown();	
	}

}
/*
Log4j by default looks for a file called log4j.properties or log4j.xml on the classpath
*/