package it.unibo.msenabler;

import it.unibo.enablerCleanArch.domain.IApplicationFacade;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;
import it.unibo.enablerCleanArch.main.RadarSystemMainOnPcCoapBase;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;

@SpringBootApplication
public class MsenablerApplication {
public static RadarSystemDevicesOnRaspMqtt sysMqtt ;
public static RadarSystemMainOnPcCoapBase sysCoap ;
public static final boolean allOnRasp = false;   //when true, this appl must run on Raspberry
/*
 * This operation is called when the application runs over Raspberry
 */
    public static IApplicationFacade startSystemMqtt() {
		sysMqtt = new RadarSystemDevicesOnRaspMqtt();
        WebSocketHandler h     = WebSocketHandler.getWebSocketHandler();
        sysMqtt.doJob("RadarSystemConfig.json");
        
        ISonarObservable sonar = sysMqtt.getSonar();
        while( sonar == null ) {
        	Colors.out("MsenablerApplication | sonar is null ... ");
        	Utils.delay(250);
        	sonar = sysMqtt.getSonar();
        }
        sonar.register(h);
    	return sysMqtt;
    }
    
    /*
     * This operation is called when the application runs on PC
     */
    public static IApplicationFacade startSystemCoap(String raspAddr) {
    	sysCoap= new RadarSystemMainOnPcCoapBase(raspAddr);
    	sysCoap.setUp(null);
        WebSocketHandler h  = WebSocketHandler.getWebSocketHandler();
    	CoapSupport sonarCoapSupport = sysCoap.getSonarCoapSupport();
    	sonarCoapSupport.observeResource(new SonarDataObserver(h) );
    	//RadarSystemConfig.raspHostAddr = "192.168.1.112";
		return sysCoap;
    }
    
	public static void main(String[] args) {
		SpringApplication.run(MsenablerApplication.class, args);
		//if( allOnRasp ) startSystemMqtt(); else startSystemCoap();
	}

}
