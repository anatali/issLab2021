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
		super.updateDistance(d);	//pone curVal nella coda
		observableDistance.setVal( curVal );    //notifies the observers 
	}

 	@Override
	public void register(IObserver obs) {
		observableDistance.addObserver(obs);		
	}

	@Override
	public void unregister(IObserver obs) {
		Colors.out("SonarObservableMock | unregister obs="+obs);
		observableDistance.deleteObserver(obs);		
	}
  
}
