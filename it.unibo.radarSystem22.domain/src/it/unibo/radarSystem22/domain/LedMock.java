package it.unibo.radarSystem22.domain;

import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.interfaces.ILed;

public class LedMock extends LedModel implements ILed{

	@Override
	protected void ledActivate(boolean val) {	
		showState();
	}

	protected void showState(){
		ColorsOut.outappl("LedMock state=" + getState(), ColorsOut.MAGENTA );
	}
} 