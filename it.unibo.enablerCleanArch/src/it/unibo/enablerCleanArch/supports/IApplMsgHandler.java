package it.unibo.enablerCleanArch.supports;

public interface IApplMsgHandler {
	//public  void elaborate(String message) ;
	public String getName(); 
	public  void elaborate( String message, Interaction2021 conn ) ;
	 
}
