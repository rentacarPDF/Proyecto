

package mails;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;



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
		Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        Session session = Session.getDefaultInstance(props, null);
        final List<Recibe> listaJavaMail = new ArrayList<Recibe>();
       try
        {// Se obtiene el Store y el Folder, para poder leer el
          // correo.
           Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "rentacarpdf@gmail.com","pepito1234");
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_ONLY);
            Message mensajes[] = inbox.search(new FlagTerm(
            		new Flags(Flags.Flag.SEEN),false));
         // Se obtienen la cantidad de  mensajes.
            String mensajeNuevos = mensajes.length != 0 ? "hay "+mensajes.length+" mensajes nuevos":" No hay mensajes nuevos.!" ;
        	// Se escribe from y subject de cada mensaje
            for (Message mensaje : mensajes){	//de:
            	final Recibe mensajeC = newTransientInstance(Recibe.class);
                //"Subject:"=asunto
            	mensajeC.setMailRemitente(mensaje.getFrom()[0].toString());
                mensajeC.setAsunto(mensaje.getSubject()); 
                listaJavaMail.add(mensajeC);
            } 
            inbox.close(false);
            store.close();
            getContainer().warnUser("Revise su casilla de e-mail "+ mensajeNuevos);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return listaJavaMail;
    }
}