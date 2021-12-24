package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarMockObservable extends SonarModelObservable   {
  
	@Override
	protected void sonarProduce() {
		if( RadarSystemConfig.testing ) {
			updateDistance( RadarSystemConfig.testingDistance );			      
			stopped = true;  //one shot
		}else {
			int v = curVal.getVal() - 1;
			updateDistance( v );			    
 			if( blockingQueue.size() > 7) Colors.out("SonarMock | queue size="+blockingQueue.size(), Colors.RED);
			stopped = ( v == 0 );
			Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation
		}		
	}
  
}
