package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class SonarModelObservable extends SonarModel implements ISonarObservable  {
	protected IDistanceMeasured observableDistance  ;		
 		
	public static ISonarObservable create() {
		if( RadarSystemConfig.simulation )  return new SonarMockObservable();
		else  return new SonarConcreteObservable();		
	}

//	@Override
//	protected void sonarSetUp() {
//		observableDistance = new DistanceMeasured( );
// 		Colors.out("SonarModelObservable | sonarSetUp curVal="+curVal);
// 		observableDistance.setVal(curVal);
// 	} 	
	
	//Ritorna il valore più recente
//	@Override
//	public IDistance getDistance() {
//  		return curVal;
// 	}

 
	
 
// 	@Override  //from SonarModel
//	protected void updateDistance( int d ) {
//		curVal = new Distance( d );
//		Colors.out("SonarModelObservable | updateDistance "+curVal.getVal(), Colors.GREEN);
//		observableDistance.setVal( curVal );    //notifies the observers 
//		//Non aggiorniamo la coda perchè per un observable non ci sono consumatori
// 		//super.updateDistance(d);	             
//	}
 

 	@Override
	public void register(IObserver obs) {
		Colors.out("SonarModelObservable | register on observableDistance obs="+obs);
		observableDistance.addObserver(obs);		
	}

	@Override
	public void unregister(IObserver obs) {
		Colors.out("SonarModelObservable | unregister obs="+obs);
		observableDistance.deleteObserver(obs);		
	}
  
}
