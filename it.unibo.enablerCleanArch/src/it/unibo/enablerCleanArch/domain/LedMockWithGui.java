package it.unibo.enablerCleanArch.domain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;

import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class LedMockWithGui extends LedMock {  
private Panel p ; 
private Frame frame;
private final Dimension sizeOn  = new Dimension(100,100);
private final Dimension sizeOff = new Dimension(30,30);

	public static ILed createLed(  ){
		LedMockWithGui led = new LedMockWithGui(Utils.initFrame(150,150));
		led.turnOff();
		return led;
	}
//	public static ILed createLed( Frame frame){
//		LedMockWithGui led = new LedMockWithGui(frame);
//		led.turnOff();
//		return led;
//	}
	//Constructor
	public LedMockWithGui( Frame frame ) {
		super();
		Colors.out("create LedMockWithGui");
		this.frame = frame;
 		configure( );
  	}	
	protected void configure( ){
		p = new Panel();
//		p.setSize( sizeOff );
//		p.validate();
		p.setBackground(Color.red);
//		p.validate();
		frame.add(BorderLayout.CENTER,p);
		turnOff();
  	}
	@Override //LedMock
	public void turnOn(){
		super.turnOn();
		p.setSize( sizeOn );
		p.setBackground(Color.red);
		p.validate();
		//frame.setSize(sizeOn);
	}
	@Override //LedMock
	public void turnOff() {
		super.turnOff();
 		p.setSize( sizeOff );
		p.setBackground(Color.gray);
		p.validate();
		//frame.setSize(sizeOff);
	}
}
