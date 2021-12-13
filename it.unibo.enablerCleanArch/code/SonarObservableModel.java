package it.unibo.enablerCleanArch.domain;

import java.util.Observable;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;

public abstract class SonarObservableModel extends SonarModel  implements ISonarObservable{

private SonarModel sonar  ; 

//Factory methods
public static ISonar create() {
	if( RadarSystemConfig.simulation )  return createSonarObservableMock();
	else  return createSonarObservableConcrete();		
}

public static ISonarObservable createSonarObservableMock() {
	Colors.out("createSonarObservableMock");
	return new SonarObservableMock();
}	
public static ISonarObservable createSonarObservableConcrete() {
	Colors.out("createSonarObservableConcrete");
	return null;
}	

//From ISonar	
	@Override
	protected synchronized void valueUpdated( ){
		sonar.valueUpdated();
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
