package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarMockObservable extends SonarModelObservable   {

	@Override
	protected void sonarSetUp() {
		observableDistance = new DistanceMeasured( );
 		ColorsOut.out("SonarMockObservable | sonarSetUp curVal="+curVal);
 		observableDistance.setVal(curVal);
 	} 	

	@Override
	protected void sonarProduce() {
		if( RadarSystemConfig.testing ) {
			updateDistance( RadarSystemConfig.testingDistance );			      
			stopped = true;  //one shot
		}else {
			int v = observableDistance.getDistance( ).getVal() - 1; //curVal.getVal() - 1; 
			ColorsOut.out("SonarMockObservable | produced:"+v);
			updateDistance( v );			    
 			stopped = ( v == 0 );
 			//curVal  = new Distance( v );
			Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation
		}		
	}
}
