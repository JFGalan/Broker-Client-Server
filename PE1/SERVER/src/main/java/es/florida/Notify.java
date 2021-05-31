package es.florida;

import es.florida.Mailsender.EmailService;
import org.apache.commons.mail.EmailException;

public class Notify implements Runnable {

    private EmailService email = new EmailService();
    private String subject;
    private String recipient;
    private String message;
    private String hostName;
    private String emailBroker;
    private int port;

    public Notify(String recipient,String subject, String message, String hostName, String emailBroker, int port){

        this.subject = subject;
        this.message = message;
        this.hostName = hostName;
        this.emailBroker = emailBroker;
        this.port = port;
        this.recipient = recipient;

    }

    @Override
    public void run() {

        try {

            this.email.sendEmail(this.recipient, this.subject, this.message, this.port, this.hostName, this.emailBroker);

        } catch (EmailException e) {

            e.printStackTrace();

        }
    }

}
