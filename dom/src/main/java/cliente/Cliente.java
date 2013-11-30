package cliente;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;

/**
 * Clase que representa la entidad Cliente en nuestro sistema. 
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listado_cliente", language = "JDQL", value = "SELECT * FROM dom.cliente.Cliente WHERE activo== :true ") })
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("Cliente")
@AutoComplete(repository = ClienteServicio.class, action = "autoComplete")

public class Cliente {
	/**
	 *  Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "cliente";
	}
     /**
      * Enumeracion que define el Tipo de Identificacion Tributaria.
      * CUIT/CUIL
      *
      */
	public static enum TipoId {
		CUIL, CUIT;
	}	
	/**
	 * Titulo identificatorio en la UI.
	 * @return String
	 */
	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getNumeroIdent());
		return buf.toString();
	}
	
	private String usuario;
	/**
	 * Retorna el nombre de usuario.
	 * @return String
	 */
	@Hidden
	public String getUsuario() {
		return usuario;
	}
	/**
	 * Se setea el nombre de usuario.
	 * @param usuario
	 */
	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}
	
	private String nombre;
	/**
	 * Retorna el nombre del Cliente.
	 * @return String
	 */
	@Named("Nombre")
	@MemberOrder(sequence = "1")
	public String getNombre() {
		return nombre;
	}
	/**
	 * Se setea el nombre del Cliente.
	 * @param nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	private String apellido;
	/**
	 * Retorna el apellido del Cliente.
	 * @return String
	 */
	@Named("Apellido")
	@MemberOrder(sequence = "2")
	public String getApellido() {
		return apellido;
	}
	/**
	 * Se setea el apellido del Cliente.
	 * @param apellido
	 */
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	private TipoId tipo;
	/**
	 *  Retorna el tipo de Identificacion Tributaria.
	 * @return TipoId
	 */
	@DescribedAs("Señala el tipo de documento")
	@Named("TipoId")
	@MemberOrder(sequence = "3")
	public TipoId getTipoId() {
		return tipo;
	}
	/**
	 * Se setea el tipo de Identificacion Tributaria.
	 * @param tipo
	 */
	public void setTipoId(TipoId tipo) {
		this.tipo = tipo;
	}
 
	private String numeroIdent;
	/**
	 * Numero de Identificacion Tributaria.
	 * @return String
	 */
	@MemberOrder(sequence = "4")
	public String getNumeroIdent() {
		return numeroIdent;
	}
	/**
	 * Seteo del numero de Identificacion Tributaria.
	 * @param numeroId
	 */
	public void setNumeroIdent(String numeroId) {
		this.numeroIdent = numeroId;
	}
 
	private int numeroTel;
	/**
	 * Retorna numero de tel&eacute;fono del Cliente.
	 * @return int
	 */
	@Named("NumeroDeTel")
	@MemberOrder(sequence = "5")
	public int getNumeroTel() {
		return numeroTel;
	}
	/**
	 * Seteo numero de tel&eacute;fono del Cliente.
	 * @param numeroTel
	 */
	public void setNumeroTel(int numeroTel) {
		this.numeroTel = numeroTel;
	}
	 
	private String mail;
	/**
	 * Retorna el e-mail del Cliente.
	 * @return String
	 */
	@Named("mail")
	@MemberOrder(sequence = "6")
	public String getEmail() {
		return mail;
	}
	/**
	 * Seteo del e-mail del Cliente.
	 * @param mail
	 */
	public void setEmail(String mail) {
		this.mail = mail;
	}

	private boolean activo;
	/**
	 * Retorna si el Cliente está activo o no.
	 * @return boolean
	 */
	@Hidden
	@Named("Activo")
	@MemberOrder(sequence = "9")
	public boolean getActivo() {
		return activo;
	}
	/**
	 * Se setea si el Cliente está activo o no.
	 * @param activo
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	/**
     * Metodo que borra el Cliente especificado.
     * Provisto por el Framework.
     */
	@Named("Borrar")
	public void remove() {
		setActivo(false);
	}
	
	private DomainObjectContainer container;
	/**
	 *  {{ injected: DomainObjectContainer
	 */
	protected DomainObjectContainer getContainer() {
		return container;
	}
	/**
	 *  {{ injected: DomainObjectContainer
	 */
	public void setDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}
}
