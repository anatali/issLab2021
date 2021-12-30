package it.unibo.enablerCleanArch.main;
import org.eclipse.californium.core.CoapClient;
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
 
public class RadarSystemMainOnPcCoap implements IApplication{
private ISonar sonar    		   = null;
private ILed led        		   = null;
private IRadarDisplay radar 	   = null;
private boolean useProxyClient 	   = true;
private ISonar clientSonarProxy    = null;
private CoapClient clientObs 	   = null;
private CoapObserveRelation relObs = null;
private IObserver obsfortesting;

	@Override
	public String getName() {
		return "RadarSystemMainOnPcCoap";
	}
	@Override
	public void doJob(String configFileName) {
		setUp(configFileName);
		configure();
		execute();
	}
	

	public void setUp(String configFileName)  {			
		if( configFileName != null ) RadarSystemConfig.setTheConfiguration(configFileName);
		else {
			RadarSystemConfig.raspHostAddr = "192.168.1.183";
			RadarSystemConfig.DLIMIT       = 12;
			RadarSystemConfig.simulation   = true;
		}				
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
 		
		Controller.activate(clientLedProxy, clientSonarProxy, radar); //Activates the sonar

 	}
	
	protected void executeCoapUsingCoapSupport() {
 		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
		CoapSupport cps = new CoapSupport("localhost", sonarUri);
		
		cps.forward("activate");	//messaggi 'semplici'
		for( int i=1; i<=5; i++) {
			String v = cps.request("");  
			Colors.outappl("RadaSystemMainCoap | executeCoap with CoapSupport i=" + i + " getVal="+v, Colors.ANSI_PURPLE);
			Utils.delay(500);
		}	 
		cps.forward("deactivate");	//termina il Sonar
		cps.close();
	}
	
	

 
	public IRadarDisplay getRadarGui() {
		return radar;
	}
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcCoap().doJob(null);

	}


}
