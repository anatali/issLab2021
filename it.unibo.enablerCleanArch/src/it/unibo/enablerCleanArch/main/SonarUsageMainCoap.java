package it.unibo.enablerCleanArch.main;
 
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;

import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IDistance;
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

/*
 * Eredita il Sonar da 
 */

public class SonarUsageMainCoap  extends SonarUsageAbstractMain {  

private CoapClient clientObs = null;
private CoapObserveRelation relObs = null;

protected ISonar clientSonarProxy = null;
protected boolean useProxyClient = true;
protected IObserver obsfortesting;

	@Override
	public void setConfiguration() {
		super.setConfiguration();
		RadarSystemConfig.sonarObservable = true;		
	}
	
	@Override
	protected void configureTheServer() {
		CoapApplServer.getTheServer();  //singleton; call fatta anche da SonarResourceCoap
		new SonarResourceCoap("sonar", sonar); 
	}

	//Called by the inherited configure
	@Override
	protected void createObservers() {
		if( RadarSystemConfig.sonarObservable ) {
			boolean oneShot         = false;
			obsfortesting           = new SonarObserverFortesting("obsfortesting", oneShot) ; 
// 	 		sonar                   = DeviceFactory.createSonarObservable();
			((ISonarObservable) sonar).register( obsfortesting );
		} 		
		//Attivo un observer CoAP
		boolean withRadar = false;
		relObs = createAnObserverCoap(withRadar);	
	}
	
	@Override
	public void execute() {
		if( useProxyClient ) executeCoapUsingProxyClients();
		else executeCoapUsingCoapSupport();
	}

	protected void executeCoapUsingProxyClients() {
 		String host      = RadarSystemConfig.pcHostAddr;
 		String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
 		clientSonarProxy = new SonarProxyAsClient("clientSonarProxy", host, sonarUri, ProtocolType.coap );
		
  		//Attivo il Sonar
		clientSonarProxy.activate();	 
		
		for( int i=1; i<=5; i++) {
			IDistance v = clientSonarProxy.getDistance( );  
			Colors.outappl("SonarUsageMainCoap | execute with proxyClient i=" + i + " getDistance="+v.getVal(), Colors.ANSI_PURPLE);
			Utils.delay(500);
		}	 
 		//Tolgo l'observer CoAP
		relObs.proactiveCancel();
		Utils.delay(300);
		//Tolgo l'observer sul Sonar POJO
		if( RadarSystemConfig.sonarObservable) ((ISonarObservable) sonar).unregister(obsfortesting); 
		Utils.delay(300);
		//Fermo il Sonar
		clientSonarProxy.deactivate();
 	}
	
	protected void executeCoapUsingCoapSupport() {
 		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
		CoapSupport cps = new CoapSupport("localhost", sonarUri);
		
		cps.forward("activate");	//messaggi 'semplici'
		for( int i=1; i<=5; i++) {
			String v = cps.request("");  
			Colors.outappl("SonarUsageMainCoap | executeCoap with CoapSupport i=" + i + " getVal="+v, Colors.ANSI_PURPLE);
			Utils.delay(500);
		}	 
		cps.forward("deactivate");	//termina il Sonar
		cps.close();
	}

	/* 
	 * Attiva un ObserverNaive 
	 * oppure
	 * un CoapApplObserver che usa SonarMessageHandler per visualizzare su RadarGui
	 */
	protected CoapObserveRelation createAnObserverCoap( boolean withRadar) {
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
		CoapApplServer.stopTheServer();  
			//Fermo il clientObs
			if( clientObs != null ) {
				Colors.outappl("SonarUsageMainCoap | terminate clientObs="+clientObs, Colors.ANSI_PURPLE );
				clientObs.shutdown();	
			}
			//Chiudo le connessioni
			if( clientSonarProxy != null )  ((ProxyAsClient) clientSonarProxy).close();
 		Colors.outappl("SonarUsageMainCoap | terminate BYE", Colors.ANSI_PURPLE );	
		System.exit(0); //per via di CoapHandler  ...
	}
	
	public static void main( String[] args) throws Exception {
		SonarUsageMainCoap  sys = new SonarUsageMainCoap();	
		sys.configure();
		sys.execute();
		Utils.delay(500);
		sys.terminate();

	}


}

