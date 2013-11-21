package twitter;

import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;
import com.google.common.base.Objects;


@Named("TWITTER")
public class TwitterServicio extends AbstractFactoryAndRepository{
	
	public String iconName(){
		return "twitter";
	}

	@MemberOrder(sequence = "1") 
	@Named("Enviar Tweet")
	public void ActualizarEstado(
			@MultiLine
			@MaxLength(140)
			@Named("Tweet") String tweet) throws TwitterException{
			final String ownedBy = currentUserName();
			actualizar(tweet,ownedBy);
	}
	@Hidden
	public void actualizar(final String tweet,final String userName) throws TwitterException{
			Twit twitter=newTransientInstance(Twit.class);
			twitter.setTweet(tweet);
			twitter.setOwnedBy(userName);
			
	}
		
	// }}	
		// {{ Helpers
		protected boolean ownedByCurrentUser(final Twit t) {
		    return Objects.equal(t.getOwnedBy(), currentUserName());
		}
		protected String currentUserName() {
		    return getContainer().getUser().getName();
		}
		// }}	
}