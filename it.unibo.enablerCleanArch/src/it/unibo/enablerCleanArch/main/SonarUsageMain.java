package it.unibo.enablerCleanArch.main;

 
 
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapObserveRelation;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
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
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.coap.SonarMessageHandler;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.example.ObserverNaive;
  

public class SonarUsageMain  {

private EnablerAsServer sonarServer;
private ISonar client1, client2;
private CoapClient clientObs;
private boolean withContext       = false;

private ApplMessage sonarActivate = new ApplMessage("msg( sonarcmd, dispatch,main,sonar, activate,0)");
private ApplMessage sonarStop     = new ApplMessage("msg( sonarcmd, dispatch, main, sonar, deactivate, 4 )");
private ApplMessage getDistance   = new ApplMessage("msg( sonarcmd, request,  main, sonar, getDistance,   5 )");


private ProxyAsClient sonarCaller;
private TcpContextServer contextServer;

	public void configure() {
		RadarSystemConfig.simulation    = true;
 		RadarSystemConfig.testing       = false;
 		RadarSystemConfig.sonarPort     = 8011;
		RadarSystemConfig.sonarDelay    = 100;
		RadarSystemConfig.protcolType   = ProtocolType.coap;  //ProtocolType.coap
 		RadarSystemConfig.pcHostAddr    = "localhost";
 		RadarSystemConfig.ctxServerPort = 8048;
 		
 		configureTheServer();
 		configureTheSonarProxyClients();
 
 		Colors.outappl("SonarUsageMain | configure done", Colors.ANSI_PURPLE  );
	}
	
	protected void configureTheServer() {
		//ISonar sonar = DeviceFactory.createSonar();
		boolean oneShot         = false;
		ISonarObservable  sonar = DeviceFactory.createSonarObservable();
		IObserver obs1          = new SonarObserverFortesting("obs1",sonar,oneShot) ;
		sonar.register( obs1 );
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
		String entry          = sonarUri; //already set for ProtocolType.coap
//		if( protocol==ProtocolType.tcp && withContext  ) entry = ""+RadarSystemConfig.ctxServerPort;
//		else 
		if( protocol==ProtocolType.tcp && ! withContext  ) {
			entry = ""+RadarSystemConfig.sonarPort;
			//String entry    = protocol==ProtocolType.coap ? sonarUri : ""+RadarSystemConfig.sonarPort;
		}
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
			executeTcp() ;
		}else if( RadarSystemConfig.protcolType == ProtocolType.coap) { 
			executeCoap();
		}
	}
	protected void executeTcp() {
		if( withContext) {
			sonarCaller = 
					new ProxyAsClient("proxyCtx","localhost", ""+RadarSystemConfig.ctxServerPort, ProtocolType.tcp);
			//contextServer.activate();
			sonarCaller.sendCommandOnConnection(sonarActivate.toString());
			for( int i=1; i<=5; i++) {
				String v = sonarCaller.sendRequestOnConnection( getDistance.toString() );
				System.out.println("executeTcp getDistance="+v);
				Utils.delay(200);
			}	 
			sonarCaller.sendCommandOnConnection(sonarStop.toString());
	
		}
		else { //Tcp non context
			sonarServer.activate(); 	
			client1.activate(); //Activate the sonar
			for( int i=1; i<=5; i++) {
				int v = client1.getDistance().getVal();
				System.out.println("executeTcp getDistance="+v);
				Utils.delay(300);
			}	 
			client1.deactivate();
		}
 		 
	}
	protected void executeCoap() {
		//CASO CoAP -----------------
		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
		CoapSupport cps = new CoapSupport("localhost", sonarUri);
  		//Attivo il Sonar
		client1.activate();				//USO client1 per attivare
		//Attivo un observer
		CoapObserveRelation relObs =  createAnObserver();	
 		 
		for( int i=1; i<=5; i++) {
			String v = cps.request(""); //getDistance		//USO CoapSupport per leggere
			Colors.outappl("execute getVal="+v, Colors.BLUE);
			Utils.delay(500);
		}	 
		Utils.delay(300);
		//Tolgo l'observer
		relObs.proactiveCancel();
		Utils.delay(500);
		client2.deactivate();		//USO client2 per disattivare
		Utils.delay(1000);		
	}

	public void terminate() {
		Colors.outappl("SonarUsageMain | terminate", Colors.ANSI_PURPLE );
		if( RadarSystemConfig.protcolType == ProtocolType.tcp) {
			sonarServer.deactivate(); //stops also the sonar device
		}
		else if( RadarSystemConfig.protcolType == ProtocolType.coap) {
			//Fermo il clientObs
			if( clientObs != null ) clientObs.shutdown();	
			//Fermo il Sonar
			if( client2 != null )  client2.deactivate();
			//Chiudo le connessioni
			if( client1 != null )  ((ProxyAsClient) client1).close();
			if( client2 != null ) ((ProxyAsClient) client2).close();
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

