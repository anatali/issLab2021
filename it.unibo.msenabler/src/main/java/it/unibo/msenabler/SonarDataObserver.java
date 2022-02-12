package it.unibo.msenabler;

import java.io.IOException;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;

import it.unibo.enablerCleanArch.supports.ColorsOut;

public class SonarDataObserver implements CoapHandler{
private WebSocketHandler h;
	public SonarDataObserver(WebSocketHandler h){
		this.h=h;
	}
	@Override
	public void onLoad(CoapResponse response) {
		String vs = response.getResponseText();
		ColorsOut.out("SonarDataObserver: "+vs);
		try {
			h.sendToAll(vs);
		} catch (IOException e) {
			ColorsOut.outerr("SonarDataObserver | onLoad ERROR:" + e.getMessage());
 		}
		
	}

	@Override
	public void onError() {
		ColorsOut.outerr("SonarDataObserver error");		
	}
	

}
