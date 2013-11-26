package mails;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.isis.applib.annotation.Hidden;

public class Mail {
//Probando mandar mails
@Hidden	
public void enviaMails(String apellido,String nombreCliente,String patente,String totalPago,String correo){
	try
    {
        // Propiedades de la conexión
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.starttls.enable", "true");
        //configuracion de puerto
        props.setProperty("mail.smtp.port", "587");
        //configuracionn de servidor con gmail de la empresa
        props.setProperty("mail.smtp.user", "proyectofinalifes2013@gmail.com");
        props.setProperty("mail.smtp.auth", "true");

        // Preparamos la sesion
        Session session = Session.getDefaultInstance(props);
      
        // Construimos el mensaje
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("proyectofinalifes2013@gmail.com"));
        //mandando al correo
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(correo));
        //Asunto del mensaje
        message.setSubject("RentaCar-PDF Alquiler de Autos");
        message.setText(
        		 "Estimado/a:"+ apellido+" "+nombreCliente+" " +
                 		""+" RentaCar-PDF le informa que usted ha alquilado" +
                 "el auto:"+" "+"con la patente:"+patente +" "+
                 		" "
                 		+"------------------------------------------------------"
                 		+" "+"Total pago:$"+totalPago+" "); 

        // Lo enviamos.
        Transport t = session.getTransport("smtp");
        t.connect("proyectofinalifes2013@gmail.com", "pepito123");
        t.sendMessage(message, message.getAllRecipients());

        // Cierre.
        t.close();
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
}

}
