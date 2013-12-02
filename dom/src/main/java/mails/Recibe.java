package mails;




import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersistable;
import org.apache.isis.applib.util.TitleBuffer;

/**
 * La entidad Recibe representa los mails que enviaran los clientes a nuestra casilla,
 * mostrando en el sistema solo el remitente y el asunto del mails.
 *
 */
@NotPersistable
public class Recibe {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "mail";
	}
	public String title(){
		final TitleBuffer buf = new TitleBuffer();
        buf.append("CORREOS");
        return buf.toString();
	}    
	private String mailRemitente;
	 /**
     * Retorna remitente del mail
     * @return String
     */
	@Named("remitente")
	@MemberOrder(sequence="1")
	public String getMailRemitente() {
		return mailRemitente;
	}	
	 /**
     * Seteo del remitente del mail recepcionado.
     * @param mailRemitente
     */
	public void setMailRemitente(final String mailRemitente){
		this.mailRemitente=mailRemitente;
	}	
    private String asunto ;
    /**
     * Retorna asunto del mail
     * @return String
     */
	@MemberOrder(sequence="2")
	public String getAsunto() {
		return asunto;
	}
	/**
     * Seteo del asunto del mail recepcionado.
     * @param asunto
     */
	public void setAsunto(final String asunto){
		this.asunto=asunto;
	}

}
