/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.utilities;

import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author juan
 */
public abstract class MailNotificacion {
    
    public static void correoNotificación(String nombrePara, String apellidoPara, String correoPara) {
        final String usuario = "referidossistema@gmail.com";
        final String clave = "ttpsqvzebksdpjex";

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com"); // envia 
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "25");
        props.setProperty("mail.smtp.starttls.required", "false");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, clave);
            }
        });

        try {
            MimeMessage mensage = new MimeMessage(session);
            mensage.setFrom(new InternetAddress(usuario));
            mensage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoPara));
            mensage.setSubject("Hola " +nombrePara + " " + " has registrado una cita en urban barber shop.");
            mensage.setContent("<center> "
                    + "<img src='https://grupocinte.com/wp-content/uploads/2019/05/logo-colores-pantone-con-espacio.png' width='500px' height='200px' >"
                    + "</center>"
                    + "<br/>"
                    + "<h1> Hola, " + nombrePara + " " + apellidoPara + " </h1>"
                    + "<br/>" +nombrePara,
                    "text/html");
            Transport.send(mensage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
            
        }

    

}
    
}
