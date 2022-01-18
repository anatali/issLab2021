package it.unibo.enablerCleanArch.main;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.coap.LedAdapterCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarAdapterCoap;
import it.unibo.enablerCleanArch.supports.coap.SonarAdapterCoapObserver;
import it.unibo.enablerCleanArch.supports.coap.example.ObserverNaive;
import it.unibo.enablerCleanArchapplHandlers.NaiveMessageHandler;


/*
 * DA usare su PC con i dispositivi attivi su Raspberry (RadaSystemMainCoap)
 */

public class RadarSystemMainOnPcCoapBase implements IApplicationFacade{
private ISonar sonar    		   = null;
private ILed led        		   = null;
private IRadarDisplay radar 	   = null;
private boolean useProxyClient 	   = true;
private ISonar clientSonarProxy    = null;
private CoapClient clientObs 	   = null;
private CoapObserveRelation relObs = null;
private IObserver obsfortesting;
private CoapSupport coapSonarSup   = null;
private CoapSupport coapLedSup     = null;
private CoapObserveRelation rel1   = null;
 
	@Override
	public String getName() {
		return "RadarSystemMainOnPcCoap";
	}
	
	@Override
	public void setUp( String configFile )  {			
 			RadarSystemConfig.raspHostAddr = "192.168.1.31";
			RadarSystemConfig.DLIMIT       = 12;
			RadarSystemConfig.simulation   = false;
			RadarSystemConfig.withContext  = false;
			RadarSystemConfig.sonarDelay   = 250;
 			configureUsingProxy();
  	}
	public void entryMainAsApplInGui( ) {
		setUp(null);
   		CoapHandler obs = new ObserverNaive("obs"); //new ControllerAsCoapSonarObserver("obsController", led, radar) ;
		rel1            = coapSonarSup.observeResource( obs );
		//Controller.activate(led, sonar, radar);  
		led.turnOff();
		Colors.out("ledState=" +led.getState() );
		//sonar.activate();
		//Utils.delay(10000);
		//Terminate
		sonarDectivate() ;	//termina il Sonar
		coapSonarSup.close();
		if(rel1 != null ) rel1.proactiveCancel();
		System.exit(0);		
	}
	
	public void entryorMainOnPc( ) {
		RadarSystemConfig.raspHostAddr = "192.168.1.31";
		RadarSystemConfig.protcolType  = ProtocolType.coap;
		RadarSystemConfig.DLIMIT       = 12;
		RadarSystemConfig.simulation   = false;
		RadarSystemConfig.withContext  = false;
		RadarSystemConfig.sonarDelay   = 250;
		radar  = DeviceFactory.createRadarGui(); //since it is called on PC
		configureUsingCoapSupport();
		executeCoapUsingCoapSupport();
 		//Utils.delay(10000);
		//Terminate
		sonarDectivate() ;	//termina il Sonar
		if(rel1 != null ) rel1.proactiveCancel();
		System.exit(0);
	}
	
	/*
	 * Ogni proxyclient introduce un CoapSupport per la sua risorsa
	 */
	protected void configureUsingProxy() {
 		String host      = RadarSystemConfig.raspHostAddr;

 		String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
 		SonarProxyAsClient sonarProxy = new SonarProxyAsClient("sonarProxyCoap", host, sonarUri, ProtocolType.coap );
  		sonar            = sonarProxy;
 		
  		//Extract the support for the sonar, in order to set an observer 
  		coapSonarSup     = (CoapSupport) sonarProxy.getConn();
Colors.out("........................................ coapSonarSup=" + coapSonarSup);
 		String ledUri  = CoapApplServer.lightsDeviceUri+"/led";
 		led            = new LedProxyAsClient("ledProxyCoap", host, ledUri, ProtocolType.coap );		
	}

 
	/*
	 * NO: Meglio usare i supporti creati entro i proxy, anche se bisogna poi 'estrarre' il CoapSupport
	 * per costruire un coap observer
	 */	
	protected void configureUsingCoapSupport() {
 		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
 		coapSonarSup    = new CoapSupport("coapSonarSup",RadarSystemConfig.raspHostAddr, sonarUri);
 		//Colors.out("............. coapSonarSup=" + coapSonarSup);
 		String ledUri   = CoapApplServer.lightsDeviceUri+"/led";
 		led             = new LedProxyAsClient("ledProxyCoap", RadarSystemConfig.raspHostAddr, ledUri, ProtocolType.coap );
 		//CoapHandler obs = new ControllerAsCoapSonarObserver("obsController", led, radar) ;
		//rel1            = coapSonarSup.observeResource( obs );
 	}
	
	/*
	 * Da usare quando lo si lancia su PC
	 */
	protected void executeCoapUsingCoapSupport() {
		//led.turnOff();
		ledActivate(true);
		Colors.out("ledState=" +led.getState() );
		Utils.delay(1000);
		ledActivate(false);
		Colors.out("ledState=" +led.getState() );
		
		/*
		sonarActivate();	
		
		for( int i=1; i<=3; i++) {
			String d = coapSonarSup.request("getDistance");  
			Colors.outappl("RadarSystemMainOnPcCoapBase | executeCoap with CoapSupport i=" + i + " d="+d, Colors.ANSI_PURPLE);
			Utils.delay(500);
		}	 
		*/
 	}
	
 
  
	public IRadarDisplay getRadarGui() {
		return radar;
	}
	@Override
	public void ledActivate(boolean v) {
		//Colors.out("RadarSystemMainOnPcCoapBase ledActivate " + v );
 		if( v ) led.turnOn();else led.turnOff();
	}
	@Override
	public String ledState() {
 		return ""+led.getState();//coapLedSup.request("ledState"); //payload don't care
	}
	@Override
	public void sonarActivate() {
		Colors.out("RadarSystemMainOnPcCoapBase sonarActivate");
		coapSonarSup.forward("activate"); //messaggi 'semplici' 
		
	}
	@Override
	public boolean sonarIsactive() {
		String answer = coapSonarSup.request("isActive"); //messaggi 'semplici' 
		return answer.equals("true");
	}
	@Override
	public void sonarDectivate() {
		coapSonarSup.forward("deactivate");
		
	}
	@Override
	public String sonarDistance() {
		String answer = coapSonarSup.request("getDistance");
		return answer;
	}
	
	public static void main( String[] args) throws Exception {
		//new RadarSystemMainOnPcCoapBase().entryorMainOnPc();//   
		new RadarSystemMainOnPcCoapBase().entryMainAsApplInGui();

	}

 


}
