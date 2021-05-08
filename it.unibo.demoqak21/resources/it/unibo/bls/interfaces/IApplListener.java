package it.unibo.bls.interfaces;


public interface IApplListener extends IObserver{
    public void setControl(IAppLogic ctrl);
    public int getNumOfClicks();
}
