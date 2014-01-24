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

import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import marca.Marca;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.When;
import org.apache.isis.applib.filter.Filter;

import alquiler.Alquiler;
import autos.Auto;

import twitter4j.TwitterException;
import utiles.EncriptarToFile;
import utiles.EncriptarToString;

@Named("TWITTER")
public class TwitterServicio extends AbstractFactoryAndRepository{
	
	static SecretKey key;
	
 
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
			@Named("Consumer Key") String consumerKey,
			@Named("Consumer Secret") String consumerSecret,
			@Named("Access Token") String accessToken,
			@Named("Access Token Secret") String accessTokenSecret,
			@Named("Usuario") String usuario) throws TwitterException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
					
			return laConfiguracion(consumerKey,consumerSecret,accessToken,accessTokenSecret,usuario);
	}
	
	@SuppressWarnings("static-access")
	@Hidden 
	public TweetConfig laConfiguracion(
		final String consumerKey,
		final String consumerSecret, 
		final String accessToken,
		final String accessTokenSecret,
		final String usuario) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IOException{
		TweetConfig tw= newTransientInstance(TweetConfig.class);
		
		key = KeyGenerator.getInstance("DES").generateKey();
		EncriptarToString enString=new EncriptarToString();
		
		tw.setConsumerKey(enString.encrypt(consumerKey,key));
		tw.setConsumerSecret(enString.encrypt(consumerSecret,key));
		tw.setAccessToken(enString.encrypt(accessToken,key));
		tw.setAccessTokenSecret(enString.encrypt(accessTokenSecret,key));
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
			@Named("Usuario") TweetConfig usuario,
			@MultiLine
			@Named("Tweet") String tweet) throws TwitterException{
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
	}/**
	 * Busqueda de Configuracion de Twitter.
	 * Se retorna un objeto TwitterConfig, que contiene todas las claves.
	 * 
	 * @param tweet
	 * 
	 * @return TweetConfig
	 */
	@Hidden
	@MemberOrder(sequence = "2")
	@Named("buscarConfig")
    public TweetConfig buscarConfiguracion(final TweetConfig tweet) {
	        return uniqueMatch(TweetConfig.class,new Filter<TweetConfig>() {
	        	public boolean accept(final TweetConfig a){
	        		return a.equals(tweet);
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
	 
	@Hidden
	public static SecretKey getClave(){
		return key;
	}
	
}