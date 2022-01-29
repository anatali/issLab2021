package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.domain.IApplication;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.mqtt.ContextMqttMsgHandler;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;

public class SonarUsageMainWithContextMqtt extends SonarUsageAbstractMain implements IApplication {

	protected ISonar clientSonarProxy ;	
	private TcpContextServer ctxServer;
 	protected IObserver obsfortesting;
 	
 	@Override //IApplication
	public String getName() {
		return "SonarUsageMainWithContextMqtt";
	}
	
	@Override
	public void setUp(String fName) {	//Called by inherited configure
		super.setUp(fName);
		RadarSystemConfig.withContext     = true;
		RadarSystemConfig.sonarObservable = false;	
		RadarSystemConfig.protcolType     = ProtocolType.mqtt;
	}
	
	@Override
	protected void configureTheServer() {
//		ctxServer  = new TcpContextServer("TcpApplServer",RadarSystemConfig.ctxServerPort);
//		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
//		ctxServer.addComponent("sonar", sonarHandler);
//  		ctxServer.activate();
		IApplMsgHandler sonarHandler = new SonarApplHandler( "sonarH",sonar );
		IContextMsgHandler  ctxH     = new ContextMqttMsgHandler ( "ctxH" );
		ctxH.addComponent("sonar", sonarHandler);
// 		EnablerAsServer ctxServer = new EnablerAsServer("CtxServerMqtt","topicCtxMqtt" , ctxH );			
//		ctxServer.start(); 
		MqttSupport mqtt        = MqttSupport.getSupport();
		mqtt.connectMqtt("CtxServerMqtt", "topicCtxMqtt" , ctxH); 
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
 		clientSonarProxy      = new SonarProxyAsClient("clientSonarProxy", 
 				RadarSystemConfig.pcHostAddr, "topicCtxMqtt", ProtocolType.mqtt );
 		clientSonarProxy.activate();
		for( int i=1; i<=2; i++) {
			int v = clientSonarProxy.getDistance().getVal();
			Colors.outappl("SonarUsageMainWithContextTcp | execute with proxyClient i=" + i + " getDistance=" + v, Colors.GREEN  );
			//Utils.delay(RadarSystemConfig.sonarDelay+100);  //MQTT impone un ritmo lento ...
		}	 
		if( RadarSystemConfig.sonarObservable) ((ISonarObservable) sonar).unregister(obsfortesting); 
		//Utils.delay( 500  ); //The sonars works for a while, by putting data in the queue
		clientSonarProxy.deactivate();	
	}
	
	public void terminate() {
 		Colors.outappl("SonarUsageMainWithContextMqtt | terminate BYE", Colors.ANSI_PURPLE );	
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
		SonarUsageMainWithContextMqtt  sys = new SonarUsageMainWithContextMqtt();	
		sys.doJob("RadarSystemConfig.json");	  
 	}

}
