

package mails;

import java.util.Date;
import java.util.List;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.When;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import com.google.common.base.Objects;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("CORREO")
@Immutable
public class Correo implements Comparable<Correo> {

	private CorreoEmpresa correoEmpresa;
	@Hidden
	public CorreoEmpresa getCorreoEmpresa(){
		return correoEmpresa;
	}
	public void setCorreoEmpresa(CorreoEmpresa correoEmpresa){
		this.correoEmpresa=correoEmpresa;
	}
	
	private boolean respondido;
	@Hidden(where=Where.ALL_TABLES)
	public boolean isRespondido(){
		return respondido;
	}
	public void setRespondido(boolean respondido){
		this.respondido=respondido;
	}
	
	private String email;
	/**
	 *  Identificacion del nombre del icono que aparecera en la UI
	 *  @return String
	 */
	public String iconName() {
		return isRespondido()? "respondido": "mail";
	}
	/**
	 * Titulo que aparecera en la UI
	 * @return
	 */
	public String title(){
		return "Correo";
	}

	/**
	 * Retorna el email
	 * 
	 * @return String
	 */
 
	@MemberOrder(sequence = "2")
	public String getEmail() {
		return email;
	}

	/**
	 * Setea el email
	 * 
	 * @param email
	 */
	public void setEmail(final String email) {
		this.email = email;
	}
 
	private String asunto;

	/**
	 * Retorna el asunto del email
	 * @return String
	 */
	@MemberOrder(sequence = "1")
	public String getAsunto() {
		return asunto;
	}

	/**
	 * Setea el asunto del email
	 * 
	 * @param asunto
	 */
	public void setAsunto(final String asunto) {
		this.asunto = asunto;
	}

	private String mensaje;

	/**
	 
	 * 
	 * @return String
	 */
	@Hidden(where=Where.ALL_TABLES)
	@MultiLine(numberOfLines = 6)
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * Setea el mensaje del email.
	 * @param mensaje
	 */
	public void setMensaje(final String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Se puede responder el correo directamente desde el viewer
	 * 
	 * @param mensaje
	 *            
	 * @return Correo
	 */
	public Correo Responder(
			@MultiLine(numberOfLines = 6) @Named("Mensaje") String mensaje) {

		Envio correo = new Envio();
		correo.setProperties(getCorreoEmpresa());
		setRespondido(true);
		correo.enviar(mensaje,this.getEmail());
		container.informUser("El mensaje ha sido enviado con éxito!");
		return this;
	}

	/**
	 * Permite borrar el correo electronico desde la UI
	 * Devuelve la lista de correos.
	 * @return List<Correo>
	 */
	@Named("Borrar")
	@Bulk
	public List<Correo> borrar() {
		// Borramos el/los objeto/s seleccionado/s
		container.removeIfNotAlready(this);
		// Vuelvo a la bandeja de entrada
		return bde.listaMensajesPersistidos();
	}

	/**
	 * Retorna la fecha en que llego el correo
	 * @return Date
	 */
	private Date fechaActual;
	@Named("Fecha del Correo")
	@MemberOrder(sequence = "3")
	public Date getFechaActual() {
		return fechaActual;
	}

	/**
	 * Setea la fecha en que llego el correo
	 * 
	 * @param fechaActual
	 */
	public void setFechaActual(final Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	// {{ injected: DomainObjectContainer
	private DomainObjectContainer container;

	public void injectDomainObjectContainer(
			final DomainObjectContainer container) {
		this.container = container;
	}
	
	private CorreoServicio bde; 
	public void injectServicioBandejaDeEntrada(
			final CorreoServicio bde) {
		this.bde = bde;
	} 
	
	
	/**
	 * Se ordenan los correos por fecha de ingreso.
	 */
	@Override
	public int compareTo(Correo mensaje) { 
		return this.fechaActual.compareTo(mensaje.getFechaActual());
	}

	
	private String usuario;

	@Hidden
	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}// }}
	
	public static Filter<Correo> creadoPor(final String usuarioActual) {
		return new Filter<Correo>() {
			@Override
			public boolean accept(final Correo mensaje) {
				return Objects.equal(mensaje.getUsuario(), usuarioActual);
			}
		};
	}
 

}
