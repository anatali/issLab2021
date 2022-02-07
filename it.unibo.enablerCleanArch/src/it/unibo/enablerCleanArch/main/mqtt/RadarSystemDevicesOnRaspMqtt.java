package it.unibo.enablerCleanArch.main.mqtt;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.context.Context2021;
import it.unibo.enablerCleanArch.supports.mqtt.ContextMqttMsgHandler;
import it.unibo.enablerCleanArch.supports.mqtt.EnablerAsServerMqtt;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArch.supports.mqtt.SonarObserverMqtt;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;


/*
 * Applicazione che va in coppia con RadarSystemMainOnPcMqtt
 */
public class RadarSystemDevicesOnRaspMqtt implements IApplication {  
private ISonar  sonar  = null;
private ILed led       = null;
//private String ctxTopic= "topicCtxMqtt";  

	public void setUp( String configFile )   {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		//else {
			RadarSystemConfig.simulation   		= false;
 			RadarSystemConfig.ledGui    		= false;
		    RadarSystemConfig.withContext       = true;  //dont't care ???
			RadarSystemConfig.testing      		= false;	
			RadarSystemConfig.protcolType       = ProtocolType.mqtt;			
		//}
	}
	
	protected void configure( ) {
  		//MqttSupport mqtt = MqttSupport.getSupport();
    //CReazione del contesto (in funzione del protocollo)
		String clientId 		=  "rasp";
		String topicToSubscribe =  MqttSupport.topicInput;
		IContext ctx            = Context2021.create(clientId,topicToSubscribe);    //activates!!
		
		if( RadarSystemConfig.sonarObservable ) {
			sonar = SonarModelObservable.create();		
			IObserver sonarObs  = new SonarObserverMqtt( "sonarObs" ) ;
			((ISonarObservable)sonar).register( sonarObs );			
		}else { sonar = SonarModel.create(); }
		ColorsOut.out(" | createServerMqtt CREATED sonar= " + sonar, ColorsOut.BLUE);
  		led   = LedModel.create();

		
  		//Aggiunta degli handler per i comandi e le richieste
		IApplMsgHandler ledHandler   = new LedApplHandler( "ledH", led );
 		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		
		ctx.addComponent("led", ledHandler);
		ctx.addComponent("sonar", sonarHandler);
  	}
 
	
  
	//@Override
	public void doJob(String configFileName) {
 		setUp(configFileName);
 		configure( );
 	}

	@Override
	public String getName() {
		return "RadarSystemDevicesOnRaspMqtt";
	}

//----------------------------------------------------------------
	private boolean ledblinking = false;
	
	public void ledActivate( boolean v ) {
		if( v ) led.turnOn();
		else led.turnOff();
	}
	
	public String ledState(   ) {
		return ""+led.getState();
	}
	public void sonarActivate(   ) {
		sonar.activate();
	}
	public boolean sonarIsactive(   ) {
		return sonar.isActive();
	}

	public void sonarDectivate(   ) {
		sonar.deactivate();
	}
	public String sonarDistance(   ) {
 		return ""+sonar.getDistance().getVal();
	}
	
	public ISonar getSonar() {		 
		return sonar;
	}

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
	public void stopLedBlink() {
		ledblinking = false;
	}
	
	/*
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
*/
	
	public static void main( String[] args) throws Exception {
		new RadarSystemDevicesOnRaspMqtt().doJob("RadarSystemConfig.json"); //"RadarSystemConfig.json"
 	}





}
