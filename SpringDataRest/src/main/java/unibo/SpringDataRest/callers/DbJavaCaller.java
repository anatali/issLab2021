package unibo.SpringDataRest.callers;
import com.squareup.okhttp.*;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.StringReader;


public class DbJavaCaller {
    final OkHttpClient client = new OkHttpClient();
    final String BASE_URL     = "http://localhost:8080";

    private static void checkImages() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                HTMLEditorKit c  = new HTMLEditorKit();
                HTMLDocument doc = new HTMLDocument();

                try {
                    c.read(new StringReader("<HTML><TITLE>Test</TITLE><BODY><IMG id=test></BODY></HTML>"), doc, 0);
                } catch (Exception e) {
                    throw new RuntimeException("The test failed", e);
                }

                Element elem = doc.getElement("test");
                System.out.println("checkImages ="+doc.getProperty("TITLE"));
                System.out.println("checkImages="+elem);
                ImageView iv = new ImageView(elem);

                if (iv.getLoadingImageIcon() == null) {
                    throw new RuntimeException("getLoadingImageIcon returns null");
                }

                if (iv.getNoImageIcon() == null) {
                    throw new RuntimeException("getNoImageIcon returns null");
                }
            }
        });
    }
    private void readTheHtmlPage(String htmlString){
        try {
            //String htmlString = "<html><head><title>Example Title</title></head><body>Some text...</body></html>";
            HTMLEditorKit htmlEditKit = new HTMLEditorKit();
            /*
            HTMLDocument htmlDocument = (HTMLDocument) htmlEditKit.createDefaultDocument();
            HTMLEditorKit.Parser parser = new ParserDelegator();
            parser.parse(
                    new StringReader(htmlString),
                    htmlDocument.getReader(0),
                    true);*/
            HTMLDocument htmlDocument = new HTMLDocument();

            try {
                htmlEditKit.read(new StringReader( htmlString ), htmlDocument, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Use HTMLDocument here
            Element elem = htmlDocument.getElement("BOX");
            System.out.println("checkImages attributes="+elem.getElementCount() );
            System.out.println("checkImages element="+elem.getElementIndex(0) );

            System.out.println("title="+htmlDocument.getProperty("title")); //
            System.out.println("BOX element="+htmlDocument.getElement("BOX")); //
            System.out.println("BOX element="+htmlDocument.getElement("BOX")); //ù
            System.out.println("BOX="+htmlDocument.getProperty("BOX")); //

            //checkImages();

        } catch( Exception e){
             e.printStackTrace();
        }
    }
    public void runGet(String lastName){
        System.out.println("--------- runGet");
        String response =  doGet("http://localhost:8080/Api/getAPerson?lastName="+lastName);
        System.out.println(response);
        readTheHtmlPage(response);
    }

    public void runPost() {
        String json =
        "{\"category\": \"category/2\",\"name\": \"box\",\"code\": \"004\",\"title\": \"box\", \"description\": \"new box\", \"imgUrl\": \"\", \"price\": 35}";
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
        String response = doPost(BASE_URL + "/products",body);
        System.out.println("testPost response="+response);
    }
    String doPost(String urlStr, RequestBody body)  {
        try{
            Request request = new Request.Builder()
                .url(urlStr)
                    //.addHeader("Authorization", Credentials.basic("username", "password"))
                .post(body)
                .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            return response.body().string();
        }catch(Exception e){
            return "error: " +e.getMessage();
        }
    }

    String doGet(String url)  {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try{
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch(Exception e){
            return "error: " +e.getMessage();
        }
    }

    public static void main(String[] args)  {
        DbJavaCaller appl = new DbJavaCaller();
        //String response = example.run("https://raw.github.com/square/okhttp/master/README.md");
        appl.runGet("Manzoni");
        //appl.runPost();
      }
}
/*
From https://www.geeksforgeeks.org/java-program-to-extract-content-from-a-html-document/
Using FileReader
Using the Url.openStream()
 */