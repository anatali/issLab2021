package it.unibo.comm2022.interfaces;

public interface IContext {
	public void addComponent( String name, IApplMsgHandler h);
	public void removeComponent( String name );
	public void activate();
	public void deactivate();
}