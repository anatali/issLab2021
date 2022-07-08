package unibo.testqakexample;


import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;


public class TrolleyPosObserver implements CoapHandler{

    @Override
    public void onLoad(CoapResponse response) {
        ColorsOut.outappl("TrolleyPosObserver changed:" + response.getResponseText(), ColorsOut.MAGENTA);
    }

    @Override
    public void onError() {
        ColorsOut.outerr("CoapObserver observe error!");
    }
}
