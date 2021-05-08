package it.unibo.bls.interfaces;

public interface IAppLogic {
    public void setControlled(ILed obj);
    public void execute(String cmd);
    public int getNumOfCalls(); //useful for testing
}
