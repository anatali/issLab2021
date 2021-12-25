package it.unibo.enablerCleanArch.supports;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import it.unibo.enablerCleanArch.domain.ApplMessage;

public class Utils {
	public static final  ApplMessage sonarActivate     = new ApplMessage("msg( sonarcmd, dispatch, system, sonar, activate,    0)");
	public static final  ApplMessage sonarDeactivate   = new ApplMessage("msg( sonarcmd, dispatch, system, sonar, deactivate,  0)");
	public static final  ApplMessage getDistance       = new ApplMessage("msg( sonarcmd, request,  system, sonar, getDistance, 0)");
	public static final  ApplMessage isActive          = new ApplMessage("msg( sonarcmd, request,  system, sonar, isActive,    0)");

	public static final ApplMessage fardistance  = new ApplMessage("msg( distance, dispatch, system, sonar, 36, 0 )");
	public static final ApplMessage neardistance = new ApplMessage("msg( distance, dispatch, system, sonar, 10, 1 )");
	public static final ApplMessage turnOnLed    = new ApplMessage("msg( turn, dispatch, system, led, on, 2 )");
	public static final ApplMessage turnOffLed   = new ApplMessage("msg( turn, dispatch, system, led, off, 3 )");

 	public static final ApplMessage getLedState  = new ApplMessage("msg( ledcmd,   request,  system, led,   getState, 6 )");

	private ApplMessage radarUpdate  = new ApplMessage("msg( update, request,  system, radar, DISTANCE, 7 )");
	
	
	public static void showSystemInfo(){

		System.out.println(
			"COMPUTER | memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		System.out.println(
			"AT START | num of threads="+ Thread.activeCount() +" currentThread=" + Thread.currentThread() );
	}

	public static Frame initFrame(int dx, int dy){
 		Frame frame         = new Frame();
 		BorderLayout layout = new BorderLayout();
 		frame.setSize( new Dimension(dx,dy) );
 		frame.setLayout(layout);		
 		frame.addWindowListener(new WindowListener() {			
			@Override
			public void windowOpened(WindowEvent e) {}				
			@Override
			public void windowIconified(WindowEvent e) {}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);				
			}			
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		}); 	
		frame.setVisible(true);
		return frame;
		
	}
 	public static Frame initFrame(){
 		return initFrame(400,200);
 	}

	public static void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
