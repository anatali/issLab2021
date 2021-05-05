package it.unibo.bls.devices.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import it.unibo.bls.interfaces.IButton;
import it.unibo.bls.interfaces.IButtonObservable;
import it.unibo.bls.interfaces.IObserver;
 

/*
Java does not support multiple inheritance among classes.
*/
public class ButtonAsGui extends Observable implements IButton, IButtonObservable, ActionListener{
//Factory method
public static IButtonObservable  createButton(  String cmd  ){
	IButtonObservable  btnListener = new ButtonAsGui();
 	new ButtonBasic(FrameUtils.initFrame(300,300), cmd,  (ActionListener) btnListener);	//ButtonAsGui is the listener
	return btnListener;
}
	@Override //from Observable and IButtonObservable
	public void addObserver(IObserver observer) {
		super.addObserver(observer);
	}
	@Override  //from ActionListener
	public void actionPerformed(ActionEvent e) {
		//System.out.println("	ButtonAsGui | actionPerformed ..." + e.getActionCommand() );
		this.setChanged();
		this.notifyObservers(e.getActionCommand());
	}
}
