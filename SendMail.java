package Devoir;

import java.net.UnknownHostException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;


public class SendMail {
	
	MimeMessage message;

	public SendMail() throws UnknownHostException {
		Properties props=System.getProperties();
		
		//DEFINITION DES PROPRIETES SMTP
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.user", "earthbnb");
        props.put("mail.smtp.password", "motdepasse2016");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");    
		props.put("mail.transport.protocol", "smtp");
		
		//OUVERTURE D'UNE SESSION SUR LE GMAIL DE L'APPLICATION
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		        return new PasswordAuthentication("earthbnb", "motdepasse2016");
		    }
		}
		);
		
		this.message=new MimeMessage(session); 
		
	}

	public void envoyer (String mailloueur,String appartement,String maillocataire) throws AddressException, MessagingException {
		//FORMATAGE DE LA REQUETE SMTP
		this.message.setFrom(new InternetAddress("earthbnb"));
		this.message.setRecipient(Message.RecipientType.TO, new InternetAddress(mailloueur));
		message.setSubject("Nouvelle location");
		message.setText(maillocataire+"  a loué l'appartement : \n" + appartement);
		//ENVOI
		Transport.send(this.message);
	}

}
