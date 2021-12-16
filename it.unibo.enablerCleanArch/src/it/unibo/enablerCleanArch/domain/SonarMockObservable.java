package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.supports.Colors;

public class SonarMockObservable extends SonarMock implements ISonarObservable  {
	DistanceObservable observableDistance; 
	@Override
	protected void sonarSetUp() { 
		super.sonarSetUp();
		//observableDistance = this.curVal;
		observableDistance = new DistanceObservable( curVal );		
		//Colors.out("SonarMockObservable | sonarSetUp curVal="+curVal.getVal());
	}
	
	@Override  //from SonarMock
	protected void updateDistance( int d ) {
		super.updateDistance(d);
//		DistanceObservable dobs = new DistanceObservable( new Distance( d ) );
//		dobs.setVal(d);
//		curVal = dobs;
		observableDistance.setVal(d);    //Nella coda ci va curVal che cambia
		//TODO: fare un consumatore della coda (controller like)
	}

 	@Override
	public void register(IObserver obs) {
		try {
			observableDistance.addObserver(obs);		
		}catch(Exception e) {
			Colors.outerr("SonarMockObservable | register ERROR="+e.getMessage());
		}
	}

	@Override
	public void unregister(IObserver obs) {
		Colors.out("SonarObservableMock | unregister obs="+obs);
		observableDistance.deleteObserver(obs);		
	}
  
}
