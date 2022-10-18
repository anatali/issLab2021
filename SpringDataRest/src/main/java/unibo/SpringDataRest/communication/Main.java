package unibo.SpringDataRest.communication;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class Main {
//https://serversmtp.com/it/smtp-gmail-configurazione/
    public static void main(String[] args) {
        try {
            HtmlEmail email = new HtmlEmail();
            String user = "agoognat@gmail.com";
            String pwd  = "hopewell2021";
            email.setSmtpPort(25); //587 465
            email.setAuthenticator(new DefaultAuthenticator(user, pwd));
            email.setDebug(true);
            email.setHostName("smtp.gmail.com");
            email.getMailSession().getProperties().put("mail.smtps.auth", "true");
            email.getMailSession().getProperties().put("mail.debug", "true");
            email.getMailSession().getProperties().put("mail.smtps.port", "465");  //587
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.port",
                    "465");//587
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            email.getMailSession().getProperties().put("mail.smtps.socketFactory.fallback",
                    "false");
            email.getMailSession().getProperties().put("mail.smtp.starttls.enable",
                    "true");
            email.addTo("agoognat@gmail.com", "Nome");
            email.setFrom(user, "Me");
            email.setSubject("Email di prova in HTML");
            URL url = new URL("https://www.mattepuffo.com/blog/images/logo.png");
            String cid = email.embed(url, "Apache logo");
            email.setHtmlMsg("Mattepuffo logo - ");
            email.setTextMsg("Il tuo client non supporta le email in HTML");
            email.send();
        } catch (EmailException | MalformedURLException ex) {
            System.out.println(ex.getMessage());
        }

    }

}