package it.unibo.enablerCleanArch.main;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.coap.LedAdapterCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarAdapterCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarAdapterCoapObserver;
import it.unibo.enablerCleanArch.supports.coap.example.ObserverNaive;
 
public class RadarSystemMainOnPcCoap implements IApplicationFacade{
//private ISonar sonar    		   = null;
//private ILed led        		   = null;
private IRadarDisplay radar 	   = null;
private boolean useProxyClient 	   = false;
private ISonar clientSonarProxy    = null;
private CoapClient clientObs 	   = null;
private CoapObserveRelation relObs = null;
private IObserver obsfortesting;

	@Override
	public String getName() {
		return "RadarSystemMainOnPcCoap";
	}
	
	
	public void doJob(String configFileName) {
		setUp(configFileName);
		execute();		
	}
	
	@Override
	public void setUp(String configFileName)  {			
		if( configFileName != null ) RadarSystemConfig.setTheConfiguration(configFileName);
		else {
			RadarSystemConfig.raspHostAddr = "192.168.1.26";
			RadarSystemConfig.DLIMIT       = 12;
			RadarSystemConfig.simulation   = false;
			RadarSystemConfig.withContext  = false;
			RadarSystemConfig.sonarDelay   = 250;
		}				
		configure();
 	}
	
	public void configure()  {		
		radar  = DeviceFactory.createRadarGui();
	} 
	
	public void execute() {
		if( useProxyClient ) executeCoapUsingProxyClients();
		else executeCoapUsingCoapSupport();
	}

	protected void executeCoapUsingProxyClients() {
 		String host      = RadarSystemConfig.raspHostAddr;

 		String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
 		ISonar clientSonarProxy = new SonarProxyAsClient("sonarProxyCoap", host, sonarUri, ProtocolType.coap );

 		String ledUri  = CoapApplServer.lightsDeviceUri+"/led";
 		ILed clientLedProxy = new LedProxyAsClient("ledProxyCoap", host, ledUri, ProtocolType.coap );
 		
 		
 		clientSonarProxy.activate();
 		/*
 		for( int i=1; i<=3; i++) {
 			Utils.delay(1000);
 			IDistance d = clientSonarProxy.getDistance(); //bloccante
 			Colors.outappl("RadarSystemMainOnPcCoap | d=" + d, Colors.BLUE  );
 		}
		
 		for( int i=1; i<=3; i++) {
	 		clientLedProxy.turnOn();
	 		Utils.delay(1000);
	 		clientLedProxy.turnOff();
	 		Utils.delay(1000);
		}
 		
 		*/
		Controller.activate(clientLedProxy, clientSonarProxy, radar);  
		
 	}
	
	protected void executeCoapUsingCoapSupport() {
 		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
		CoapSupport cps = new CoapSupport(RadarSystemConfig.raspHostAddr, sonarUri);
 		String ledUri   = CoapApplServer.lightsDeviceUri+"/led";
 		ILed led        = new LedProxyAsClient("ledProxyCoap", RadarSystemConfig.raspHostAddr, ledUri, ProtocolType.coap );
 		CoapHandler obs = new ControllerAsCoapObserver("obsController", led) ;
		CoapObserveRelation rel1 = cps.observeResource( obs );
		led.turnOff();
		cps.forward("activate");	//messaggi 'semplici'
		/*
		for( int i=1; i<=5; i++) {
			String v = cps.request("");  
			Colors.outappl("RadaSystemMainCoap | executeCoap with CoapSupport i=" + i + " getVal="+v, Colors.ANSI_PURPLE);
			Utils.delay(500);
		}	 */
		Utils.delay(10000);
		cps.forward("deactivate");	//termina il Sonar
		cps.close();
		rel1.proactiveCancel();
		System.exit(0);
	}
  
	public IRadarDisplay getRadarGui() {
		return radar;
	}
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcCoap().doJob(null);

	}
	@Override
	public void ledActivate(boolean v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String ledState() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void sonarActivate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean sonarIsactive() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void sonarDectivate() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public String sonarDistance() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ISonarObservable getSonar() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void doLedBlink() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void stopLedBlink() {
		// TODO Auto-generated method stub
		
	}


}
