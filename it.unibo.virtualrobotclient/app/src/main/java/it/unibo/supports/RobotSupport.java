/*
===============================================================
RobotSupport.java
Utility class
===============================================================
*/
package it.unibo.supports;

import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URI;

public class RobotSupport {
    private IssCommSupport rs;

    public RobotSupport(IssCommSupport rs){
        this.rs = rs;
    }

    public void request(String jsonMoveStr ) {
        doRobotAsynchMove(jsonMoveStr,rs);
    }

    public void close(){
        rs.close();
    }
    public static String doBoundary(int stepNum, String journey, IssOperations rs) {
        if (stepNum > 4) {
            return journey;
        }
        String answer = rs.requestSynch( MsgRobotUtil.wMsg );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = rs.requestSynch( MsgRobotUtil.wMsg );
        }
        //collision
        rs.requestSynch(MsgRobotUtil.lMsg);
        return doBoundary(stepNum + 1, journey + "l", rs);
    }

    //Utility
    public static void doRobotAsynchMove(String jsonMoveStr, IssOperations rs) {
        System.out.println(jsonMoveStr);
        //"{\"robotmove\":\"...\", \"time\": ...}";
        JSONObject jsonObj = new JSONObject(jsonMoveStr);
        int time = Integer.parseInt( jsonObj.get("time").toString() );
        rs.forward( jsonMoveStr );
        try { Thread.sleep(time+100); } catch (InterruptedException e) { e.printStackTrace(); }
        //The answer is handled by the controllers
    }

    public static boolean sendHttpCmd(String URL, String move, int time)  {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            System.out.println( move + " sendCmd "  );
            //String json         = "{\"robotmove\":\"" + move + "\"}";
            String json         = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
            StringEntity entity = new StringEntity(json);
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    .setHeader("Content-Type", "application/json")
                    .setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();
            CloseableHttpResponse response = httpclient.execute(httppost);
            //System.out.println( "ClientUsingPost | sendCmd response= " + response );
            boolean collision = checkCollision(response);
            //boolean collision = checkCollision_javax(response);
            return collision;
        } catch(Exception e){
            System.out.println("ERROR:" + e.getMessage());
            return true;
        }
    }

    protected static boolean checkCollision(CloseableHttpResponse response) throws Exception {
        try{
            //response.getEntity().getContent() is an InputStream
            String jsonStr = EntityUtils.toString( response.getEntity() );
            System.out.println( "ClientUsingPost | checkCollision_simple jsonStr= " +  jsonStr );
            //jsonStr = {"endmove":true,"move":"moveForward"}
            //org.json.simple.parser.JSONParser simpleparser = new JSONParser();
            //org.json.simple.JSONObject jsonObj              = (JSONObject) simpleparser.parse( jsonStr );
            JSONObject jsonObj = new JSONObject(jsonStr) ;
            boolean collision = false;
            if( jsonObj.get("endmove") != null ) {
                collision = ! jsonObj.get("endmove").toString().equals("true");
                System.out.println("ClientUsingPost | checkCollision_simple collision=" + collision);
            }
            return collision;
        }catch(Exception e){
            System.out.println("ClientUsingPost | checkCollision_simple ERROR:" + e.getMessage());
            throw(e);
        }
    }


}
