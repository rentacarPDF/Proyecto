package twitter;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import twitter4j.TwitterException;
import com.google.common.base.Objects;

@Named("TWITTER")
public class TwitterServicio extends AbstractFactoryAndRepository{
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "twitter";
	}
	/**
	 * Se envia el tweet a Twitter.
	 * 
	 * @param tweet
	 * @throws TwitterException
	 */
	@MemberOrder(sequence = "1") 
	@Named("Enviar Tweet")
	public void ActualizarEstado(
			@MultiLine
			@MaxLength(140)
			@Named("Tweet") String tweet) throws TwitterException{
			final String ownedBy = currentUserName();
			actualizar(tweet,ownedBy);
	}
	/**
	 * Metodo que setea la propiedad tweet de la entidad Twitter
	 * y maneja la autenticacion de la cuenta.
	 * 
	 * @param tweet
	 * @param userName
	 * @throws TwitterException
	 */
	@Hidden
	public void actualizar(final String tweet,final String userName) throws TwitterException{
			Twit twitter=newTransientInstance(Twit.class);
			twitter.setTweet(tweet);
			twitter.setOwnedBy(userName);
			
	}
	
	/**
	 * Helpers
	 * 
	 * Retorna un boolean que determina 
	 * si el usuario que se le está pasando por parametro es el mismo.
	 * 
	 * @param twitter
	 * @return boolean
	 * 
	 */
	protected boolean ownedByCurrentUser(final Twit twitter) {
	    return Objects.equal(twitter.getOwnedBy(), currentUserName());
	}
	/**
	 * Helpers
	 * 
	 * Retorna el usuario.
	 * 
	 * @return String
	 */
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}
		
}