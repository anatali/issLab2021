package unibo.SpringDataRest.communication;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.net.URI;


public class EmailService {
    private String username;
    private String password;

    private final Properties prop;

    public EmailService(String host, int port, String username, String password) {
        prop = new Properties();

        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);

        prop.put("mail.smtp.auth", true);
 //       prop.put("mail.smtp.starttls.enable", "true");

        //prop.put("mail.smtp.ssl.trust", host);
 //       prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        this.username = username;
        this.password = password;
    }

    public EmailService(String host, int port) {
        prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
    }

    public static void main(String... args) {
        try {
            new EmailService("smtp.mailtrap.io", 25,
                    "9ed36bdf47bccb", "c84a76872c7592")
                    .sendMail(); //ok
            //new EmailService("smtp.gmail.com", 25,mail.tin.it
            //new EmailService("smtp.mailtrap.io", 25,
        } catch (Exception e) {
            System.out.println("ERROR:"+e.getMessage());
        }
    }

    public void sendMail() throws Exception {

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

//        Session session = Session.getDefaultInstance(prop);
        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress("fittizio@gmail.com"));
        //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("antonio.natali@unibo.it")); //InternetAddress.parse("to@gmail.com")
        message.addRecipients(Message.RecipientType.TO, InternetAddress.parse("antonio.natali@unibo.it")); //InternetAddress.parse("to@gmail.com")
        message.setSubject("Mail Subject");

        String msg = "This is my first email using JavaMailer";

        message.setText(msg);
        /*
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

        String msgStyled = "This is my <b style='color:red;'>bold-red email</b> using JavaMailer";
        MimeBodyPart mimeBodyPartWithStyledText = new MimeBodyPart();
        mimeBodyPartWithStyledText.setContent(msgStyled, "text/html; charset=utf-8");

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();

        attachmentBodyPart.attachFile(getFile());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
//        multipart.addBodyPart(mimeBodyPartWithStyledText);
//        multipart.addBodyPart(attachmentBodyPart);

        message.setContent(multipart);
*/
        Transport.send(message);
    }

    private File getFile() throws Exception {
        URI uri = this.getClass()
                .getClassLoader()
                .getResource("attachment.txt")
                .toURI();
        return new File(uri);
    }

}
