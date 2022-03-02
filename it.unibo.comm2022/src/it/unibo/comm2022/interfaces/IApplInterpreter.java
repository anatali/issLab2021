package it.unibo.comm2022.interfaces;

public interface IApplInterpreter {
	public String elaborate( IApplMessage message );
	public String elaborate( String message );
}
