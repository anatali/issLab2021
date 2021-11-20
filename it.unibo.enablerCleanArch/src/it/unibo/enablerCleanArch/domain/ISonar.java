package it.unibo.enablerCleanArch.domain;

public interface ISonar {
	//public int receive();		//genera valori su un 'canale'
	public int getVal();		//introdotto per permettere polling
}
