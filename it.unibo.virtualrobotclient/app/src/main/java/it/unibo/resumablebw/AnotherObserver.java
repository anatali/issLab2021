package it.unibo.resumablebw;
import it.unibo.interaction.IssObserver;
import org.json.JSONObject;

public class AnotherObserver implements IssObserver {
    @Override
    public void handleInfo(String info) {
        System.out.println("AnotherObserver | " + info);
        if( info.contains("sonarName")) return;
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleInfo(JSONObject info) {
        handleInfo( info.toString() );
    }
}
