/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.mail;

import java.util.Properties;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author christian.gaona
 */
public class Email {

//    private static final String SMTP_SERVER = "smtp server ";
//    private static final String USERNAME = "";
//    private static final String PASSWORD = "";
//
//    private static final String EMAIL_FROM = "from@gmail.com";
//
//    private String emailTo; //= "email_1@yahoo.com, email_2@gmail.com";
//    private String emailToCC; //= "";
//    private String emailSubject; //= "Test Send Email via SMTP";
//    private String emailText; //= "<h1>Hello Java Mail \\n ABC123</h1>";
    private Properties props;
    private String from, to, subject;
    private MimeMultipart multipart;

    public Email() {
        props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

}
