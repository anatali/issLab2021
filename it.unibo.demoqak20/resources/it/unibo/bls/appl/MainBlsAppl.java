package it.unibo.bls.appl;

import it.unibo.bls.components.Utils;
import it.unibo.bls.devices.*;
import it.unibo.bls.interfaces.*;
//import it.unibo.bls.kotlin.applLogic.BlsApplicationLogic;	//KOTLIN IMPL
import it.unibo.bls.components.ButtonObserver;


public class MainBlsAppl  {
	 
	protected IButtonObservable btn;
	protected ILed led;
	protected IApplListener buttonObserver;
	protected IAppLogic applLogic;
	protected DeviceFactory devFactory = new DeviceFactory();
	protected LedType ledType ;
	protected ButtonType buttonType;

//Factory method
  	public static MainBlsAppl createTheSystem(){
  		return new MainBlsAppl();
 	}
//Not-visible Constructor
 	protected MainBlsAppl( ) {
		Utils.showSystemInfo();
		setDeviceTypes();
		createTheComponents();
		connectTheComponents();
		startTheSystem();
 	}
 	protected void setDeviceTypes(){
//		ledType    = LedType.LedMockObj;
//		buttonType = ButtonType.ButtonMockObj;
		ledType    = LedType.LedGuiObj;
		buttonType = ButtonType.ButtonGuiObj;
 		
	}
 	protected void createTheComponents(){
  		led       = devFactory.createLed( ledType );
		applLogic = new BlsApplicationLogic();
 		btn       = devFactory.createButton( buttonType );
 		led.turnOff();
		buttonObserver = ButtonObserver.createButtonListener( );
	}
	protected void connectTheComponents(){
		buttonObserver.setControl( applLogic );
		applLogic.setControlled(led);
	}

	protected void startTheSystem(){
  		btn.addObserver( buttonObserver );	//starts the job
	}

	//Selectors (introduced for testing)
 	public IButtonObservable getButton(){
		return btn;
	}
	public ILed getLed(){
		return led;
	}
	public IApplListener getButtonObserver(){
		return buttonObserver;
	}
	public IAppLogic getApplLogic(){
  		return applLogic;
	}
 
//MAIN
 
	public static void main(String[] args) {
   		MainBlsAppl sys = createTheSystem();
 	}
 	 
}