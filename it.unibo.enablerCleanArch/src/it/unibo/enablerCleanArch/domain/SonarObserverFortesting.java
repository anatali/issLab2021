package it.unibo.enablerCleanArch.domain;

import static org.junit.Assert.assertTrue;
import java.util.Observable;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarObserverFortesting implements IObserver{
	private String name;
	private boolean oneShot = false;
	private int v0          = -1;
	private int delta       =  1;
	
	public SonarObserverFortesting(String name,  boolean oneShot) {  
		this.name    = name;
		this.oneShot = oneShot;
	}
	@Override
	public void update(Observable source, Object data) {
		 ColorsOut.out( name + " | update data=" + data ); //+ " from " + source	
		 update( data.toString() );
		 //HORRIBLE EFFECT !!??
		 //Utils.delay(2000);  //the SonarMock is delayed !!!
	}

	@Override
	public void update(String vs) {
		try {
 		 if(oneShot) {
 			 ColorsOut.outappl( name + "| oneShot value=" + vs, ColorsOut.BLUE );  
 			 if( RadarSystemConfig.testing ) assertTrue(  vs.equals( ""+RadarSystemConfig.testingDistance) );	
 		 }else {
 			 int value = Integer.parseInt(vs);
 			 if( v0 == -1 ) {	//set the first value observed
 				v0 = value;
 				ColorsOut.outappl( name + "| v0=" + v0, ColorsOut.BLUE);
 			 }else {
 				ColorsOut.outappl( name + "| value=" + value, ColorsOut.BLUE );  
  				int vexpectedMin = v0-delta;
 				int vexpectedMax = v0+delta;
 				if( RadarSystemConfig.testing )
 					assertTrue(  value <= vexpectedMax && value >= vexpectedMin );
 				v0 = value;			 
 			 }
 		 } 
		}catch( Exception e) {
			ColorsOut.outerr(name+" | update failure:" + e.getMessage());
		}
	}
}