/**
 * ConsoleGui
 * @author AN - DISI - Unibo
 * ===============================================================
 * The user hoits a button and a message with the same name is
 * sent to the WEnv by using WEnvConnSupportNoChannel.sendMessage
 * ===============================================================
 */
package demo

import it.unibo.supports2021.ActorBasicJava
import kotlin.jvm.JvmStatic
import consolegui.GuiUtils
import consolegui.ButtonAsGui
import java.util.*

class ConsoleGuiActor : ActorBasicJava("userConsole"), Observer {
    //Observer deprecated in 11 WHY?
    private val buttonLabels = arrayOf("STOP", "RESUME")

    //Observer
    override fun update(o: Observable, arg: Any) {    //Observable deprecated WHY?
        val move = arg.toString()
        //System.out.println("GUI input move=" + move);
        val robotCmd = if (move === "STOP") "{\"robotcmd\":\"STOP\" }" else "{\"robotcmd\":\"RESUME\" }"
        //System.out.println("GUI input robotCmd=" + robotCmd );
        //controller.handleInfo( robotCmd );
        updateObservers(robotCmd)
    }

    override fun handleInput(s: String) {}

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            ConsoleGuiActor()
        }
    }

    init {
        GuiUtils.showSystemInfo()
        val concreteButton = ButtonAsGui.createButtons("", buttonLabels)
        concreteButton.addObserver(this)
    }
}