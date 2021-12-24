package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.devices.EnablerSonarAsServer;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarUsageMainWithEnablerTcp extends SonarUsageAbstractMain{
	protected EnablerAsServer sonarServer;
 	protected IObserver obsfortesting;
	
	@Override
	public void setConfiguration() {	//Called by inherited configure
		super.setConfiguration();
		RadarSystemConfig.withContext     = false;
		RadarSystemConfig.sonarObservable = true;	
		RadarSystemConfig.sonarPort       = 8011;
	}
	
	@Override
	protected void configureTheServer() {
		sonarServer  = new EnablerSonarAsServer("sonarServer",RadarSystemConfig.sonarPort, 
				RadarSystemConfig.protcolType, new SonarApplHandler("sonarH",sonar), sonar );						
	}

	//called by the inherited configure
	@Override
	protected  void createObservers() {
		if( RadarSystemConfig.sonarObservable ) {
			boolean oneShot         = false;
			obsfortesting           = new SonarObserverFortesting("obsfortesting", oneShot) ; 
// 	 		sonar                   = DeviceFactory.createSonarObservable();
			((ISonarObservable) sonar).register( obsfortesting );
		} 		
	}

	
	@Override
	public void execute() {
		ISonar clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", 
				RadarSystemConfig.pcHostAddr, ""+RadarSystemConfig.sonarPort, ProtocolType.tcp );
		sonarServer.start(); 	
 		clientSonarProxy.activate(); //Activate the sonar
			 
 		for( int i=1; i<=5; i++) {
 			int v = clientSonarProxy.getDistance().getVal();
 			Colors.outappl("SonarUsageMainWithEnablerTcp | execute  with proxyClient i=" + i + " v="+v, Colors.BLUE);
 			Utils.delay(RadarSystemConfig.sonarDelay  );    
 		}	  
 		if( RadarSystemConfig.sonarObservable ) ((ISonarObservable) sonar).unregister(obsfortesting); 
 		Utils.delay( 500  ); //The sonars works for a while, by putting data in the queue
 		clientSonarProxy.deactivate();
	}

	
	
	public void terminate() {
		Colors.outappl("SonarUsageMainCoap | terminate",     Colors.ANSI_PURPLE );
 		Colors.outappl("SonarUsageMainCoap | terminate BYE", Colors.ANSI_PURPLE );	
		System.exit(0);
	}
	
	public static void main( String[] args) throws Exception {
		SonarUsageMainWithEnablerTcp  sys = new SonarUsageMainWithEnablerTcp();	
		sys.configure();
		sys.execute();
		Utils.delay(500);
		sys.terminate();
	}

}
