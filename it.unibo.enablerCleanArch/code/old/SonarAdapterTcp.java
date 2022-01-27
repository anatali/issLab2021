package it.unibo.enablerCleanArch.adapters;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.main.ProtocolType;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;

public class SonarAdapterTcp extends ApplMessageHandler implements ISonar{

private SonarEnablerAsServer sonarEnablerAsServer;
private int lastSonarVal = 0;		 
private boolean stopped  = true;	//mirror value
private boolean produced = false;

	public SonarAdapterTcp( String name,  String host, int port) {
		super( name );
		try {
			this.name = name;
			sonarEnablerAsServer = new SonarEnablerAsServer("SonarEnablerAsServer" );	
			sonarEnablerAsServer.setServerSupport(port, this, ProtocolType.tcp);
		} catch (Exception e) {
			Colors.outerr(name + " |  ERROR " + e.getMessage() );
		}
 	}
	//From ApplMessageHandler
	@Override
	public void elaborate(String message) {  //receive a distance value after the request in getVal
		Colors.out(name + " |  elaborate " + message );
		lastSonarVal = Integer.parseInt( message );
		valueUpdated( );
	}

	
	//From ISonar
	@Override
	public void deactivate() {
		Colors.out(name + " | deactivate");
		try {
			sonarEnablerAsServer.sendCommandToClient("deactivate");
		} catch (Exception e) {		 
			Colors.outerr(name + " |  ERROR " + e.getMessage() );
		}		 
	}

	@Override
	public void activate() {
		sonarEnablerAsServer.sendCommandToClient("activate");
		stopped = false;
	}

 

	@Override
	public  int getVal() {
		sonarEnablerAsServer.sendCommandToClient("getVal");
		waitForUpdatedVal();
		return lastSonarVal;
	}

	private synchronized void waitForUpdatedVal() {
		try {
 			while( ! produced ) wait();
 			produced = false;
 		} catch (InterruptedException e) {
 			System.out.println("Sonar | waitForUpdatedVal ERROR " + e.getMessage() );
		}		
	}	

	@Override
	public boolean isActive() { 
		return stopped;
	}

	protected synchronized void valueUpdated( ){
		produced = true;
		this.notify();
	}

}
