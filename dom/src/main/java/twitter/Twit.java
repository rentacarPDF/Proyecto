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
public class Twit {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "twitter";
	}
	
	private String tweet;
    @Named("Tweet")
    @MultiLine(numberOfLines=2)
    @MemberOrder(sequence="2")
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
     */
    public void setTweet(final String tweet){
		   ConfigurationBuilder cb = new ConfigurationBuilder();
		   cb.setDebugEnabled(true)
		     .setOAuthConsumerKey("******")
		     .setOAuthConsumerSecret("******")
		     .setOAuthAccessToken("******")
		     .setOAuthAccessTokenSecret("******");
		   TwitterFactory tf = new TwitterFactory(cb.build());
		   Twitter twit = tf.getInstance();
		   try { 
			   	@SuppressWarnings("unused")
				Status status = null;
				status = twit.updateStatus(tweet);
				container.informUser("Su Tweet se ha enviado con éxito");
				container.informUser(tweet);
				
			} catch (TwitterException e) {
				e.printStackTrace();
				container.warnUser("Ha ocurrido un problema--");
			}
	}
        
    private String ownedBy;
    /**
     * Retorna el usuario.
     * @return String
     */
	@Hidden 
	public String getOwnedBy() {
	    return ownedBy;	
	}
	/**
	 * Se setea el usuario.
	 * @param ownedBy
	 */
	public void setOwnedBy(final String ownedBy){
	    this.ownedBy = ownedBy;	
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