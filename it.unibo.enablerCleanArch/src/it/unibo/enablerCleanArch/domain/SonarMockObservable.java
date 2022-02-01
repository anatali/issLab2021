package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarMockObservable extends SonarModelObservable   {

	@Override
	protected void sonarSetUp() {
		observableDistance = new DistanceMeasured( );
 		ColorsOut.out("SonarModcklObservable | sonarSetUp curVal="+curVal);
 		observableDistance.setVal(curVal);
 	} 	

	@Override
	protected void sonarProduce() {
		if( RadarSystemConfig.testing ) {
			updateDistance( RadarSystemConfig.testingDistance );			      
			stopped = true;  //one shot
		}else {
			int v = curVal.getVal() - 1;
			updateDistance( v );			    
 			stopped = ( v == 0 );
			Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation
		}		
	}
  
}
