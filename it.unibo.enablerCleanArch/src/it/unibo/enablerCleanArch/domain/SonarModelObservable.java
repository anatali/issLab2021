package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;

public abstract class SonarModelObservable extends SonarModel implements ISonarObservable  {
	protected IDistanceMeasured observableDistance ; 
 		
	public static ISonarObservable create() {
		if( RadarSystemConfig.simulation )  return new SonarMockObservable();
		else  return new SonarConcreteObservable();		
	}
	
	@Override
	public IDistance getDistance() {
  		return observableDistance;
 	}
	@Override
	protected void updateDistance( int d ) {
		ColorsOut.out("SonarModelObservable call updateDistance | d=" + d, ColorsOut.GREEN );
 		observableDistance.setVal( new Distance( d ) ); //notifies
 	}	
  	@Override
	public void register(IObserver obs) {
		ColorsOut.out("SonarModelObservable | register on observableDistance obs="+obs, ColorsOut.GREEN);
		observableDistance.addObserver(obs);		
	}
	@Override
	public void unregister(IObserver obs) {
		ColorsOut.out("SonarModelObservable | unregister obs="+obs, ColorsOut.GREEN);
		observableDistance.deleteObserver(obs);		
	}

  
}
