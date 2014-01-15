package mails;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Envio {

	private Session session;
	private Properties propiedades = new Properties();

	/**
	 * Setea la sesion para poder enviar el correo electronico.
	 * 
	 * @param propiedades
	 */
	public void setSession(Properties propiedades) {

		session = Session.getInstance(propiedades);
		session.setDebug(true);
	}

	/**
	 * Retorna la sesion creada.
	 * 
	 * @return Session
	 */
	public Session getSession() {
		// TODO Auto-generated method stub
		return session;
	}

	/**
	 * Setea propiedades para poder enviar el correo electronico 
	 * al destinatario.
	 * Puerto, Host, Correo, etc.
	 * 
	 */
	public void setProperties() {

		// Nombre del host de correo, es smtp.gmail.com
		propiedades.setProperty("mail.smtp.host", "smtp.gmail.com");

		// TLS si está disponible
		propiedades.setProperty("mail.smtp.starttls.enable", "true");

		// Puerto de gmail para envio de correos
		propiedades.setProperty("mail.smtp.port", "587");

		// Nombre del usuario
		propiedades.setProperty("mail.smtp.user",
				"rentacarPDF@gmail.com");

		// Si requiere o no usuario y password para conectarse.
		propiedades.setProperty("mail.smtp.auth", "true");

		setSession(propiedades);

	}

	/**
	 * Retorna las propiedades seteadas para poder enviar el mail.
	 * 
	 * @return Properties
	 */
	public Properties getProperties() {
		return propiedades;
	}

	/**
	 * Env&iacute;a el mail al cliente, para responder su solicitud.
	 * 
	 * @param mensaje
	 * @param direccion
	 */
	public void enviar(String mensaje, String direccion) {
		MimeMessage message = new MimeMessage(getSession());
		Multipart multiPart = new MimeMultipart("alternative");
		try {
			message.setFrom(new InternetAddress(
					"rentacarPDF@gmail.com"));
			// A quien va dirigido
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					direccion));
			message.setSubject("RentacarPDF - Consulta");

			String firma="Seguimos viajando a su lado....<b>rentacarPDF.</b>";
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			// www.gravatar.com/avatar/291161c91449c74a82d7e502958e0160.png'
			Object html = "<p style='font-size:12px;'><font color='grey'>"+mensaje.toString()+"</font></p> " +
			"<p><img src='http://es.gravatar.com/userimage/58275124/6d3364b689fcc936b61fb0b15fcece70.png?size=200' </img></p>"+
			"<p align=center style='font-size:12px;'><font color=#279898>"+firma.toString()+"</font></p>";

			htmlPart.setContent(html, "text/html; charset=utf-8");

			multiPart.addBodyPart(htmlPart);
			message.setContent(multiPart);

			Transport t = session.getTransport("smtp");
			t.connect("rentacarPDF@gmail.com", "pepito1234");
			t.sendMessage(message, message.getAllRecipients());
 
			t.close();
			
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
