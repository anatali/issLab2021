package it.unibo.remoteCall;

import it.unibo.interaction.IUniboActor;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class ObserverRemoteNaive extends AbstractRobotRemote{

    public ObserverRemoteNaive(String name) {
        super(name);
    }

    @Override
    protected void msgDriven(JSONObject infoJson) {
        System.out.println("    " + myname + " | " + infoJson);
    }

    @Override
    public void registerActor(@NotNull IUniboActor iUniboActor) {

    }

    @Override
    public void removeActor(@NotNull IUniboActor iUniboActor) {

    }
}
