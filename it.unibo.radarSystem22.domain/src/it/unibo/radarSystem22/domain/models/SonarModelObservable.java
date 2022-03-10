package it.unibo.radarSystem22.domain.models;

 
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcreteObservable;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.IDistanceMeasured;
import it.unibo.radarSystem22.domain.interfaces.IObserver;
import it.unibo.radarSystem22.domain.interfaces.ISonarObservable;
import it.unibo.radarSystem22.domain.mock.SonarMockObservable;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public abstract class SonarModelObservable extends SonarModel implements ISonarObservable  {
	protected IDistanceMeasured observableDistance ; 
 		
	public static ISonarObservable create() {
		if( DomainSystemConfig.simulation )  return new SonarMockObservable();
		else  return new SonarConcreteObservable();		
	}
	
	@Override
	public IDistance getDistance() {
  		return observableDistance;
 	}
	@Override
	protected void updateDistance( int d ) {
		//ColorsOut.out("SonarModelObservable call updateDistance | d=" + d, ColorsOut.GREEN );
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
