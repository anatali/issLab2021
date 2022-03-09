package it.unibo.radarSystem22.domain.mock;

import it.unibo.radarSystem22.domain.DistanceMeasured;
import it.unibo.radarSystem22.domain.SonarModelObservable;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class SonarMockObservable extends SonarModelObservable   {

	@Override
	protected void sonarSetUp() {
		observableDistance = new DistanceMeasured( );
 		ColorsOut.out("SonarMockObservable | sonarSetUp curVal="+curVal);
 		observableDistance.setVal(curVal);
 	} 	

	@Override
	protected void sonarProduce() {
		if( DomainSystemConfig.testing ) {
			updateDistance( DomainSystemConfig.testingDistance );			      
			stopped = true;  //one shot
		}else {
			int v = observableDistance.getDistance( ).getVal() - 1; //curVal.getVal() - 1; 
			ColorsOut.out("SonarMockObservable | produced:"+v);
			updateDistance( v );			    
 			stopped = ( v == 0 );
 			//curVal  = new Distance( v );
			BasicUtils.delay(DomainSystemConfig.sonarDelay);  //avoid fast generation
		}		
	}
}
