package it.unibo.bls.interfaces;


public interface IButtonListener extends IObserver{
    public void setControl(IControlLed ctrl);
    public int getNumOfClicks();
}
