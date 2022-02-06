package it.unibo.enablerCleanArch.main.all;

import java.util.Observable;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.supports.ColorsOut;
 

public class SonarObserver implements IObserver{
	private String name;
  
	public SonarObserver( String name ) {  
		this.name    = name;
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
			 //mqtt.publish("sonarDataTopic", vs, 0, false);
 		}catch( Exception e) {
			ColorsOut.outerr(name+" | update failure:" + e.getMessage());
		}
	}
}