package unibo.SpringDataRest.communication;

public interface Mailer {
    public void sendMail( String msg, String destination ) throws Exception;
}
