package it.unibo.enablerCleanArch.main.coap;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.context.Context2021;
 
public class RadarSystemMainOnPcCoap implements IApplicationFacade{
	
private ISonar sonar    		   = null;
private ILed led        		   = null;
private IRadarDisplay radar 	   = null;
private CoapSupport coapSonarSup   = null;
private CoapObserveRelation relObs = null;
private final int ampl             = 3;
private boolean ledblinking        = false;
 
	@Override
	public String getName() {
		return "RadarSystemMainOnPcCoap";
	}
	
	
	public void doJob(String configFileName) {
		setUp(configFileName);
		executeLed();
		//executeWithObserverAsController();		

	}
	
	@Override
	public void setUp(String configFileName)  {			
		if( configFileName != null ) RadarSystemConfig.setTheConfiguration(configFileName);
		//else {
			RadarSystemConfig.raspHostAddr = "192.168.1.9";
			RadarSystemConfig.DLIMIT       = 10*ampl;
			RadarSystemConfig.simulation   = false;
			RadarSystemConfig.withContext  = false;
			RadarSystemConfig.sonarDelay   = 250;
			RadarSystemConfig.sonarObservable = false;
			RadarSystemConfig.protcolType  = ProtocolType.coap;
		//}		
		
  	}
	
  	
	protected void configure() {
		IContext ctx     = Context2021.create("dont'care","dont'care");
		
 		String host      = RadarSystemConfig.raspHostAddr;
 		String sonarUri  = CoapApplServer.inputDeviceUri+"/sonar";
 		SonarProxyAsClient sonarProxy = new SonarProxyAsClient("sonarProxyCoap", host, sonarUri, ProtocolType.coap );
  		sonar            = sonarProxy;
 		
  		coapSonarSup     = (CoapSupport) sonarProxy.getConn();

 		String ledUri  = CoapApplServer.lightsDeviceUri+"/led";
 		led            = new LedProxyAsClient("ledProxyCoap", host, ledUri, ProtocolType.coap );
		
	}

	protected void executeLed() {
		configure();
   		
 		for( int i=1; i<=3; i++) {
	 		led.turnOn();
	 		Utils.delay(1000);
	 		led.turnOff();
	 		Utils.delay(1000);
		}
 		
 		/*
  		for( int i=1; i<=3; i++) {
 			Utils.delay(1000);
 			IDistance d = clientSonarProxy.getDistance(); //bloccante
 			Colors.outappl("RadarSystemMainOnPcCoap | d=" + d, Colors.BLUE  );
 		}
		
 		
 		
		radar  = DeviceFactory.createRadarGui();
 		Controller.activate(led, sonar, radar);  
		Utils.delay(10000);
		*/
		
		terminate();
 	}
	
	protected void executeWithObserverAsController() {
		configure();		
 		radar           = DeviceFactory.createRadarGui();
  		CoapHandler obs = new ControllerAsCoapSonarObserver("obsController", led, radar, ampl) ;
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
	public void doLedBlink() {
		
		
	}
	@Override
	public void stopLedBlink() {
		
		
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
	
	@Override
	public void takePhoto( String fName ) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void startWebCamStream() {
		// TODO Auto-generated method stub
		
	}	
	@Override
	public void stopWebCamStream(  ) {
		
	}
	
	@Override
	public String getImage(String fName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendCurrentPhoto() {
		
	}
	
	@Override
	public void storeImage(String encodedString, String fName) {
		// TODO Auto-generated method stub
		
	}

	public static void main( String[] args) throws Exception {
		new RadarSystemMainOnPcCoap().doJob(null);

	}




}
