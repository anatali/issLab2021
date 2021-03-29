/**
 * ConsoleGuiActorKotlin
 * @author AN - DISI - Unibo
===============================================================
An 'hybrid' that should be replaced by a full kotlin version
===============================================================
 */
package it.unibo.consolegui;

import it.unibo.actor0.ActorBasicKotlin;
import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.MsgUtil;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

public class ConsoleGuiActorKotlin extends ActorBasicKotlin implements  Observer{
private String[] buttonLabels  = new String[]  { "STOP", "RESUME" };

	public ConsoleGuiActorKotlin( ) {
		super("userConsole");
		GuiUtils.showSystemInfo();
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
 	}

 	@Override //Observer
	public void update( Observable o , Object arg ) {	//Observable deprecated WHY?
		String move = arg.toString();
		ApplMessage guiCmd = MsgUtil.buildDispatch(getName(), "robotcmd", move.toLowerCase(Locale.ROOT), "any") ;


		System.out.println("GUI guiCmd=" + guiCmd);
		ApplMessage test = ApplMessage.Companion.create(guiCmd.toString());
		System.out.println("GUI test=" + test);

		doupdateObservers( guiCmd );
	}

	@Nullable
	@Override
	protected Object handleInput(@NotNull ApplMessage applMessage,
								 @NotNull Continuation<? super Unit> continuation) {
		System.out.println("GUI guiCmd applMessage = " + applMessage);
		return null;
	}

	public static void main( String[] args) {
		new ConsoleGuiActorKotlin(   );
	}



}

