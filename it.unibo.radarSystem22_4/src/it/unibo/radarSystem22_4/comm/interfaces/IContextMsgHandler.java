package it.unibo.radarSystem22_4.comm.interfaces;


public interface IContextMsgHandler {
	public void addComponent( String name, IApplMsgHandler h);
	public void removeComponent( String name );
	public IApplMsgHandler getHandler( String name );
}
