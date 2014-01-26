package mails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import org.apache.isis.applib.DomainObjectContainer;

import utiles.EncriptarToString;

public class Recibe {
	
	   private Session session;
       private Properties propiedades = new Properties();
       private Store store;
       private Message[] mensajes;
       private List<Correo> listaMensajes = new ArrayList<Correo>();
       private static String contenidoMail;
       private CorreoEmpresa correoEmpresa=new CorreoEmpresa();
       private EncriptarToString enString=new EncriptarToString();
       /**
        *Retorna la lista con los correos electrónicos nuevos
        * @return List<Correo>
        */
       public List<Correo> getListaMensajes() {
               return listaMensajes;
       }
       
       /**
        * Setea la lista de correos electrónicos nuevos
        * @param listaMensajes
        */
       public void setListaMensajes(List<Correo> listaMensajes) {
               this.listaMensajes = listaMensajes;
       }
       
       /**
        * A la sesión actual le aplica las propiedades de conexión
        * @param propiedades
        */
       public void setSession(Properties propiedades) {
               session = Session.getInstance(propiedades);
               session.setDebug(true);
       }

       /**
        *
        * @return Retorna la sesión actual
        */
       public Session getSession() {
               return session;
       }

       /**
        * Setea las propiedades para crear la sesión de usuario
        */
       public void setProperties(CorreoEmpresa ce) {
    	   correoEmpresa=ce;
    	   System.out.println("PROPIEDADES%%%%%% "+ce.getCorreo());
    	   System.out.println("PROPIEDADES%%%%%% "+ce.getPass());
           // Deshabilitamos TLS
           propiedades.setProperty("mail.pop3.starttls.enable", "false");

           // Hay que usar SSL
           propiedades.setProperty("mail.pop3.socketFactory.class","javax.net.ssl.SSLSocketFactory" );
           propiedades.setProperty("mail.pop3.socketFactory.fallback", "false");

           // Puerto 995 para conectarse.
           propiedades.setProperty("mail.pop3.port","995");
           propiedades.setProperty("mail.pop3.socketFactory.port", "995");
           
           setSession(propiedades);
       }

       /**
        *Retorna las propiedades para crear la sesión de usuario
        * @return Properties
        */
       public Properties getProperties() {
               return propiedades;
       }

       /**
        *
        * Se encarga de conectarse al buzon del correo y bajar todos los correos.
        * Los adiciona a una lista, con la misma seran persistidos en la BD. 
        *
        */
       public void accion() {
               
               try {
            	  
                       store = session.getStore("pop3");
                       System.out.println(" %%%&& PASS DE LA BD "+correoEmpresa.getPass());
                       String pass=enString.decrypt(correoEmpresa.getPass(),cs.getKey());
                       System.out.println("%%%&& PASS DECRYPT "+pass);
                       store.connect("pop.gmail.com",correoEmpresa.getCorreo(),pass);
                       //store.connect("pop.gmail.com","rentacarPDF@gmail.com","pepito1234");

                       Folder folder = store.getFolder("INBOX");
       
                       folder.open(Folder.READ_ONLY);
               
                       mensajes = folder.getMessages();
                                         
                       for (Message mensaje : mensajes)
                       {
                    			
                    	System.out.println("MENSAJES");
                        System.out.println(mensaje.getContent());
                                                
                        final Correo actual = new Correo();
                         
                        actual.setEmail(mensaje.getFrom()[0].toString());
						actual.setAsunto(mensaje.getSubject());
						actual.setFechaActual(mensaje.getSentDate());
						actual.setCorreoEmpresa(correoEmpresa);
						analizaParteDeMensaje(mensaje);
						if(contenidoMail.length()<255){
							actual.setMensaje(contenidoMail);
						}
						
						getListaMensajes().add(actual);
						
                       }
                        //Cierre de la sesión
                       store.close();
                       
               } catch (MessagingException e) {
                       e.printStackTrace();
               } catch (IOException e) {
                       e.printStackTrace();
               }
      }
       
       private static void analizaParteDeMensaje(Part unaParte)
       {
           try
           {
             // Si es multipart, se analiza cada una de sus partes recursivamente.
               if (unaParte.isMimeType("multipart/*"))
               {
                   Multipart multi;
                   multi = (Multipart) unaParte.getContent();
     
                   for (int j = 0; j < multi.getCount(); j++)
                   {
                       analizaParteDeMensaje(multi.getBodyPart(j));
                   }
               }
               else
               {
                 // Si es texto, se escribe el texto.
                   if (unaParte.isMimeType("text/plain"))
                   {
                       contenidoMail=unaParte.getContent().toString();
                       System.out.println("Texto " + unaParte.getContentType());
                       System.out.println(unaParte.getContent());
                       System.out.println("---------------------------------");
                   }
                   else
                   {
                     // Si es imagen, se guarda en fichero y se visualiza en JFrame
                       if (unaParte.isMimeType("image/*"))
                       {
                           System.out.println(
                               "Imagen " + unaParte.getContentType());
                           System.out.println("Fichero=" + unaParte.getFileName());
                           System.out.println("---------------------------------");
     
                           //salvaImagenEnFichero(unaParte);
                           //visualizaImagenEnJFrame(unaParte);
                       }
                   }
               }
           }
           catch (Exception e)
           {
               e.printStackTrace();
           }
       }
     
   @SuppressWarnings("unused")
   private DomainObjectContainer container;
   
   public void injectDomainObjectContainer(DomainObjectContainer container) {
           this.container = container;
   }
   
   private CorreoServicio cs=new CorreoServicio();
   public void injectCorreoServicio(CorreoServicio cs){
	   this.cs=cs;
   }

}

