package it.unibo.enablerCleanArch.adapters;

import it.unibo.enablerCleanArch.domain.SonarModel;
import it.unibo.enablerCleanArch.supports.Colors;

public class SonarProxy extends SonarModel{

private SonarEnablerAsServer sonarEnablerAsServer;
private int lastSonarVal = 0;		//set by update called by the server
private String name      = "";
	
	public SonarProxy( String name,  String host, int port) {
		try {
			this.name = name;
			sonarEnablerAsServer = new SonarEnablerAsServer("SonarAdapterServer",port,this);			 
		} catch (Exception e) {
			Colors.outerr(name + " |  ERROR " + e.getMessage() );
		}
 	}
	
	@Override
	protected void sonarSetUp() { //called by activate
		try {
			sonarEnablerAsServer.sendCommandToClient("activate");
		} catch (Exception e) {		 
			Colors.outerr(name + " |  ERROR " + e.getMessage() );
		}
	}

	//Called by sonarEnablerAsServer 
	//Alla ricezione di ogni dato da parte del server modifica lastSonarVal
	public void update( int v ) {
		Colors.out(name + " |  update v=" +v );
		lastSonarVal = v;
	}
	@Override
	protected void sonarProduce() {  //called by activate of SonarModel
		curVal = lastSonarVal;
		//Colors.out(name + " | curVal="+ curVal );
		valueUpdated();  //
	}
	
	@Override
	public void deactivate() {
		Colors.out(name + " | deactivate");
		try {
			sonarEnablerAsServer.sendCommandToClient("deactivate");
		} catch (Exception e) {		 
			Colors.outerr(name + " |  ERROR " + e.getMessage() );
		}		 
	}


}
