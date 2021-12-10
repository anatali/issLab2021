package it.unibo.enablerCleanArch.supports;

public class SysMessageHandler extends ApplMessageHandler{
private 	ApplMessageHandler userDefinedHandler;
private 	Interaction2021 conn;

	public SysMessageHandler( ApplMessageHandler userDefinedHandler, Interaction2021 conn) {
		super( "sysh_"+count++);
		this.userDefinedHandler = userDefinedHandler;
		this.conn               = conn;
	}

	@Override
	public void elaborate(String message) {
		Colors.out("SysMessageHandler | elaborate message=" + message);
		userDefinedHandler.elaborate( message, conn );		
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		//Colors.out("SysMessageHandler | elaborate message=" + message + " conn=" + conn);
		userDefinedHandler.elaborate( message, conn );
	}




}
