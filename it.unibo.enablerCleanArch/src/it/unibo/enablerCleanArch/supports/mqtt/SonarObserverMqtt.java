package it.unibo.enablerCleanArch.supports.mqtt;

 
import java.util.Observable;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.supports.ColorsOut;
 

public class SonarObserverMqtt implements IObserver{
	private String name;
	private MqttConnection mqtt;
 
	public SonarObserverMqtt( String name ) {  
		this.name    = name;
		mqtt         = MqttConnection.getSupport();
	}
 	@Override  //java.util.Observer
	public void update(Observable source, Object data) {
		 ColorsOut.out( name + " | update data=" + data, ColorsOut.GREEN ); //+ " from " + source	
		 update( data.toString() );
	}

	@Override  //IObserver
	public void update(String vs) {
		try {
			 ColorsOut.out( name + " | update vs=" + vs, ColorsOut.GREEN ); //+ " from " + source	
			 mqtt.publish("sonarDataTopic", vs, 0, false);
 		}catch( Exception e) {
			ColorsOut.outerr(name+" | update failure:" + e.getMessage());
		}
	}
}