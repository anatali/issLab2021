package it.unibo.enablerCleanArch.main;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
 
public class RadarSystemMainOnPcCoap implements IApplicationFacade{
private ISonar sonar    		   = null;
private ILed led        		   = null;
private IRadarDisplay radar 	   = null;
private CoapSupport coapSonarSup   = null;
private CoapObserveRelation relObs = null;
 
	@Override
	public String getName() {
		return "RadarSystemMainOnPcCoap";
	}
	
	
	public void doJob(String configFileName) {
		setUp(configFileName);
		//executeWithObserverAsController();		
		executeWithController();
	}
	
	@Override
	public void setUp(String configFileName)  {			
		if( configFileName != null ) RadarSystemConfig.setTheConfiguration(configFileName);
		else {
			RadarSystemConfig.raspHostAddr = "192.168.1.9";
			RadarSystemConfig.DLIMIT       = 10;
			RadarSystemConfig.simulation   = false;
			RadarSystemConfig.withContext  = false;
			RadarSystemConfig.sonarDelay   = 250;
			RadarSystemConfig.sonarObservable = false;
		}		
		
  	}
	
  	
	protected void configure() {
 		String host      = RadarSystemConfig.raspHostAddr;

 		String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
 		SonarProxyAsClient sonarProxy = new SonarProxyAsClient("sonarProxyCoap", host, sonarUri, ProtocolType.coap );
  		sonar            = sonarProxy;
 		
  		coapSonarSup     = (CoapSupport) sonarProxy.getConn();

 		String ledUri  = CoapApplServer.lightsDeviceUri+"/led";
 		led            = new LedProxyAsClient("ledProxyCoap", host, ledUri, ProtocolType.coap );
		
	}

	protected void executeWithController() {
		configure();
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
		radar  = DeviceFactory.createRadarGui();
 		Controller.activate(led, sonar, radar);  
		Utils.delay(10000);
		terminate();
 	}
	
	protected void executeWithObserverAsController() {
		configure();		
 		radar           = DeviceFactory.createRadarGui();
  		CoapHandler obs = new ControllerAsCoapSonarObserver("obsController", led, radar) ;
		relObs          = coapSonarSup.observeResource( obs );
 		sonar.activate();  
		Utils.delay(10000);
		terminate();
 	}
	
 	
 	
	public void terminate () {
		sonarDectivate() ;	//termina il Sonar
  		//relObs.proactiveCancel();
		System.exit(0);		
	}
  
	public IRadarDisplay getRadarGui() {
		return radar;
	}

	@Override
	public void ledActivate(boolean v) {
		if( v ) led.turnOn(); else led.turnOff();
		
	}
	@Override
	public String ledState() {
 		return ""+led.getState();
	}
	@Override
	public void sonarActivate() {
		sonar.activate();
		
	}
	@Override
	public boolean sonarIsactive() {
		boolean answer = sonar.isActive();
		return answer ;
	}
	@Override
	public void sonarDectivate() {
		sonar.deactivate();
		
	}
	@Override
	public String sonarDistance() {
		String answer = sonar.getDistance().toString();
		return answer;
	}
	
	
	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcCoap().doJob(null);

	}


}
