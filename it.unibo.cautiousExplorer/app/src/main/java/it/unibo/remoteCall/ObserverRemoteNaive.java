package it.unibo.remoteCall;

import org.json.JSONObject;

public class ObserverRemoteNaive extends AbstractRobotRemote{

    public ObserverRemoteNaive(String name) {
        super(name);
    }

    @Override
    protected void msgDriven(JSONObject infoJson) {
        System.out.println("    " + myname + " | " + infoJson);
    }
}
