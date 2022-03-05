package it.unibo.actorComm.interfaces;

import it.unibo.kactor.IApplMessage;

public interface IApplInterpreter {
	public String elaborate( IApplMessage message );
	public String elaborate( String message );
}
