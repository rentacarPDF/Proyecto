package twitter;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import twitter4j.TwitterException;

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
			actualizar(tweet);
	}
	/**
	 * Metodo que setea la propiedad tweet de la entidad Twitter
	 * y maneja la autenticacion de la cuenta.
	 * 
	 * @param tweet
	 * @throws TwitterException
	 */
	@Hidden
	public void actualizar(final String tweet) throws TwitterException{
			Twit twitter=newTransientInstance(Twit.class);
			twitter.setTweet(tweet);			
	}
}