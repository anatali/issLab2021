package it.unibo.enablerCleanArch.supports.coap;

import java.io.File;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;

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
			
			String encodedString = WebCamRasp.getImage(fname);
			
			String replyAddr=callerAddr.getHostAddress();
			//sendPhotoHttp(replyAddr, encodedString);
			//sendPhotoHttp1( "http://"+replyAddr+":8081/photo", fname);
			sendPhotoHttp2( "http://"+replyAddr+":8081/photo", fname);
			
			//Colors.out( getName() + " | takePhoto fname:" + fname    );
		}else if( req.startsWith("startWebCamStream") ){
			WebCamRasp.startWebCamStream();
		}		
	}
	
	//https://www.baeldung.com/httpclient-post-http-request
	protected void sendPhotoHttp2( String url, String fname  )   {
		try {
			CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(url+"/?request=todo");
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.addTextBody("username", "John");
		    builder.addTextBody("password", "pass");
		    builder.addBinaryBody(
		      "image", new File(fname), ContentType.create("image/jpeg"), fname);
			//String encodedString = Base64.getEncoder().encodeToString(fileContent);

		    HttpEntity multipart = builder.build();
		    httpPost.setEntity(multipart);
		    CloseableHttpResponse response = client.execute(httpPost);
		    HttpEntity resEntity = response.getEntity();
		    if (resEntity != null) {
		    	Colors.out("resEntity not null");
		    	Colors.out(EntityUtils.toString(resEntity), Colors.BgYellow);
		    	EntityUtils.consume(resEntity) ;
		    }		    
	    }catch( Exception e) {
	    	Colors.outerr(getName() + " | sendPhotoHttp ERROR" + e.getMessage());
	    }
	}
	//https://www.codejava.net/frameworks/spring-boot/spring-boot-file-upload-tutorial
	protected void sendPhotoHttp1( String url, String fname  )   {
		try {
	    //HttpClient httpclient = new DefaultHttpClient();
	    HttpClient httpclient = HttpClientBuilder.create().build();
	    //httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

	    HttpPost httppost = new HttpPost(url+"/?request=todo"); //"http://localhost:9000/upload"
	    //File file = new File(fname); //"C:\\Users\\joao\\Pictures\\bla.jpg"

	    //MultipartEntity mpEntity = new MultipartEntity();
	    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
	    //ContentBody cbFile        = new FileBody(file, "image/jpeg");
	    //mpEntity.addPart("request", cbFile);
	    FileBody fileBody = new FileBody(new File(fname)); //image should be a String
	    builder.addPart("image", fileBody); 
	    //builder.addBinaryBody("image", fileBody);

	    //httppost.setEntity(mpEntity);
	    HttpEntity mpEntity = builder.build();
	    httppost.setEntity(mpEntity);
	    
	    Colors.out("sendPhotoHttp1 executing request "+ httppost.getRequestLine(), Colors.BgMagenta ); //
	    HttpResponse response = httpclient.execute(httppost);
	   
	    HttpEntity resEntity = response.getEntity();

 /*
 * https://stackoverflow.com/questions/9197745/what-exactly-is-an-http-entity
 * 
 * Entity is an optional payload inside an http message(either request or response), 
 * so it is a "part-whole" relation between Entity and Message.	 
 * 
 * It is an abstraction representing a request or response payload. 
 */
	    Colors.out(""+response.getStatusLine(), Colors.BgCyan);
	    if (resEntity != null) {
	    	Colors.out("resEntity not null");
	    	Colors.out(EntityUtils.toString(resEntity), Colors.BgMagenta);
	    }
	    if (resEntity != null) {
	    	Colors.out("resEntity consumeContent");
	      //resEntity.consumeContent();
	      EntityUtils.consume(resEntity) ;
	      //is deprecated so please use EntityUtils.consume(HttpEntity) when feasible.
	      //ensures that all the resources allocated to this entity are deallocated.
	    }

	    httpclient.getConnectionManager().shutdown();
	    }catch( Exception e) {
	    	Colors.outerr(getName() + " | sendPhotoHttp ERROR" + e.getMessage());
	    }

	}
	/* executed on Rasp */
	protected void sendPhotoHttp(String replyAddr, String encodedString) {  
		try {
 			
			HttpClientSupport httpSup = new HttpClientSupport( "http://"+replyAddr+":8081" );			
			String answer = httpSup.requestSynch("/photo/?request="+"encodedString");			
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
