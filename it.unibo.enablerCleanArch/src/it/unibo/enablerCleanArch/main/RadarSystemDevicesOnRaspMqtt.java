package it.unibo.enablerCleanArch.main;


import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.EnablerAsServer;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.ContextMqttMsgHandler;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;


/*
 * Applicazione che va in coppia con RadarSystemMainOnPc
 */
public class RadarSystemDevicesOnRaspMqtt implements IApplicationFacade{
private ISonarObservable sonar  = null;
private ILed led          		= null;
private String ctxTopic   		= "topicCtxMqtt";  

	public void setUp( String configFile )   {
		if( configFile != null ) RadarSystemConfig.setTheConfiguration(configFile);
		else {
			RadarSystemConfig.simulation   		= true;
 			RadarSystemConfig.ledGui    		= true;
			RadarSystemConfig.testing      		= false;		
			RadarSystemConfig.protcolType       = ProtocolType.mqtt;			
		}
	}
	
	protected void createServerMqtt( ) {
		sonar = SonarModelObservable.create();		
  		led   = LedModel.create();
		IApplMsgHandler ledHandler   = new LedApplHandler( "ledH", led );
		IContextMsgHandler  ctxH     = new ContextMqttMsgHandler ( "ctxH" );
		ctxH.addComponent("led", ledHandler);
 		
		IApplMsgHandler sonarHandler = new SonarApplHandler("sonarH",sonar);
		ctxH.addComponent("sonar", sonarHandler);	
		
		EnablerAsServer ctxServer    = new EnablerAsServer("CtxServerMqtt", ctxTopic , ctxH );			
		ctxServer.start(); 
  	}
 
	
  
	//@Override
	public void doJob(String configFileName) {
 		setUp(configFileName);
 		createServerMqtt( );
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
	
	public ISonarObservable getSonar() {		 
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
		new RadarSystemDevicesOnRaspMqtt().doJob(null); //"RadarSystemConfig.json"
 	}





}
