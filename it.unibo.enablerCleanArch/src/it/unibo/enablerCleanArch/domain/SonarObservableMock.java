package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.supports.Colors;

public class SonarObservableMock extends SonarMock implements ISonarObservable  {
 
	@Override
	protected void sonarSetUp() { 
		super.sonarSetUp();
		curVal = new DistanceObservable( curVal );		
		//Colors.out("SonarObservableMock | sonarSetUp curVal="+curVal.getVal());
	}
	
 	@Override
	public void register(IObserver obs) {
		//try {
			((DistanceObservable)curVal).addObserver(obs);		
//		}catch(Exception e) {
//			Colors.outerr("SonarObservableMock | register ERROR="+e.getMessage());
//		}
	}

	@Override
	public void unregister(IObserver obs) {
		((DistanceObservable)curVal).deleteObserver(obs);		
	}
  
}
