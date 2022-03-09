package it.unibo.comm2022.interfaces;

public interface IContextMsgHandler extends IApplMsgHandler{
	public void addComponent( String name, IApplMsgHandler h);
	public void removeComponent( String name );
	public IApplMsgHandler getHandler( String name );
}