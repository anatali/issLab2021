package it.unibo.enablerCleanArch.domain;

public interface IApplInterpreter {
	public String elaborate( ApplMessage message );
	public String elaborate( String message );
}
