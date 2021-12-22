package it.unibo.enablerCleanArch.main;

 
 
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapObserveRelation;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.EnablerSonarAsServer;
import it.unibo.enablerCleanArch.enablers.devices.SonarApplHandler;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplObserver;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.SonarMessageHandler;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.example.ObserverNaive;
  

public class SonarUsageMain  {

private EnablerAsServer sonarServer;
private ISonar client1, client2;
private CoapClient clientObs;
private boolean withContext = true;

private ApplMessage sonarActivate =
	new ApplMessage("msg( sonarcmd, dispatch,main,sonar, activate,0)");

	public void configure() {
		RadarSystemConfig.simulation    = true;
 		RadarSystemConfig.testing       = false;
 		RadarSystemConfig.sonarPort     = 8011;
		RadarSystemConfig.sonarDelay    = 100;
		RadarSystemConfig.protcolType   = ProtocolType.tcp;  //ProtocolType.coap
 		RadarSystemConfig.pcHostAddr    = "localhost";
 		RadarSystemConfig.ctxServerPort = 8048;
 		
 		configureTheServer();
 		configureTheSonarProxyClients();
 
 		Colors.outappl("SonarUsageMain | configure done", Colors.ANSI_PURPLE  );
	}
	
	protected void configureTheServer() {
		ISonar sonar = DeviceFactory.createSonar();
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			if( withContext ) configureWithContext(sonar); 
			else configureWithEnabler(sonar);
		}else if( RadarSystemConfig.protcolType == ProtocolType.coap){		
			new SonarResourceCoap("sonar", sonar);
		}
	}
	protected void configureWithEnabler(ISonar sonar) {
		sonarServer  = new EnablerSonarAsServer("sonarServer",RadarSystemConfig.sonarPort, 
				RadarSystemConfig.protcolType, new SonarApplHandler("sonarH",sonar), sonar );				
	}
	protected void configureWithContext(ISonar sonar) {
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		TcpContextServer contextServer  =
			    new TcpContextServer("TcpApplServer",RadarSystemConfig.ctxServerPort);
		contextServer.addComponent("sonar", sonarHandler);
		contextServer.activate();
	}
	protected void configureTheSonarProxyClients() {		 
		String host           = RadarSystemConfig.pcHostAddr;
		ProtocolType protocol = RadarSystemConfig.protcolType;
		String sonarUri       = CoapApplServer.inputDeviceUri+"/sonar";
		String entry    = protocol==ProtocolType.coap ? sonarUri : ""+RadarSystemConfig.sonarPort;
		client1         = new SonarProxyAsClient("client1", host, entry, protocol );
		client2         = new SonarProxyAsClient("client2", host, entry, protocol );	
	}
	 
	/*
	 * Attiva un ObserverNaive 
	 * oppure
	 * un CoapApplObserver che usa SonarMessageHandler per visualizzare su RadarGui
	 */
	protected CoapObserveRelation createAnObserver() {
		String sonarUri   = CoapApplServer.inputDeviceUri+"/sonar";
 		String sonarAddr  = "coap://localhost:5683/"+sonarUri;
		clientObs         =  new CoapClient( sonarAddr );
 		//CoapApplObserver obs  =  new CoapApplObserver( "localhost", sonarUri,new SonarMessageHandler( "sonarH" ) );	
 		ObserverNaive obs  =	new ObserverNaive("obsnaive");
		CoapObserveRelation relObs = clientObs.observe(obs);
		return relObs;
	}
	public void execute() {
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) { 
			if( withContext) ; 
			else sonarServer.activate(); 
		}else if( RadarSystemConfig.protcolType == ProtocolType.coap) { 
			executeCoap();
		}
	}
	protected void executeCoap() {
		//CASO CoAP -----------------
		
  		//Attivo il Sonar
		client1.activate();
		//Attivo un observer
		CoapObserveRelation relObs =  createAnObserver();	
 		/*
		for( int i=1; i<=5; i++) {
			int v = client1.getDistance().getVal();
			System.out.println("execute getVal="+v);
			Utils.delay(500);
		}	*/
		Utils.delay(1500);
		//Tolgo l'observer
		relObs.proactiveCancel();
		Utils.delay(1000);		
	}

	public void terminate() {
		Colors.outappl("SonarUsageMain | terminate", Colors.ANSI_PURPLE );
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			sonarServer.deactivate(); //stops also the sonar device
		}
		else if( RadarSystemConfig.protcolType == ProtocolType.coap) {
			//Fermo il clientObs
			clientObs.shutdown();	
			//Fermo il Sonar
			client2.deactivate();
			//Chiudo le connessioni
			((ProxyAsClient) client1).close();
			((ProxyAsClient) client2).close();
			CoapApplServer.getServer().destroy();
		}
	}
	
	public static void main( String[] args) throws Exception {
		SonarUsageMain  sys = new SonarUsageMain();	
		sys.configure();
		sys.execute();
		Utils.delay(500);
		sys.terminate();

	}
}

