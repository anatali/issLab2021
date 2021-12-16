package it.unibo.enablerCleanArch.domain;

import java.util.concurrent.BlockingQueue;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarMock extends SonarModel implements ISonar{
protected  IDistance curVal ;  
private int delta = 1;
	@Override
	protected void sonarSetUp() {
		curVal = new Distance(90);		
		Colors.out("SonarMock | sonarSetUp curVal="+curVal);
	}
	
	protected void updateDistance( int d) {
		curVal = new Distance( d );
	}

	@Override
	protected void sonarProduce(BlockingQueue<IDistance> queue) {
		try {
		    if( RadarSystemConfig.testing ) {
		    	 updateDistance( RadarSystemConfig.testingDistance );
			     queue.put( curVal );
			     stopped = true;  //one shot
			}else {
				updateDistance( curVal.getVal() - delta );
			    queue.put( curVal );
			    if( queue.size() > (SonarModel.queueSize * 8 / 10) ) Colors.out("SonarMock | queue size="+queue.size());
	 		    stopped = ( curVal.getVal() == 0 );
	 			Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation
			}
		}catch(Exception e) {
			Colors.outerr("SonarMock | ERROR:"+e.getMessage());
		}
 	}
 
/*	@Override
	  protected void sonarProduce() {
	    if( RadarSystemConfig.testing ) {
	      curVal.setVal( RadarSystemConfig.testingDistance );
	      stopped = true;  //one shot
	    }else {
	    	curVal.setVal( curVal.getVal() - 1 ) ;
	    	stopped = ( curVal.getVal() == 0 );
	  }
	  valueUpdated(   );
	  Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation
	}
 */
}
