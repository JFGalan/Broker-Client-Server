package es.florida.Mailsender;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class EmailService {

    public EmailService() {

    }

    public void sendEmail(String recipient, String subject, String message, int PORT, String hostName, String emailBroker) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName(hostName);
        email.setSmtpPort(PORT);
        email.setFrom(emailBroker);
        email.setSubject(subject);
        email.setMsg(message);
        email.addTo(recipient);
        email.send();
    }
}

