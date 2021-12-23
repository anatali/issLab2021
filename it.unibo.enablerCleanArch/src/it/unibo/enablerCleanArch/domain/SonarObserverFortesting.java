package it.unibo.enablerCleanArch.domain;

import static org.junit.Assert.assertTrue;
import java.util.Observable;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public class SonarObserverFortesting implements IObserver{
	private String name;
	private boolean oneShot = false;
	private int v0          = -1;
	private int delta       =  1;
//	private ISonarObservable sonar;
	
	public SonarObserverFortesting(String name,  boolean oneShot) { //ISonarObservable sonar,
		this.name    = name;
//		this.sonar   = sonar;
		this.oneShot = oneShot;
	}
	@Override
	public void update(Observable source, Object data) {
		 //Colors.out( name + " | update data=" + data ); //+ " from " + source	
		 update( data.toString() );
	}

	@Override
	public void update(String vs) {
		try {
 		 if(oneShot) {
 			 Colors.outappl( name + "| oneShot value=" + vs, Colors.ANSI_YELLOW );  
 			 assertTrue(  vs.equals( ""+RadarSystemConfig.testingDistance) );	
 		 }else {
 			 int value = Integer.parseInt(vs);
 			 if( v0 == -1 ) {	//set the first value observed
 				v0 = value;
 				Colors.outappl( name + "| v0=" + v0, Colors.ANSI_YELLOW);
 			 }else {
 				Colors.outappl( name + "| value=" + value, Colors.ANSI_YELLOW );  
  				int vexpectedMin = v0-delta;
 				int vexpectedMax = v0+delta;
 				//assertTrue(  value <= vexpectedMax && value >= vexpectedMin );
 				v0 = value;			 
 				//if( v0 == 30 && name.equals("obs1")) sonar.unregister(this);
 			 }
 		 } 
		}catch( Exception e) {
			Colors.outerr(name+" | update failure:" + e.getMessage());
		}
	}
}