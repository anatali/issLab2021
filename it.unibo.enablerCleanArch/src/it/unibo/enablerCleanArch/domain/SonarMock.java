package it.unibo.enablerCleanArch.domain;

import java.util.concurrent.BlockingQueue;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarMock extends SonarModel implements ISonar{
protected  IDistance curVal ;  
	@Override
	protected void sonarSetUp() {
		curVal = new Distance(90);		
		Colors.out("SonarMock | sonarSetUp curVal="+curVal);
	}

	@Override
	protected void sonarProduce(BlockingQueue<IDistance> queue) {
		try {
		    if( RadarSystemConfig.testing ) {
			      queue.put( new Distance( RadarSystemConfig.testingDistance ) );
			      stopped = true;  //one shot
			}else {
				curVal = new Distance(  curVal.getVal() - 1 );
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
