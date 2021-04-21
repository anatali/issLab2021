/**
 * ConsoleGui
 * @author AN - DISI - Unibo
===============================================================
The user hoits a button and a message with the same name is
sent to the WEnv by using WEnvConnSupportNoChannel.sendMessage
===============================================================
 */
package consolegui;

import it.unibo.interaction.IUniboActor;
import it.unibo.supports2021.ActorBasicJava;
import org.jetbrains.annotations.NotNull;

import java.util.Observable;
import java.util.Observer;

public class ConsoleGuiActor extends ActorBasicJava implements  Observer{	//Observer deprecated in 11 WHY?
private String[] buttonLabels  = new String[]  { "STOP", "RESUME" };

	public ConsoleGuiActor( ) {
		super("userConsole");
		GuiUtils.showSystemInfo();
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
 	}

 	@Override //Observer
	public void update( Observable o , Object arg ) {	//Observable deprecated WHY?
		String move = arg.toString();
		//System.out.println("GUI input move=" + move);
		String robotCmd = (move == "STOP") ? "{\"robotcmd\":\"STOP\" }" : "{\"robotcmd\":\"RESUME\" }";
		//System.out.println("GUI input robotCmd=" + robotCmd );
		//controller.handleInfo( robotCmd );
		this.updateObservers( robotCmd );
	}
	
	public static void main( String[] args) {
		new ConsoleGuiActor(   );
	}

	@Override
	protected void handleInput(String s) {

	}

	@Override
	public void registerActor(@NotNull IUniboActor iUniboActor) {

	}

	@Override
	public void removeActor(@NotNull IUniboActor iUniboActor) {

	}
}

