package it.unibo.enablerCleanArch.supports;

 
import it.unibo.enablerCleanArch.domain.ApplMessage;
 

public interface IApplMsgHandler  {
	public String getName(); 
	public void elaborate( String message, Interaction2021 conn ) ;	
	public void elaborate( ApplMessage message, Interaction2021 conn );//ESTENSIONE dopo Context
	public void sendMsgToClient( String message, Interaction2021 conn );
	public void sendAnswerToClient( String message, Interaction2021 conn  );
}
