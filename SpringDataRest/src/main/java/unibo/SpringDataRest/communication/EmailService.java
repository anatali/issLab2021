package unibo.SpringDataRest.communication;

import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.URI;
import java.util.Scanner;

@Component
public class EmailService implements Mailer{
private Properties prop;
private String userName="";
private String userPswd="";
private boolean useTrueMail = false;

    public EmailService() {
        readUserData(  );
    }

    protected void readUserData(  )   {
        try {
            FileInputStream fis ;
            if( useTrueMail )
                fis = new FileInputStream("gmailData.txt");
            else
                fis   = new FileInputStream("mailtrapData.txt");
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            String[] items = line.split(",");
            userName=items[0];
            userPswd=items[1];
            System.out.println(
                    "EmailService readUserData userName:"+userName+" userPswd:"+userPswd);
        }catch(Exception e){
            System.out.println("EmailService readUserData ERROR:"+e.getMessage());
        }
    }

    public void sendMail( String msg, String destination ) {
        try {
            //appl.sendMailUsingGoogle("Mail to unibo usando smtp.gmail.com","antonio.natali@unibo.it");
            if( useTrueMail ) sendMailUsingGoogle(msg, destination);
            //appl.sendMailUsingMailTrap("Mail to unibo usando smtp.mailtrap.io","antonio.natali@unibo.it");
            else sendMailUsingMailTrap(msg,destination);
        }catch(Exception e){
            System.out.println("EmailService sendMail ERROR:"+e.getMessage());
        }
    }



    protected void sendMailUsingMailTrap(String msg, String destination){
        try {
            prop = new Properties();
            prop.put("mail.smtp.host", "smtp.mailtrap.io");
            prop.put("mail.smtp.port", 25);
            prop.put("mail.smtp.auth", true);
            //prop.put("mail.smtp.starttls.enable", "true");
            //prop.put("mail.smtp.ssl.trust", host);
            //prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, userPswd);
                }
            });
            sendMail(session, msg, destination);
        } catch (Exception e) {
            System.out.println("ERROR:"+e.getMessage());
        }
    }
    protected void sendMailUsingGoogle(String msg, String destination){
        try {
            prop = new Properties();
            //prop.setProperty("mail.debug", "true");
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", 25);  //25  587
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true"); //richiesto dal server gmail
            prop.put("mail.smtp.ssl.trust", "*"); //per evitare Could not convert socket to TLS

            //VISTI IN RETE:
            //prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            //prop.setProperty("mail.transport.protocol", "smtp");
            //prop.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    //Password set Google Mail  Password per le app
                    return new PasswordAuthentication(userName, userPswd);
                }
            });

             sendMail(session, msg, destination);

        } catch (Exception e) {
            System.out.println("ERROR:"+e.getMessage());
        }
    }
/*
    public void sendMailUsingGoogle(String msg, String destination) throws Exception {
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //Password set Google Mail  Password per le app
                return new PasswordAuthentication("agoognat@gmail.com", "bhthxbuswevuxfba");
            }
        });
        sendMail(session, msg, destination);
    }
    public void sendMailUsingMailTrap(String msg, String destination) throws Exception {
        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("9ed36bdf47bccb", "c84a76872c7592");
            }
        });
        sendMail(session, msg, destination);
    }
*/

    protected void sendMail(Session session, String msg, String destination ) throws Exception{
        Message message = new MimeMessage(session);
        //message.setFrom(new InternetAddress("fittizio@gmail.com"));
        //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("antonio.natali@unibo.it")); //InternetAddress.parse("to@gmail.com")
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destination)); //"agoognat@gmail.com"
        message.setSubject("MailOct19");
        message.setText(msg);
        System.out.println("SENDING MAIL:" + message.getContent());
        Transport.send(message);  //javax.mail.Service
        System.out.println("SENT MAIL:" + message.getContent());
       /*
       //UTILI IN FUTURO (per attachment)
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");
        String msgStyled = "This is my <b style='color:red;'>bold-red email</b> using JavaMailer";
        MimeBodyPart mimeBodyPartWithStyledText = new MimeBodyPart();
        mimeBodyPartWithStyledText.setContent(msgStyled, "text/html; charset=utf-8");
        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.attachFile(getFile());
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        multipart.addBodyPart(mimeBodyPartWithStyledText);
        multipart.addBodyPart(attachmentBodyPart);
        message.setContent(multipart);
         */
    }
    //Utile in futuro
    private File getFile() throws Exception {
        URI uri = this.getClass()
                .getClassLoader()
                .getResource("attachment.txt")
                .toURI();
        return new File(uri);
    }

    public static void main(String... args) {
        EmailService appl = new EmailService();
        //appl.sendMail("Mail to gmail con smtp.gmail.com", "agoognat@gmail.com");
        appl.sendMail("Mail to gmail using smtp.mailtrap.io", "agoognat@gmail.com");

    }
}
