package it.unibo.enablerCleanArch.domain;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class SonarObservableModel extends SonarModel implements ISonarObservable{

//private Observable observableSupport = new Observable(); 

//Factory methods
public static ISonar create() {
	if( RadarSystemConfig.simulation )  return createSonarMock();
	else  return createSonarConcrete();		
}

public static ISonarObservable createSonarObservableMock() {
	Colors.out("createSonarMock");
	return new SonarObservableMock();
}	
public static ISonarObservable createSonarObservableConcrete() {
	Colors.out("createSonarConcrete");
	return null;
}	

//From ISonar	
	@Override
	protected synchronized void valueUpdated( ){
		super.valueUpdated();
		setChanged(); //not visible
		notifyObservers(curVal);
	}
	protected abstract void sonarSetUp() ;
	protected abstract void sonarProduce() ;

//From ISonarObservable	
	@Override
	public void register( IObserver obs ) {
		addObserver( obs );
	}
	@Override
	public void unregister( IObserver obs ) {
		deleteObserver( obs );
	}
	


}
