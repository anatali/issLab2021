package it.unibo.enablerCleanArch.supports;

public interface Interaction2021  {	 
	public void forward(  String msg ) throws Exception;
 	public String receiveMsg(  ) throws Exception ;
	public void close( )  throws Exception;
}
