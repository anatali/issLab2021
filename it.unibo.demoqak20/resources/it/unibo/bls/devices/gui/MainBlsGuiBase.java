package it.unibo.bls.devices.gui;

import it.unibo.bls.appl.BlsApplicationLogic;
import it.unibo.bls.appl.MainBlsAppl;
import it.unibo.bls.components.ButtonObserver;
import it.unibo.bls.interfaces.IAppLogic;
import it.unibo.bls.interfaces.IApplListener;
import it.unibo.bls.interfaces.IButton;
import it.unibo.bls.interfaces.IButtonObservable;
import it.unibo.bls.interfaces.ILed;
import it.unibo.bls.interfaces.IObservable;

public class MainBlsGuiBase   {
 	public static MainBlsGuiBase createTheSystem(){
 		return new MainBlsGuiBase();	//calls the super costructor
 	}

 	public static void main(String[] args) {
		ILed led              		= LedAsGui.createLed(  );
		IButtonObservable button	= ButtonAsGui.createButton("click");
		IApplListener bobs    		= ButtonObserver.createButtonListener();
		IAppLogic applLogic   		= BlsApplicationLogic.create();
		
		bobs.setControl( applLogic );
		applLogic.setControlled(led);
		
		button.addObserver( bobs );	//starts the job
		
 	}
}
