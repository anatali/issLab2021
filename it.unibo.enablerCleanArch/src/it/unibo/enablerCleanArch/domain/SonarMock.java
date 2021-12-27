package it.unibo.enablerCleanArch.domain;

 
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class SonarMock extends SonarModel implements ISonar{
//protected  IDistance curVal ;  
private int delta = 1;
	@Override
	protected void sonarSetUp() {
		curVal = new Distance(90);		
		Colors.out("SonarMock | sonarSetUp curVal="+curVal);
	}
	
 
	@Override
	protected void updateDistance( int d ) {
 		curVal = new Distance( d );
  	}	
	@Override
	public IDistance getDistance() {
		return curVal;
	}	
	@Override
	protected void sonarProduce( ) {
		if( RadarSystemConfig.testing ) {
			updateDistance( RadarSystemConfig.testingDistance );			      
			stopped = true;  //one shot
		}else {
			int v = curVal.getVal() - delta;
			updateDistance( v );			    
 			if( blockingQueue.size() > 7) Colors.out("SonarMock | queue size="+blockingQueue.size(), Colors.RED);
			stopped = ( v == 0 );
			Utils.delay(RadarSystemConfig.sonarDelay);  //avoid fast generation
		}
 	}
}
