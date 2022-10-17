package unibo.SpringDataRest.callers;

import javax.swing.text.Element;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.StringReader;

public class PageUtil {
    public static String readTheHtmlPage(String htmlString, String elementID){
        String answer = "error";
        try {
            HTMLEditorKit htmlEditKit = new HTMLEditorKit();
            HTMLDocument htmlDocument = new HTMLDocument();
            try {
                htmlEditKit.read(new StringReader( htmlString ), htmlDocument, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Element foundField  = htmlDocument.getElement(elementID);
            int start  = foundField.getStartOffset();
            int length = foundField.getEndOffset() - start;
            //System.out.println("readTheHtmlPage foundField:"  + start + " " + length);
            answer  = foundField.getDocument().getText(start,length);
            //System.out.println( "!!! readTheHtmlPage answer=" + answer );
            //System.out.println("title="+htmlDocument.getProperty("title")); //
        } catch( Exception e){
            System.out.println( "!!! readTheHtmlPage ERROR:"+e.getMessage());
            answer="PageUtil error:"+e.getMessage();
        }
        return answer;
    }
}
