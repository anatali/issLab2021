package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.supports.Colors;

public abstract class SonarModelObservable extends SonarModel implements ISonarObservable  {
	protected IDistanceMeasured observableDistance  ;		
 		
	@Override
	protected void sonarSetUp() {
		observableDistance = new DistanceMeasured( );
 		Colors.out("SonarModelObservable | sonarSetUp curVal="+curVal);
 		observableDistance.setVal(curVal);
 	} 	
	
 	@Override  //from SonarModel
	protected void updateDistance( int d ) {
		//Colors.out("SonarModelObservable | updateDistance d="+d, Colors.GREEN);
		observableDistance.setVal( curVal );    //notifies the observers 
 		super.updateDistance(d);	            //pone curVal nella coda per getDistance
	}

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
