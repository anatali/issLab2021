package it.unibo.enablerCleanArch.main.coap;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.enablers.ProxyAsClient;
import it.unibo.enablerCleanArch.enablers.SonarProxyAsClient;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapConnection;


/*
 * DA usare su PC con i dispositivi attivi su Raspberry (RadaSystemMainCoap)
 */

public class RadarSystemMainOnPcCoapBaseOld implements IApplicationFacadeWithWebcam{
private ISonar sonar    		   = null;
private ILed led        		   = null;
private IRadarDisplay radar 	   = null;
private boolean useProxyClient 	   = true;
private ISonar clientSonarProxy    = null;
private CoapClient clientObs 	   = null;
private CoapObserveRelation relObs = null;
private IObserver obsfortesting;
private CoapConnection coapSonarSup   = null;
private CoapConnection coapLedSup     = null;
private CoapConnection coapWebCamSup  = null;
private CoapObserveRelation rel1   = null;
private final int ampl             = 3;
private boolean ledblinking        = false;

    public RadarSystemMainOnPcCoapBaseOld(String addr) {
    	RadarSystemConfig.raspHostAddr = addr;
    }
	@Override
	public String getName() {
		return "RadarSystemMainOnPcCoap";
	}
	
	@Override
	public void setUp( String configFile )  {			
 			RadarSystemConfig.DLIMIT       = 10*ampl;
			RadarSystemConfig.simulation   = false;
			RadarSystemConfig.withContext  = false;
			RadarSystemConfig.sonarDelay   = 250;
 			configureUsingProxy();
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
  		coapSonarSup     = (CoapConnection) sonarProxy.getConn();
ColorsOut.out("........................................ coapSonarSup=" + coapSonarSup);
 		String ledUri  = CoapApplServer.lightsDeviceUri+"/led";
 		led            = new LedProxyAsClient("ledProxyCoap", host, ledUri, ProtocolType.coap );		
 
 		CoapHandler obs = new ControllerAsCoapSonarObserver("obsController", led, radar, ampl) ;
		rel1            = coapSonarSup.observeResource( obs );
		
 		String webcamUri = CoapApplServer.inputDeviceUri+"/webcam";
 		//coapWebCamSup    = new CoapConnection("coapWebCamSup",RadarSystemConfig.raspHostAddr, webcamUri);
		coapWebCamSup    = new CoapConnection( RadarSystemConfig.raspHostAddr, webcamUri);
		     
	}

	public CoapConnection getSonarCoapSupport() {
		return coapSonarSup;
	}
	
	/*
	 * Simula operazioni che farà la Spring GUI
	 */
 
	public void entryMainAsApplInGui( ) {
		setUp(null);
		RadarSystemConfig.raspHostAddr = "192.168.1.9";  
   		//CoapHandler obs = new ObserverNaive("obs"); //new ControllerAsCoapSonarObserver("obsController", led, radar) ;
		//rel1            = coapSonarSup.observeResource( obs );
		//Controller.activate(led, sonar, radar);  
		led.turnOn();
		ColorsOut.out("ledState=" +led.getState() );
		Utils.delay(500);
		led.turnOff();
		ColorsOut.out("ledState=" +led.getState() );
		
		
		//Terminate
//		sonarDectivate() ;	//termina il Sonar
		coapSonarSup.close();
		if(rel1 != null ) rel1.proactiveCancel();
		 
		System.exit(0);		
	}
	
	public void entryorMainOnPc( ) {
		RadarSystemConfig.raspHostAddr = "192.168.1.9";
		RadarSystemConfig.protcolType  = ProtocolType.coap;
		RadarSystemConfig.DLIMIT       = 10*ampl;
		RadarSystemConfig.simulation   = false;
		RadarSystemConfig.withContext  = false;
		RadarSystemConfig.sonarDelay   = 250;
		radar  = DeviceFactory.createRadarGui(); //since it is called on PC
		configureUsingCoapSupport();
		executeCoapUsingCoapSupport();
		//Terminate
		sonarDectivate() ;	//termina il Sonar
		if(rel1 != null ) rel1.proactiveCancel();
		System.exit(0);
	}

