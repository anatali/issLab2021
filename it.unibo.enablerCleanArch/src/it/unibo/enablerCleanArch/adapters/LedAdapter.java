package it.unibo.enablerCleanArch.adapters;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedConcrete;
import it.unibo.enablerCleanArch.domain.LedMock;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;

/*
 * Adapter per led remoto
 */
public class LedAdapter implements ILed{
private ILed led;

	public LedAdapter(ILed led) {
		if( RadarSystemConfig.ledRemote)  {
			//led = LedMock.create();
		}else {
			this.led = led;
		}
	}
	@Override
	public void turnOn() { led.turnOn(); }

	@Override
	public void turnOff() { led.turnOff(); }

	@Override
	public boolean getState() { return led.getState(); }

}
