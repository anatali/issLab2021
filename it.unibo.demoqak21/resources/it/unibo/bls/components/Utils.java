package it.unibo.bls.components;

import it.unibo.bls.devices.gui.LedAsGui;
import it.unibo.bls.interfaces.ILed;

public class Utils {
	
	public static ILed createLed() {
		return LedAsGui.createLed();
	}

	public static void showSystemInfo(){
		System.out.println(
			"COMPUTER | memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		System.out.println(
			"AT START | num of threads="+ Thread.activeCount() +" currentThread=" + Thread.currentThread() );
		System.out.println("----------------------------------------------------------------");
	}

 
	public static void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