	/*
	 * NO: Meglio usare i supporti creati entro i proxy, anche se bisogna poi 'estrarre' il CoapSupport
	 * per costruire un coap observer
	 */	
	protected void configureUsingCoapSupport() {
 		String sonarUri = CoapApplServer.inputDeviceUri+"/sonar";
 		//coapSonarSup    = new CoapConnection("coapSonarSup",RadarSystemConfig.raspHostAddr, sonarUri);
 		coapSonarSup    = new CoapConnection( RadarSystemConfig.raspHostAddr, sonarUri );
 		//Colors.out("............. coapSonarSup=" + coapSonarSup);
 		String ledUri   = CoapApplServer.lightsDeviceUri+"/led";
 		led             = new LedProxyAsClient("ledProxyCoap", RadarSystemConfig.raspHostAddr, ledUri, ProtocolType.coap );
 		CoapHandler obs = new ControllerAsCoapSonarObserver("obsController", led, radar, ampl) ;
		rel1            = coapSonarSup.observeResource( obs );
 	}
	
	/*
	 * Da usare quando lo si lancia su PC
	 */
	protected void executeCoapUsingCoapSupport() {
		//led.turnOff();
		ledActivate(true);
		ColorsOut.out("ledState=" +led.getState() );
		Utils.delay(1000);
		ledActivate(false);
		ColorsOut.out("ledState=" +led.getState() );
		Utils.delay(1000);
		
		
		sonarActivate();	
 		Utils.delay(10000);
		/*
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
	public void doLedBlink() {
		new Thread() {
			public void run() {
				ledblinking = true;
				while( ledblinking ) {
					ledActivate(true);
					Utils.delay(500);
					ledActivate(false);
					Utils.delay(500);
				}
			}
		}.start();		
		
	}
	@Override
	public void stopLedBlink() {
		ledblinking = false;		
	}
	
	@Override
	public void sonarActivate() {
		ColorsOut.out("RadarSystemMainOnPcCoapBase | sonarActivate");
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

	@Override
	public void takePhoto( String fName ) {
		coapWebCamSup.forward("takePhoto-"+fName);	
	}
	
	public void sendPhoto( String photo) {
		Request request = Request.newPost();
		request.setURI("");
		request.setPayload(photo);
		request.getOptions().setContentFormat(MediaTypeRegistry.IMAGE_JPEG);
		try {
		request.send();
		} catch (Exception e) {
		System.err.println("Failed to execute request: " + e.getMessage());
		}

	}
	
	@Override
	public void startWebCamStream(  ) {
		coapWebCamSup.forward("startWebCamStream");	
	}
	
	@Override
	public void stopWebCamStream() {
		coapWebCamSup.forward("stopWebCamStream");	
	}
	
	public String getImage(String fName) {
		try {
			String imgBase64 = coapWebCamSup.request("getImage-" + fName);
			ColorsOut.out("RadarSystemMainOnPcCoapBase | getImage imgBase64.length()=" + imgBase64.length());
			return imgBase64;
 		} catch (Exception e) {
 			ColorsOut.outerr("RadarSystemMainOnPcCoapBase | getImage " + e.getMessage());
 			return "";
		}
	}
	
	@Override
	public void sendCurrentPhoto() {
		String answer = coapWebCamSup.request("sendCurrentPhoto"  );
		ColorsOut.out("RadarSystemMainOnPcCoapBase | sendCurrentPhoto answer=" + answer);
	}
	
	public void storeImage(String encodedString, String fName) {
		try {
			byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
			ColorsOut.out("RadarSystemMainOnPcCoapBase | storeImage decodedBytes.length=" + decodedBytes.length);
			FileUtils.writeByteArrayToFile(new File(fName), decodedBytes);
		} catch (Exception e) {
 			ColorsOut.outerr("RadarSystemMainOnPcCoapBase | storeImage " + e.getMessage());			 
		}
	}
	
	public static void main( String[] args) throws Exception {
		//new RadarSystemMainOnPcCoapBase().entryorMainOnPc();//   
		new RadarSystemMainOnPcCoapBaseOld("192.168.1.9").entryMainAsApplInGui();

	}

	
 

 

 


}
