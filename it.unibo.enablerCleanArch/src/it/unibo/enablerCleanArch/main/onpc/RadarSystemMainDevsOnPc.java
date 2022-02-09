package it.unibo.enablerCleanArch.main.onpc;

import it.unibo.enablerCleanArch.domain.*;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.main.all.SonarObserver;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;
import it.unibo.enablerCleanArchapplHandlers.SonarApplHandler;
import it.unibo.enablerCleanArch.supports.context.Context2021; 

/*
 * Applicazione che va in coppia con RadarSystemMainUasgeOnPc
 */

public class RadarSystemMainDevsOnPc implements IApplication{
	private ISonar sonar;
	private ILed  led ;
 	private IContext ctx;

	@Override
	public String getName() {	 
		return "RadarSystemMainDevsOnPc";
	}

	public void setup(   )  {
 			RadarSystemConfig.ctxServerPort     = 8018;
			RadarSystemConfig.withContext       = true;
		    RadarSystemConfig.protcolType       = ProtocolType.coap;
 			RadarSystemConfig.sonarDelay        = 200;
 			RadarSystemConfig.simulation   		= true;
			RadarSystemConfig.DLIMIT      		= 55;  
			RadarSystemConfig.ledGui            = true;
			RadarSystemConfig.testing           = false;
			RadarSystemConfig.tracing           = true;
			RadarSystemConfig.sonarObservable   = true;
			RadarSystemConfig.mqttBrokerAddr    = "tcp://broker.hivemq.com"; //: 1883  OPTIONAL  "tcp://localhost:1883" 	
 	}
	
 	
	 	
	protected void configure() {
		//Dispositivi di Input
		if( RadarSystemConfig.sonarObservable ) {
			sonar               = DeviceFactory.createSonarObservable();		
//			IObserver sonarObs  = new SonarObserver( "sonarObs" ) ;
//			((ISonarObservable)sonar).register( sonarObs );			
		}else{
			sonar      = DeviceFactory.createSonar();
		}
	    //Dispositivi di Output
	    led        = DeviceFactory.createLed();
	    //led.turnOff();
		//Creazione del server di contesto
	    String id 	 = "";
	    String entry = "";
		switch( RadarSystemConfig.protcolType ) {
		case tcp :  { id="rasp";  entry=""+RadarSystemConfig.ctxServerPort; break; }
		case coap : {  break; }
		case mqtt : { id="rasp";  entry=MqttSupport.topicInput; break; }
		default:
		};
	    ctx  = Context2021.create(id,entry);
  		//Registrazione dei componenti presso il contesto
 		  IApplMsgHandler sonarHandler = SonarApplHandler.create("sonarH",sonar); 
		  IApplMsgHandler ledHandler   = LedApplHandler.create("ledH",led);		  
 		  ctx.addComponent("sonar", sonarHandler);	    //sonar NAME mandatory
		  ctx.addComponent("led",   ledHandler);		//led NAME mandatory
	}
  
	public void terminate() {
		//Utils.delay(1000);  //For the testing ...
		sonar.deactivate();
		ctx.deactivate();
		System.exit(0);
	}

	public void doJob( String configFileName ) {
		setup( );
		configure();
 	}
	
	public static void main( String[] args) throws Exception {
 		new RadarSystemMainDevsOnPc().doJob( null );
 	}

}
