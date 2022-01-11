package it.unibo.msenabler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.unibo.enablerCleanArch.main.RadarSystemDevicesOnRaspMqtt;

@SpringBootApplication
public class MsenablerApplication {
public static RadarSystemDevicesOnRaspMqtt sys ;

	public static void main(String[] args) {
		SpringApplication.run(MsenablerApplication.class, args);
		sys = new RadarSystemDevicesOnRaspMqtt();
		sys.doJob("RadarSystemConfig.json");
	}

}
