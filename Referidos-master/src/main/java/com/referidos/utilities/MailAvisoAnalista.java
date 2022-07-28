/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.referidos.utilities;


import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.*;
import java.util.*; 
import javax.naming.*; 
import javax.naming.directory.*;

/**
 *
 * @author juan
 */
public class MailAvisoAnalista {
    
    public static void mailAvisoAnalista(String nombreAnalista, String apellidoAnalista,  String correoAnalista, String nombreReferido, String apellidoReferido, String encabezado, String contenido, String footer) {
        final String usuario = "referidossistema@gmail.com";
        final String clave = "ttpsqvzebksdpjex";

        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com"); // envia 
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.smtp.port", "587");
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
            mensage.addRecipient(Message.RecipientType.TO, new InternetAddress(correoAnalista));
            mensage.setSubject("Hola! " + nombreAnalista + " " + encabezado);
            mensage.setContent("<center> "
                    + "<img src='https://grupocinte.com/wp-content/uploads/2019/05/logo-colores-pantone-con-espacio.png' width='500px' height='200px' >"
                    + "</center>"
                    + "<br/>"
                    + "<h1> Hola, " + nombreAnalista + " " + apellidoAnalista + " </h1>"
                    + contenido + " " + nombreReferido + " " + apellidoReferido + " "
                    + "<br/>" + footer,
                    "text/html");
            Transport.send(mensage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
            
        }

    }    
    
}
