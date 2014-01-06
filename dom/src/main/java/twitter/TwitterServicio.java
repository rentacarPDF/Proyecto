package twitter;

import org.apache.isis.applib.AbstractFactoryAndRepository;
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
	 * Se crea una instancia de la entidad Tweet.
	 * En base al tweet escrito por el usuario, donde se chequea el numero 
	 * de caracteres.
	 * 
	 * @param tweet
	 * @throws TwitterException
	 */
	@MemberOrder(sequence = "1") 
	@Named("Enviar Tweet")
	public Tweet crearTweet(
			@MultiLine
			@Named("Tweet") String tweet) throws TwitterException{
			Tweet twitter=newTransientInstance(Tweet.class);							
			twitter.actualizarEstado(tweet);			
			
			return twitter;
	} 
}