package it.unibo.enablerCleanArch.main;
 
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.devices.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplObserver;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.coap.SonarMessageHandler;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.example.ObserverNaive;
  

public class SonarUsageMainCoap  { //extends SonarUsageMain

private CoapClient clientObs;


//EREDITABILI
protected ISonar clientSonarProxy, client2;
protected ISonar   sonar;
protected boolean sonarWithObserver = false;
protected IObserver obsfortesting;
protected boolean withRadar = false;



	//@Override
	public void configure() {
		RadarSystemConfig.sonarDelay    = 100;
		//super.configure();		
		createTheSonar();
 		configureTheServer();
 		configureTheSonarProxyClients();
 		Colors.outappl("SonarUsageMainCoap | configure done", Colors.ANSI_PURPLE  );
	}

	protected void createTheSonar() {
		if( sonarWithObserver ) {
			boolean oneShot         = false;
			obsfortesting           = new SonarObserverFortesting("obsfortesting", oneShot) ;
	 		sonar                   = DeviceFactory.createSonarObservable();
			((ISonarObservable) sonar).register( obsfortesting );
		}else {
			sonar                   = DeviceFactory.createSonar();
		}		
	}

	
	protected void configureTheServer() {
		CoapApplServer.getServer();  //singleton
		new SonarResourceCoap("sonar", sonar); 
	}
	
 	protected void configureTheSonarProxyClients() {		 
 		String host      = RadarSystemConfig.pcHostAddr;
 		String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
 		clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", host, sonarUri, ProtocolType.coap );
		client2          = new SonarProxyAsClient("client2", host, sonarUri, ProtocolType.coap );	
	}
	 
	public void execute() {
		executeCoap();
	}

	protected void executeCoap() {
		//CASO CoAP -----------------
		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
		CoapSupport cps = new CoapSupport("localhost", sonarUri);
  		//Attivo il Sonar
		clientSonarProxy.activate();				//USO client1 per attivare
		//Attivo un observer
		CoapObserveRelation relObs =  createAnObserver(withRadar);	
 		 
		for( int i=1; i<=5; i++) {
			String v = cps.request(""); //getDistance		//USO CoapSupport per leggere
			Colors.outappl("SonarUsageMainCoap | executeCoap i=" + i + " getVal="+v, Colors.ANSI_PURPLE);
			Utils.delay(500);
		}	 
		//Utils.delay(300);
		//Tolgo l'observer CoAP
		relObs.proactiveCancel();
		Utils.delay(500);
		//Tolgo l'observer sul Sonar POJO
		if( sonarWithObserver) ((ISonarObservable) sonar).unregister(obsfortesting); 
		client2.deactivate();		//USO client2 per disattivare
		//Utils.delay(1000);		
	}

	/* 
	 * Attiva un ObserverNaive 
	 * oppure
	 * un CoapApplObserver che usa SonarMessageHandler per visualizzare su RadarGui
	 */
	protected CoapObserveRelation createAnObserver( boolean withRadar) {
		String sonarUri   = CoapApplServer.inputDeviceUri+"/sonar";
 		String sonarAddr  = "coap://localhost:5683/"+sonarUri;
		clientObs         =  new CoapClient( sonarAddr );
		CoapHandler obs;
		if( withRadar ) {
 		     obs=new CoapApplObserver( "localhost", sonarUri,new SonarMessageHandler( "sonarH" ) );	
		}else {
			 obs=new ObserverNaive("obsnaive");}
		CoapObserveRelation relObs = clientObs.observe(obs);
		return relObs;
	}

	public void terminate() {
		Colors.outappl("SonarUsageMainCoap | terminate", Colors.ANSI_PURPLE );
			//Fermo il clientObs
			if( clientObs != null ) clientObs.shutdown();	
			//Fermo il Sonar
			if( client2 != null )  client2.deactivate();
			//Chiudo le connessioni
			if( clientSonarProxy != null )  ((ProxyAsClient) clientSonarProxy).close();
			if( client2 != null ) ((ProxyAsClient) client2).close();
			CoapApplServer.getServer().destroy();
		Colors.outappl("SonarUsageMainCoap | terminate BYE", Colors.ANSI_PURPLE );	
	}
	
	public static void main( String[] args) throws Exception {
		SonarUsageMainCoap  sys = new SonarUsageMainCoap();	
		sys.configure();
		sys.execute();
		Utils.delay(500);
		sys.terminate();

	}
}

