package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;

public class CoapApplObserver implements CoapHandler{
	protected ApplMsgHandler applHandler;
	
	public CoapApplObserver(String hostAddr, String resourceUri, ApplMsgHandler applHandler) {
		this.applHandler = applHandler;
		//Creato da SonarAdapterCoap che specializza un EnablerAsServer
		//Usa CoapSupport per creare un client/observer verso una CoapSonareResource su port
		//Ogni item ricevuto da onload viene inviato a applHandler 
		
		//String uri = CoapApplServer.inputDeviceUri+"/"+resourceName ;
		System.out.println("CoapApplObserver | resourceUri= " + resourceUri );
		CoapSupport cps = new CoapSupport(hostAddr, resourceUri );
		cps.observeResource(this);
	}

	@Override
	public void onLoad(CoapResponse response) {
		//Colors.out("CoapApplObserver | response " +  response.getResponseText() );
		applHandler.elaborate(response.getResponseText(), null);
	}

	@Override
	public void onError() {
		System.out.println("CoapApplObserver | ERROR " );	
	}

}
