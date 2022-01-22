package it.unibo.enablerCleanArch.supports;

import java.net.URI;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientSupport {
	private CloseableHttpClient httpclient;
    private  String URL  = "unknown";
    
    public HttpClientSupport( String url ){
        httpclient = HttpClients.createDefault();
        URL        = url;
        Colors.out( "HttpClientSupport | created for url=" + url  );
    }
    
    public void forward( String msg)  {
        Colors.out( "HttpClientSupport | forward:" + msg  );
        performrequest(msg);
    }   
    
    public void request( String msg) {
        Colors.out( "HttpClientSupport | request:" + msg  );
        performrequest(msg);    //the answer is lost
    }
    public void reply(String msg) {
        Colors.out( "HttpClientSupport | WARNING: reply NOT IMPLEMENTED"  );
    }
    
    public String requestSynch( String msg) {
        //Colors.out( "HttpClientSupport | requestSynch:" + msg  );
        return performrequest(msg);    //the answer is lost
    }   
  //===================================================================

    protected String performrequest( String msg )  {
        
        try {
            //Colors.out( "HttpClientSupport | performrequest:" + msg + " URL=" + URL );
            StringEntity entity     = new StringEntity(msg);
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    //.setHeader("Content-Type", "application/json")
                    //.setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();
            CloseableHttpResponse response = httpclient.execute(httppost);
            Colors.out( "HttpClientSupport | response:" + response  );
            return response.getEntity().getContent().toString();
            /*
            String jsonStr = EntityUtils.toString( response.getEntity() );
            JSONObject jsonObj = new JSONObject(jsonStr) ;
            if( jsonObj.get("endmove") != null ) {
                endmove = jsonObj.getBoolean("endmove");
                //Colors.out("IssHttpSupport | response=" + endmove);
            }*/
        } catch(Exception e){
            Colors.out("HttpClientSupport | ERROR:" + e.getMessage());
         }
        return "sorry ... " ;
    }
   
}
