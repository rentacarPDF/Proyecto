package twitter;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.When;

/**
 * La entidad Tweet Config, sirve para la configuracion de claves que se usaran
 * a lo largo del Sistema.
 * 
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listadoClaves", language = "JDOQL", value = "SELECT FROM twitter.twitter") })
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("TWITTER")
@AutoComplete(repository = TwitterServicio.class, action = "autoComplete")
@Named("TWITTER")
@Immutable(When.ONCE_PERSISTED)
public class TweetConfig {

	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * @return String
	 */

	public String iconName() {
		return "twitter";
	}

	public String title() {
		return getUsuario();
	}



	// Claves
	// consumerKey
	private String consumerKey;

	@MemberOrder(sequence = "1")
	@Named("Consumer Key")
	public String getConsumerKey() {
		return consumerKey;
	}

	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	// ConsumerSecret
	private String consumerSecret;

	@MemberOrder(sequence = "2")
	@Named("Consumer Secret")
	public String getConsumerSecret() {
		return consumerSecret;

	}

	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;

	}

	// AccessToken
	private String accessToken;

	@MemberOrder(sequence = "3")
	@Named("Access Token")
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;

	}

	// AccessTokenSecret
	private String accessTokenSecret;

	@MemberOrder(sequence = "4")
	@Named("Access Token Secret")
	public String getAccessTokenSecret() {
		return accessTokenSecret;
	}

	public void setAccessTokenSecret(String accessTokenSecret) {
		this.accessTokenSecret = accessTokenSecret;

	}

	//Usuario
	private String usuario;

	@MemberOrder(sequence = "5")
	@Named("Usuario")
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	} 
 
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	/**
	 * {{ injected: DomainObjectContainer
	 */
	public void setDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;

	}

}
