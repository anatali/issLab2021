package it.unibo.enablerCleanArch.supports.context;


import org.eclipse.californium.core.server.resources.Resource;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;


//TODO: ??? CoapApplServer coapServer = CoapApplServer.getTheServer();  //SINGLETION
public class CoapContextServer implements IContext{
//private String clientId;
//private String entry;
//private CoapSupport coapSupport;

	public CoapContextServer( ) { //String a, String b
//		this.clientId = clientId;
//		this.entry    = entry;
		ColorsOut.out("CoapContextServer CREATED");
	}
	@Override
	public void addComponent(String name, IApplMsgHandler h) { //h is a ApplResourceCoap
		ColorsOut.outerr( "CoapContextServer |  h " + h   );	 	
 	}
	@Override
	public void removeComponent(String name) {
		ColorsOut.outerr( "CoapContextServer |  removeComponent INAPPROPRIATE"   );	 			 	
	}
	@Override
	public void activate() {
		CoapApplServer.getTheServer();
		//coapSupport = new CoapSupport(clientId, entry );
 		ColorsOut.out("CoapContextServer activated");
  	}
	@Override
	public void deactivate() {		 
		//coapSupport.close();
		CoapApplServer.getTheServer().stop(); 
		CoapApplServer.getTheServer().destroy();		
		ColorsOut.out("CoapContextServer deactivate");
  	}
}
