package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.IApplication;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;

public class SonarUsageMainWithContextTcp extends SonarUsageAbstractMain implements IApplication {

	protected ISonar clientSonarProxy ;	
	private TcpContextServer ctxServer;
 	protected IObserver obsfortesting;
 	
 	@Override //IApplication
	public String getName() {
		return "SonarUsageMainWithContextTcp";
	}
	
	@Override
	public void setUp(String fName) {	//Called by inherited configure
		super.setUp(fName);
		RadarSystemConfig.withContext     = true;
		RadarSystemConfig.sonarObservable = true;	
//		RadarSystemConfig.ctxServerPort   = 8018;
	}
	
	@Override
	protected void configureTheServer() {
		ctxServer  = new TcpContextServer("TcpApplServer",RadarSystemConfig.ctxServerPort);
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		ctxServer.addComponent("sonar", sonarHandler);
  		ctxServer.activate();
	}
	
	//called by the inherited configure
	@Override
	protected  void createObservers() {
		if( RadarSystemConfig.sonarObservable ) {
			boolean oneShot         = false;
			obsfortesting           = new SonarObserverFortesting("obsfortesting", oneShot) ;
			((ISonarObservable) sonar).register( obsfortesting );
		} 		
	}

	@Override
	public void execute() {
 		clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", 
 				RadarSystemConfig.pcHostAddr, ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp );
 		clientSonarProxy.activate();
		for( int i=1; i<=5; i++) {
			int v = clientSonarProxy.getDistance().getVal();
			Colors.outappl("SonarUsageMainWithContextTcp | execute with proxyClient i=" + i + " getDistance=" + v, Colors.BLUE  );
			Utils.delay(RadarSystemConfig.sonarDelay+100);
		}	 
		if( RadarSystemConfig.sonarObservable) ((ISonarObservable) sonar).unregister(obsfortesting); 
		Utils.delay( 500  ); //The sonars works for a while, by putting data in the queue
		clientSonarProxy.deactivate();;			
	}
	
	public void terminate() {
		Colors.outappl("SonarUsageMainWithContextTcp | terminate",     Colors.ANSI_PURPLE );
 		Colors.outappl("SonarUsageMainWithContextTcp | terminate BYE", Colors.ANSI_PURPLE );	
		System.exit(0);
	}
	
	public void doJob(String fName) {
		setUp(fName);
		configure();
		execute();
		Utils.delay(500);
		terminate();		
	}
	public static void main( String[] args) throws Exception {
		SonarUsageMainWithContextTcp  sys = new SonarUsageMainWithContextTcp();	
		sys.doJob("RadarSystemConfig.json");	 //"RadarSystemConfig.json"
 	}

}
