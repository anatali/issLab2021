package it.unibo.enablerCleanArch.main.onpc;

import java.util.Observable;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
 

public class SonarObserverEmitter implements IObserver{
	private String name;
	private Interaction2021 conn;
	
	public SonarObserverEmitter( String name, Interaction2021 conn ) {  
		this.name    = name;
		this.conn    = conn;
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
//			 if( Utils.isMqtt() ) {
//				 //mqtt.publish("sonarDataTopic", vs, 0, false);
//			 }
			 conn.forward(vs);
 		}catch( Exception e) {
			ColorsOut.outerr(name+" | update failure:" + e.getMessage());
		}
	}
}