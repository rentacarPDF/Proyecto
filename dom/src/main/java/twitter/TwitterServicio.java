package twitter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.NoSuchPaddingException;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;
import encriptacion.Encripta;
import encriptacion.EncriptaException;
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
	
	@MemberOrder(sequence = "1") 
	@Named("Configuracion")
	public TweetConfig crearConfiguracion(
			final @Named("Consumer Key") String consumerKey,
			final @Named("Consumer Secret") String consumerSecret,
			final @Named("Access Token") String accessToken,
			final @Named("Access Token Secret") String accessTokenSecret,
			final @Named("Usuario") String usuario) throws TwitterException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException, EncriptaException{
					
			return laConfiguracion(consumerKey,consumerSecret,accessToken,accessTokenSecret,usuario);
	}
	
	@Hidden 
	public TweetConfig laConfiguracion(
		@Named("ConsumerKey") final String consumerKey,
		@Named("ConsumerSecret") final String consumerSecret, 
		@Named("AccessToken") final String accessToken,
		@Named("AccessTokenSecret") final String accessTokenSecret,
		@Named("Usuario") final String usuario) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException, EncriptaException{
		TweetConfig tw= newTransientInstance(TweetConfig.class);
		

		String clave="LAS AVES VUELAN LIBREMENTE";
		Encripta encripta = new Encripta(clave);
 
		tw.setConsumerKey(encripta.encriptaCadena(consumerKey));
		tw.setConsumerSecret(encripta.encriptaCadena(consumerSecret));
		tw.setAccessToken(encripta.encriptaCadena(accessToken));
		tw.setAccessTokenSecret(encripta.encriptaCadena(accessTokenSecret));
		tw.setUsuario(usuario); 
	
		
		persistIfNotAlready(tw);
		
		List<String> listaStringEncriptada=listaConfiguracion(tw);
	 
		
		//guarda el archivo.
		File file=new File("Twitter.txt");
		OutputStream out = new FileOutputStream(file);
		
		for(String al:listaStringEncriptada){
			out.write((al.toString()+"\n").getBytes(Charset.forName("UTF-8")));
		} 
		out.close();
				
		return tw;
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
			@Named("Usuario") final TweetConfig usuario,
			@MultiLine
			@Named("Tweet") final String tweet) throws TwitterException{
			Tweet twitter=newTransientInstance(Tweet.class);	
			if(tweet!=null){
				twitter.actualizarEstado(usuario,tweet);
			}			
			return twitter;
	} 
	@Hidden
	public List<String> listaConfiguracion(TweetConfig tweet){
		List<String> listaConfiguracion=new ArrayList<String>();
		
	 
		if (tweet != null) {
			
			listaConfiguracion.add(tweet.getConsumerKey());
			listaConfiguracion.add(tweet.getConsumerSecret());
			listaConfiguracion.add(tweet.getAccessToken());
			listaConfiguracion.add(tweet.getAccessTokenSecret());
		}

		// Persistir en el archivo

		return listaConfiguracion;
	}
	
	/**
	 * Busqueda de Configuracion del Correo.
	 * Se retorna un objeto CorreoEmpresa, que contiene el correo y clave.
	 * Se busca por el correo.
	 * 
	 * @param correo
	 * 
	 * @return CorreoEmpresa
	 */
	@Hidden
	@MemberOrder(sequence = "2")
	@Named("buscarConfig")
    public TweetConfig buscarConfiguracionPorUsuario(final String usuario) {
	        return uniqueMatch(TweetConfig.class,new Filter<TweetConfig>() {
	        	public boolean accept(final TweetConfig a){
	        		return a.getUsuario().contains(usuario);
	        	}
			});
	    }	
	
	/**
     * Accion de Autocompletado generada por el framework, 
     * retorna una lista de los objetos de la entidad.
     *
     * @param usuario
     * 
     * @return List<TweetConfig>
     */
	@Hidden    
	public List<TweetConfig> autoComplete(final String usuario) {
		return allMatches(TweetConfig.class, new Filter<TweetConfig>() {
		@Override
		public boolean accept(final TweetConfig t) {		
		return t.getUsuario().contains(usuario) ; 
		}
	  });				
	}
	
}