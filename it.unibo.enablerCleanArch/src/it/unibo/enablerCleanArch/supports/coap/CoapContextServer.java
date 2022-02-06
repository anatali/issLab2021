package it.unibo.enablerCleanArch.supports.coap;


import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;

public class CoapContextServer implements IContext{
private String clientId;
private String entry;
private CoapSupport coapSupport;

	public CoapContextServer(String clientId, String entry) {
		this.clientId = clientId;
		this.entry    = entry;
		ColorsOut.out("CoapContextServer CREATED");
	}
	@Override
	public void addComponent(String name, IApplMsgHandler h) {
		ColorsOut.outerr( "CoapContextServer |  addComponent becomes new CoapResource"   );	 	
	}
	@Override
	public void removeComponent(String name) {
		ColorsOut.outerr( "CoapContextServer |  removeComponent INAPPROPRIATE"   );	 			 	
	}
	@Override
	public void activate() {
		coapSupport = new CoapSupport(clientId, entry );
 		ColorsOut.out("CoapContextServer activated");
  	}
	@Override
	public void deactivate() {		 
		coapSupport.close();
		CoapApplServer.getTheServer().stop(); 
		CoapApplServer.getTheServer().destroy();		
		ColorsOut.out("CoapContextServer deactivate");
  	}
}
