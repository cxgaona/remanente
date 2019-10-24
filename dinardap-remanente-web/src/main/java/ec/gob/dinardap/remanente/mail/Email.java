/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.dinardap.remanente.mail;

import java.util.Properties;

import javax.mail.AuthenticationFailedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author christian.gaona
 */
public class Email {

    private final Properties prop;
    private static final String FROM = "notificaciones.remanentes@dinardap.gob.ec";
    private static final String URL = "http://10.0.0.168:8080/remanente";
//    private String to;
//    private String subject;

    public Email() {
        prop = new Properties();
        prop.put("mail.smtp.host", "smtpsrv.dinardap.gob.ec");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "false");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.transport.protocol", "smtp");
    }

    public void sendMail(String para,
            String asunto,
            String cuerpo) throws AuthenticationFailedException,
            MessagingException {
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("notificaciones.remanentes@dinardap.gob.ec", "Password.1");
            }
        });
        try {
            if (para != null && !para.isEmpty()) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(FROM));
                message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(para));
                message.setSubject(asunto);
                Multipart multipartes = new MimeMultipart();
                MimeBodyPart htmlPart = new MimeBodyPart();
                String cabecera = "<html><body><center><h1>Plataforma de Remanentes</h1></center><br/>";
                String contenido = "<center><p>" + cuerpo + "</p></center><br/>";
                String boton = "<center><a href='" + URL + "'>Plataforma Remanentes</a></center>";
                String formulario = String.format("%s%s%s", cabecera, contenido, boton);
                htmlPart.setContent(formulario, "text/html; charset=utf-8");
                multipartes.addBodyPart(htmlPart);
                message.setContent(multipartes);
                Transport.send(message);
            }
        } catch (AuthenticationFailedException e) {
            throw e;
        } catch (MessagingException ex) {
            throw ex;
        }
    }
}
