package it.unibo.enablerCleanArch.supports.coap;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import it.unibo.enablerCleanArch.domain.WebCamRasp;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.HttpClientSupport;

public class WebCamRaspResourceCoap extends CoapDeviceResource {
 	
	public WebCamRaspResourceCoap( String name  ) {
		super(name, DeviceType.input);  //add the resource
		//WebCamRasp.startWebCamStream();  //Forse non prende foto ???
  	}

	@Override
	protected String elaborateGet(String req) {
		Colors.out( getName() + " | before elaborateGet req:" + req   );
		if( req.startsWith("getImage-")) {
			String fname=req.substring( req.indexOf('-')+1, req.length());
			String imgBase64 = WebCamRasp.getImage(fname);
			Colors.out(getName() + " | imgBase64 length=" + imgBase64.length());
			return imgBase64;
		}
  		return "nothing to to" ;
	}

	@Override
	protected void elaboratePut(String req) {
		Colors.out( getName() + " | before elaboratePut req:" + req   );
		 if( req.startsWith("takePhoto-") ){
			String fname=req.substring( req.indexOf('-')+1, req.length());
			Colors.out( getName() + " | takePhoto fname:" + fname   );
			WebCamRasp.takePhoto(fname);
		}else if( req.startsWith("startWebCamStream") ){
			WebCamRasp.startWebCamStream();
		}
 		 
 	}  
	
	@Override
	protected void elaboratePut(String req, InetAddress callerAddr) {
		Colors.out( getName() + " | before elaboratePut req:" + req + " callerAddr="  + callerAddr  );
		 if( req.startsWith("takePhoto-") ){
			String fname=req.substring( req.indexOf('-')+1, req.length());
			WebCamRasp.takePhoto(fname);
			
			String replyAddr=callerAddr.getHostAddress();
			sendPhotoHttp(replyAddr);
			
			//Colors.out( getName() + " | takePhoto fname:" + fname    );
		}else if( req.startsWith("startWebCamStream") ){
			WebCamRasp.startWebCamStream();
		}		
	}
	
	/* executed on Rasp */
	protected void sendPhotoHttp(String replyAddr) { //HttpClient
		try {
			String request = "http://"+replyAddr+"/photo?p1=a";
			URL url = new URL(request); 
			Colors.out( getName() + " |  sendPhotoHttp replyAddr=" + replyAddr + " sendPhotoHttp url:" + url  );
			
			HttpClientSupport httpSup = new HttpClientSupport( "http://"+replyAddr+":8081" );
			
			String answer = httpSup.requestSynch("photo");
			
			Colors.out( getName() + " |  sendPhotoHttp answer=" + answer  );
			
			/*
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); 
		    connection.setInstanceFollowRedirects(false); 
		    connection.setRequestMethod("PUT"); 
		    connection.setRequestProperty("Content-Type", "text/plain"); 
		    connection.setRequestProperty("charset", "utf-8");
		    //connection.connect();
			
		    
	           	HttpClient client = HttpClientBuilder.create().build();
	            HttpGet request1 = new HttpGet(""+url);
	            HttpResponse response = client.execute(request1);
//	            System.out.println( "RESPONSE=" + response.getEntity().getContent());
	            String answer = IOUtils.toString(response.getEntity().getContent(), "UTf-8");
	            System.out.println( "RESPONSE=" + answer );
	            
	            
	            CloseableHttpClient client1 = HttpClients.createDefault();
	            HttpPost httpPost = new HttpPost("http://"+replyAddr);

	            List<NameValuePair> params = new ArrayList<NameValuePair>();
	            params.add(new BasicNameValuePair("username", "John"));
	            params.add(new BasicNameValuePair("password", "pass"));
	            httpPost.setEntity(new UrlEncodedFormEntity(params));

	            CloseableHttpResponse response1 = client1.execute(httpPost);
	            //assertThat(response1.getStatusLine().getStatusCode(), equalTo(200));
	            client1.close();
	            */
			
				

	            
		}catch( Exception e ) {
			Colors.outerr(getName() + " | sendPhotoHttp ERROR" + e.getMessage());
		}
	}
}
