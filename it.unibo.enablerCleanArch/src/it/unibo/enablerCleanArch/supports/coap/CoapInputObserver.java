package it.unibo.enablerCleanArch.supports.coap;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import it.unibo.enablerCleanArch.supports.ApplMessageHandler;

public class CoapInputObserver implements CoapHandler{
	protected String name;
	protected int port;
	protected ApplMessageHandler applHandler;
	
	public CoapInputObserver(String name, int port, ApplMessageHandler applHandler) {
		this.name        = name;
		this.port        = port;
		this.applHandler = applHandler;
		//Creato da SonarAdapterServer che specializza un EnablerAsServer
		//Usa CoapSupport per creare un client/observer verso una CoapSonareResource su port
		//Ogni item ricevuto da onload viene inviato a applHandler 
	}

	@Override
	public void onLoad(CoapResponse response) {
		// Invoca applHandler
		
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}

}
