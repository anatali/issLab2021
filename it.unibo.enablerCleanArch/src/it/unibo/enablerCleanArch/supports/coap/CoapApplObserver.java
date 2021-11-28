package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;

public class CoapApplObserver implements CoapHandler{
	protected ApplMessageHandler applHandler;
	
	public CoapApplObserver(String hostAddr, String resourceUri, ApplMessageHandler applHandler) {
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
		System.out.println("CoapApplObserver | response " +  response.getResponseText() );
		applHandler.elaborate(response.getResponseText());
	}

	@Override
	public void onError() {
		System.out.println("CoapApplObserver | ERROR " );	
	}

}
