package twitter;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersistable;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;
/**
 * Clase que representa la Entidad Twitter en nuestro Sistema
 * Se utiliza para enviar Tweets desde la cuenta de la empresa
 * a trav&eacute;s de nuestro Sistema, y evitar as&iacute; el ingreso
 * a Redes Sociales.
 */
@Named("TWITTER")
@NotPersistable
public class Tweet {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "twitter";
	}
	
	/**
	 * Titulo identificatorio en la UI. Retorna el nombre del Tweet.
	 * 
	 * @return String
	 */
	public String title() {
		return "Twitter";
	}
	
	private String tweet;
    @Named("Tweet")
    @MultiLine(numberOfLines=5)
    @MemberOrder(sequence="1")
    /**
     * Retorna el tweet enviado a la Plataforma de la famosa red social Twitter.
     * @return String
     */
    public String getTweet() {
            return tweet;
    }
    /**
     * Se setea el tweet para enviarlo a la Plataforma de Twitter.
     * @param tweet
     * @throws TwitterException 
     */
    public void setTweet(final String tweet) throws TwitterException{
    	String tw=chequearLargo(tweet);
    	this.tweet=tw;
    }
    
    @Named("Enviar Tweet")
    public void enviarTweet(){
		   ConfigurationBuilder cb = new ConfigurationBuilder();
		   cb.setDebugEnabled(true)
		     .setOAuthConsumerKey("*****")
		     .setOAuthConsumerSecret("*****")
		     .setOAuthAccessToken("*****")
		     .setOAuthAccessTokenSecret("*****");
		   TwitterFactory tf = new TwitterFactory(cb.build());
		   Twitter twit = tf.getInstance();
		   try { 
			   	String tw=chequearLargo(getTweet());
			   	@SuppressWarnings("unused")
				Status status = null;
				status = twit.updateStatus(tw);
				container.informUser("Su Tweet se ha enviado con éxito");
			} catch (TwitterException e) {
				e.printStackTrace();
				container.warnUser("Ha ocurrido un problema!!");
			}
	}
    public String disableEnviarTweet(){
    	if (getTweet().length()>140){
    		return "El Tweet excede el límite de carácteres";
    	}else return null;
    }
    @Hidden
    public Tweet actualizarEstado(final String tweet) throws TwitterException{    		
    		String tw=chequearLargo(tweet);
    		setTweet(tw);    		
			return this;
	} 
	@Hidden
	public String chequearLargo(final String mensaje) throws TwitterException {
		String mensajeCortado="";
		if (mensaje.length()>140){			
			mensajeCortado=mensaje.substring(0, 140);
			container.informUser("El Tweet excedió el límite de carácteres(140)");
			container.informUser("Ha sido cortado!");
		}else{
			mensajeCortado=mensaje;
		}
		return mensajeCortado;
	}
	
    private DomainObjectContainer container;
    /**
     * // {{ injected: DomainObjectContainer
     * @param container
     */
    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
       
    }	
}