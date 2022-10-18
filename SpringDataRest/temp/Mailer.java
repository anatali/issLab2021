package unibo.SpringDataRest.communication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


public class Mailer {


    private static JavaMailSender javaMailService  ;

    @Autowired
    private JavaMailSender mailSender ;

    @PostConstruct
    private void init() {
        System.out.println("PostConstruct init: " + mailSender);
        javaMailService = this.mailSender;
    }
    
    public static void sendAMail( String email, String msg) {
        //System.out.println("Mailer sendAMail: " + javaMailService);
        SimpleMailMessage mailMsg = new SimpleMailMessage();
        mailMsg.setTo(email);
        mailMsg.setSubject("Data handling");
        mailMsg.setText("Added Person: " + msg);
        try {
            javaMailService.send(mailMsg);
        } catch (Exception e) {
            System.out.println("Mailer sendAMail ERROR: " + e.getMessage());
        }
    }
}
