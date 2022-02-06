package it.unibo.enablerCleanArch.main.coap;
 
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;

import it.unibo.enablerCleanArch.domain.Controller;
import it.unibo.enablerCleanArch.domain.DeviceFactory;
import it.unibo.enablerCleanArch.domain.IApplication;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.domain.SonarObserverFortesting;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplObserver;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.coap.LedResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.WebCamRaspResourceCoap;
import it.unibo.enablerCleanArch.supports.coap.example.ObserverNaive;
import it.unibo.enablerCleanArchapplHandlers.SonarDistanceHandler;

/*
 * Applicazione che va in coppia con RadarSystemMainOnPcCoap
 */
public class RadarSystemDevicesOnRaspCoap  implements IApplication{  
private ISonar   sonar;
private ILed     led;

private CoapClient clientObs = null;
private CoapObserveRelation relObs = null;

protected ISonar clientSonarProxy = null;
//protected boolean useProxyClient  = true;
protected IObserver obsfortesting;

	@Override //IApplication
	public String getName() {
		return "RadaSystemMainCoap";
	}
 
	public void setUp(String fName) {
		if( fName != null ) RadarSystemConfig.setTheConfiguration(fName);
		//else {
			RadarSystemConfig.pcHostAddr         = "localhost";
			RadarSystemConfig.sonarDelay         = 100;		
			//RadarSystemConfig.sonarObservable    = false;	
			RadarSystemConfig.ControllerRemote   = false;
			RadarSystemConfig.DLIMIT             = 55;
			RadarSystemConfig.withContext        = true;
		//}		
	}
 	
	public void configure() {
		//CoapApplServer.getTheServer();  //singleton; call fatta anche da SonarResourceCoap
		sonar = DeviceFactory.createSonar(RadarSystemConfig.sonarObservable);	
 		new SonarResourceCoap("sonar", sonar); 
 		
 		led = DeviceFactory.createLed();
 		new LedResourceCoap("led", led); 
 		
 		Utils.delay(1000); //Give time for the LedGui setup
 		
 		
 		//WebCam
 		if( RadarSystemConfig.webCam ) 
 			new WebCamRaspResourceCoap("webcam");
 		
 		ColorsOut.outappl("RadaSystemMainCoap | configure done", ColorsOut.ANSI_PURPLE  );
		//createObservers();
	}

	//Called by the inherited configure 
	protected void createObservers() {
		if( RadarSystemConfig.sonarObservable ) {
			boolean oneShot         = false;
			obsfortesting           = new SonarObserverFortesting("obsfortesting", oneShot) ; 
			((ISonarObservable) sonar).register( obsfortesting );
		} 		
		//Attivo un observer CoAP
		boolean withRadar = false;
		relObs = createAnObserverCoap(withRadar);	
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
 		     obs=new CoapApplObserver( "localhost", sonarUri,new SonarDistanceHandler( "sonarH" ) );	
		}else {
			 obs=new ObserverNaive("obsnaive");}
		CoapObserveRelation relObs = clientObs.observe(obs);
		return relObs;
	}


	@Override
	public void doJob(String fName) {
		setUp(fName);
		configure();
	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystemDevicesOnRaspCoap().doJob("RadarSystemConfig.json");	

	}




}

