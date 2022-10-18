package unibo.SpringDataRest.communication;

import org.springframework.stereotype.Component;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.URI;

@Component
public class EmailService {
    private Properties prop;

    public EmailService() {
    }

    public void sendMailUsingMailTrap(String msg, String destination){
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
                    return new PasswordAuthentication("9ed36bdf47bccb", "c84a76872c7592");
                }
            });
            sendMail(session, msg, destination);        } catch (Exception e) {
            System.out.println("ERROR:"+e.getMessage());
        }
    }
    public void sendMailUsingGoogle(String msg, String destination){
        try {
            prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.port", 25);  //25  587
            prop.put("mail.smtp.auth", true);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.setProperty("mail.debug", "true");
            Session session = Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    //Password set Google Mail  Password per le app
                    return new PasswordAuthentication("agoognat@gmail.com", "bhthxbuswevuxfba");
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
        message.setFrom(new InternetAddress("fittizio@gmail.com"));
        //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("antonio.natali@unibo.it")); //InternetAddress.parse("to@gmail.com")
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(destination)); //"agoognat@gmail.com"
        message.setSubject("MailOct18");
        message.setText(msg);
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

//        appl.sendMailUsingGoogle("Mail to unibo usando smtp.gmail.com","antonio.natali@unibo.it");
//        appl.sendMailUsingGoogle("Mail to gmail usando smtp.gmail.com","agoognat@gmail.com");
//        appl.sendMailUsingMailTrap("Mail to unibo usando smtp.mailtrap.io","antonio.natali@unibo.it");
//        appl.sendMailUsingMailTrap("Mail to gmail usando smtp.mailtrap.io","agoognat@gmail.com");

    }
}
