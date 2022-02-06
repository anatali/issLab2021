package it.unibo.enablerCleanArch.supports.http;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import it.unibo.enablerCleanArch.supports.ColorsOut;

public class HttpClientSupport {
	private CloseableHttpClient httpclient;
    private  String URL  = "unknown";
    
    public HttpClientSupport( String url ){
        httpclient = HttpClients.createDefault();
        URL        = url;
        ColorsOut.out( "HttpClientSupport | created for url=" + url  );
    }
    
    public void forward( String msg)  {
        ColorsOut.out( "HttpClientSupport | forward:" + msg  );
        performrequest(msg);
    }   
    
    public void request( String msg) {
        ColorsOut.out( "HttpClientSupport | request:" + msg  );
        performrequest(msg);    //the answer is lost
    }
    public void reply(String msg) {
        ColorsOut.out( "HttpClientSupport | WARNING: reply NOT IMPLEMENTED"  );
    }
    
    public String requestSynch( String msg) {
        //Colors.out( "HttpClientSupport | requestSynch:" + msg  );
        return performrequest(msg);    //the answer is lost
    }   
  //===================================================================

    protected String performrequest( String msg )  {
        //https://www.tutorialspoint.com/apache_httpclient/apache_httpclient_quick_guide.htm
    	//https://hc.apache.org/httpcomponents-client-5.1.x/quickstart.html
        try {
        	//URL = URL+"/photo";
        	URL = URL+msg;
            ColorsOut.out( "HttpClientSupport | performrequest:" + msg + " URL=" + URL );
            StringEntity entity     = new StringEntity(msg);
            HttpUriRequest httppost = RequestBuilder.post()
                    .setUri(new URI(URL))
                    .addParameter("Name", "pi").addParameter("password", "unibo")
                    //.addParameter("request", "todo")
                    //.setHeader("Content-Type", ContentType.TEXT_PLAIN.getMimeType())
                    //.setHeader("Accept", "application/json")
                    .setEntity(entity)
                    .build();
            CloseableHttpResponse response = httpclient.execute(httppost);
//            Colors.out( "HttpClientSupport | response_a:" + response  );
//            Colors.out( "HttpClientSupport | response_b:" + response.getEntity()   );
//            Colors.out( "HttpClientSupport | response_c:" + response.getEntity().getContent()  );
            String answer = IOUtils.toString(response.getEntity().getContent(), "UTf-8");
            ColorsOut.out( "HttpClientSupport | answer:" + answer  );
           
            return response.getEntity().getContent().toString();
            /*
            String jsonStr = EntityUtils.toString( response.getEntity() );
            JSONObject jsonObj = new JSONObject(jsonStr) ;
            if( jsonObj.get("endmove") != null ) {
                endmove = jsonObj.getBoolean("endmove");
                //Colors.out("IssHttpSupport | response=" + endmove);
            }*/
        } catch(Exception e){
            ColorsOut.out("HttpClientSupport | ERROR:" + e.getMessage());
         }
        return "sorry ... " ;
    }
   
}
