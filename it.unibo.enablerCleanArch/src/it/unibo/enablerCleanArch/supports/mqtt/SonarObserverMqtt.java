package it.unibo.enablerCleanArch.supports.mqtt;

 
import java.util.Observable;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.supports.ColorsOut;
 

public class SonarObserverMqtt implements IObserver{
	private String name;
	private MqttSupport mqtt;
 
	public SonarObserverMqtt(String name ) {  
		this.name    = name;
		mqtt         = MqttSupport.getSupport();
	}
 	@Override
	public void update(Observable source, Object data) {
		 ColorsOut.out( name + " | update data=" + data ); //+ " from " + source	
		 update( data.toString() );
	}

	@Override
	public void update(String vs) {
		try {
			 ColorsOut.out( name + " | update vs=" + vs ); //+ " from " + source	
			 mqtt.publish("sonarDataTopic", vs, 0, false);
 		}catch( Exception e) {
			ColorsOut.outerr(name+" | update failure:" + e.getMessage());
		}
	}
}