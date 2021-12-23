package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.supports.Colors;

public class SonarMockObservable extends SonarMock implements ISonarObservable  {
	private IDistanceMeasured observableDistance  ;
	@Override
	protected void sonarSetUp() {
		super.sonarSetUp();
		observableDistance = new DistanceMeasured( );		
		observableDistance.setVal(curVal);
		//Colors.out("SonarMockObservable | sonarSetUp curVal="+curVal, Colors.ANSI_PURPLE);
	} 	
	@Override  //from SonarMock
	protected void updateDistance( int d ) {
		Colors.out("SonarMockObservable | updateDistance d="+d, Colors.GREEN);
 		super.updateDistance(d);	            //pone curVal nella coda per getDistance
		observableDistance.setVal( curVal );    //notifies the observers 
	}

 	@Override
	public void register(IObserver obs) {
		Colors.out("SonarObservableMock | register on observableDistance obs="+obs);
		observableDistance.addObserver(obs);		
	}

	@Override
	public void unregister(IObserver obs) {
		Colors.out("SonarObservableMock | unregister obs="+obs);
		observableDistance.deleteObserver(obs);		
	}
  
}
