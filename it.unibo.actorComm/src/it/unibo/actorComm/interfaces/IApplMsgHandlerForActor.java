package it.unibo.actorComm.interfaces;

public interface IApplMsgHandlerForActor {
	public void sendMsgToClient( String message  );
	public void sendAnswerToClient( String message  );
}
