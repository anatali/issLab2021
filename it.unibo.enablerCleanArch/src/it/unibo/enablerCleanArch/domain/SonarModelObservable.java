package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;

public abstract class SonarModelObservable extends SonarModel implements ISonarObservable  {
	protected IDistanceMeasured observableDistance  ;		
 		
	public static ISonarObservable create() {
		if( RadarSystemConfig.simulation )  return new SonarMockObservable();
		else  return new SonarConcreteObservable();		
	}
	
	@Override
	public IDistance getDistance() {
  		return observableDistance;
 	}
	protected void updateDistance( int d ) {
 		observableDistance.setVal(new Distance( d ));
		//ColorsOut.out("SonarModelObservable | updateDistance "+ d);
	}	
  	@Override
	public void register(IObserver obs) {
		ColorsOut.out("SonarModelObservable | register on observableDistance obs="+obs);
		observableDistance.addObserver(obs);		
	}
	@Override
	public void unregister(IObserver obs) {
		ColorsOut.out("SonarModelObservable | unregister obs="+obs);
		observableDistance.deleteObserver(obs);		
	}

  
}
