

package mails;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;


import org.apache.isis.applib.AbstractFactoryAndRepository;


import org.apache.isis.applib.annotation.Named;
/**
 * 
 * Clase que recepciona Emails a nuestros Clientes.
 *
 */
@Named("Mails")
public class RecibirMailServicio extends AbstractFactoryAndRepository{
	/**
	 * Metodo que recibe Emails de los Clientes
	 * 
	 * En el se configuran las propiedades de conexi&oacute;n,
	 * se configura el puerto y el servidor del correo.  
	 * Se prepara la sesi&oacute;n para recibir el mensaje, se setea el remitente y el asunto e informa
	 * la cantidad de mensajes que se encuentran el buzon de entrada de nuestro Gmail.  
	 * y por ultimo se cierra la sesi&oacute;n.
	 * 
	 */
	public List<Recibe> Recepcion(){ 
		// Se obtiene la Session
        Properties prop = new Properties();
        prop.setProperty("mail.pop3.starttls.enable", "false");
        prop.setProperty(
         "mail.pop3.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.pop3.socketFactory.fallback", "false");
        prop.setProperty("mail.pop3.port", "995");
        prop.setProperty("mail.pop3.socketFactory.port", "995");
        Session sesion = Session.getInstance(prop);
        // sesion.setDebug(true); 
        final List<Recibe> listaJavaMail = new ArrayList<Recibe>();
       try
        {// Se obtiene el Store y el Folder, para poder leer el
          // correo.
            Store store = sesion.getStore("pop3");
            store.connect("pop.gmail.com", "rentacarpdf@gmail.com","pepito1234");
            Folder folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
            Message[] mensajes = folder.getMessages();
            // Se obtienen los mensajes.
            String mensajeNuevos = mensajes.length == 1 ? "HAY NUEVO MENSAJES!" : "HAY:"+mensajes.length;
        	// Se escribe from y subject de cada mensaje
            for (Message mensaje : mensajes){	//de:
            	final Recibe mensajeC = newTransientInstance(Recibe.class);
                //"Subject:"=asunto
            	mensajeC.setMailRemitente(mensaje.getFrom()[0].toString());
                mensajeC.setAsunto(mensaje.getSubject()); 
                listaJavaMail.add(mensajeC);
            } 
            folder.close(false);
            store.close();
            getContainer().warnUser("cantidad de mensajes nuevos :"+ mensajeNuevos);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listaJavaMail;
    }
}