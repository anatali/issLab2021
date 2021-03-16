/**
 * ConsoleGui
 * @author AN - DISI - Unibo
===============================================================
The user hoits a button and a message with the same name is
sent to the WEnv by using WEnvConnSupportNoChannel.sendMessage
===============================================================
 */
package it.unibo.consolegui;
//import it.unibo.interaction.WEnvConnSupportNoChannel;
import it.unibo.interaction.IssObserver;
import it.unibo.wenv.RobotInputController;

import java.util.Observable;
import java.util.Observer;

public class ConsoleGui implements  Observer{	//Observer deprecated in 11 WHY?
private String[] buttonLabels  = new String[]  { "START", "STOP" };//{"w", "s", "l", "r", "START", "STOP"};
//private WEnvConnSupportNoChannel wenvConn ;
private IssObserver controller ;

	public ConsoleGui(IssObserver controller) {
		GuiUtils.showSystemInfo();
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
		this.controller = controller;
 		//wenvConn      = new WEnvConnSupportNoChannel("localhost:8091", "600");
		//wenvConn.initConn("localhost:8091" );
 	}

	public void update( Observable o , Object arg ) {	//Observable deprecated WHY?
		String move = arg.toString();
		System.out.println("GUI input move=" + move);
		String robotCmd = (move == "STOP") ? "{\"robotcmd\":\"h\" }" : "{\"robotcmd\":\"w\" }";
		System.out.println("GUI input robotCmd=" + robotCmd );
		try {
			//wenvConn.sendMessage( move );
			controller.handleInfo( robotCmd );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main( String[] args) {
		new ConsoleGui(  new RobotInputController(null, true,false));
	}
}

