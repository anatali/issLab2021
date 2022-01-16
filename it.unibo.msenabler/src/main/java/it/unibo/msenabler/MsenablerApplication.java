package it.unibo.msenabler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unibo.enablerCleanArch.domain.ISonarObservable;
import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;
import it.unibo.enablerCleanArch.main.RadarSystemMainOnPcCoap;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

@SpringBootApplication
public class MsenablerApplication {
public static RadarSystemDevicesOnRaspMqtt sysMqtt ;
public static RadarSystemMainOnPcCoap sysCoap ;
public static final boolean allOnRasp = false;
/*
 * This operation is called when the application runs over Raspberry
 */
    private static void startSystemMqtt() {
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
    	
    }
    
    /*
     * This operation is called when the application runs on PC
     */
    private static void startSystemCoap() {
    	sysCoap= new RadarSystemMainOnPcCoap();
    	sysCoap.setUp(null);
    }
    
	public static void main(String[] args) {
		SpringApplication.run(MsenablerApplication.class, args);
		if( allOnRasp ) startSystemMqtt();
		else startSystemCoap();
	}

}
