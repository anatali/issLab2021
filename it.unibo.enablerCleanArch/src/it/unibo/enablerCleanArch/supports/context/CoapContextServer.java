package it.unibo.enablerCleanArch.supports.context;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
 


//TODO: ??? CoapApplServer coapServer = CoapApplServer.getTheServer();  //SINGLETION
public class CoapContextServer implements IContext{
 
	public CoapContextServer( ) { //String a, String b
 		ColorsOut.out("CoapContextServer CREATED");
	}
	@Override
	public void activate() {
		CoapApplServer.getTheServer();
  		ColorsOut.out("CoapContextServer activated");
  	}
	@Override
	public void deactivate() {		 
 		CoapApplServer.getTheServer().stop(); 
		CoapApplServer.getTheServer().destroy();		
		ColorsOut.out("CoapContextServer deactivate");
  	}
	@Override
	public void addComponent(String name, IApplMsgHandler h) { //h is a ApplResourceCoap
		ColorsOut.outerr( "CoapContextServer |  add handler=" + h   );	 	
 	}
	@Override
	public void removeComponent(String name) {
		ColorsOut.outerr( "CoapContextServer |  removeComponent INAPPROPRIATE"   );	 			 	
	}
}
