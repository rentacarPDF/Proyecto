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

@Named("TWITTER")
@NotPersistable
public class Twit {
	
	public String iconName(){
		return "twitter";
	}
	
	private String tweet;
    @Named("Tweet")
    @MultiLine(numberOfLines=2)
    @MemberOrder(sequence="2")
    public String getTweet() {
            return tweet;
    }
    
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
	@Hidden 
	public String getOwnedBy() {
	    return ownedBy;	
	}
	
	public void setOwnedBy(final String ownedBy){
	    this.ownedBy = ownedBy;	
	}	
	
    // {{ injected: DomainObjectContainer
    @SuppressWarnings("unused")
    private DomainObjectContainer container;
    
    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
       
    }
    // }}
	
}