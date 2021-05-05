package resources.bls.kotlin

import it.unibo.bls.interfaces.ILed

object led{
	fun create() : ILed{
		return it.unibo.bls.devices.gui.LedAsGui.createLed()
	}
}