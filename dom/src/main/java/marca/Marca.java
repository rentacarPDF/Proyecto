package marca;

import javax.jdo.annotations.IdentityType;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;
import javax.jdo.annotations.VersionStrategy;

/**
 * Clase que representa la Entidad Marca del vehiculo en nuestro sistema.
 * 
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listado_marcas", language = "JDQL", value = "SELECT FROM dom.utilidades.Marca WHERE usuario == :usuario") })
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("MARCA")
@AutoComplete(repository = MarcaServicio.class, action = "autoComplete")
public class Marca {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * @return String
	 */
	public String iconName() {
		return "marca";
	}

	/**
	 * Titulo identificatorio en la UI Retorna el nombre de la Marca del
	 * vehiculo.
	 * 
	 * @return Stirng
	 */
	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getNombre());
		return buf.toString();
	}

	private String usuario;

	/**
	 * Retorna el nombre del usuario. No se muestra en la UI
	 * 
	 * @return String
	 */
	@Hidden
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Se setea el nombre del usuario.
	 * 
	 * @param usuario
	 */
	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	private String nombre;

	/**
	 * Retorna el nombre de la Marca del vehiculo.
	 * 
	 * @return String
	 */
	@DescribedAs("La marca del vehiculo.")
	@Named("Nombre")
	@MemberOrder(sequence = "1")
	public String getNombre() {
		return nombre;
	}

	/**
	 * Se setea el nombre de la Marca del vehiculo.
	 * 
	 * @param nombre
	 */
	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	private boolean activo;

	/**
	 * Retorna si la Marca del vehiculo esta activa o no.
	 * 
	 * @return boolean
	 */
	@Hidden
	@DescribedAs("Activo")
	public boolean getActivo() {
		return activo;
	}

	/**
	 * Se setea si la Marca del vehiculo esta activa o no.
	 * 
	 * @param activo
	 */
	public void setActivo(final boolean activo) {
		this.activo = activo;
	}

	/**
	 * Metodo que borra la Marca especificada. Provisto por el Framework.
	 */
	@Named("Borrar")
	public void remove() {
		setActivo(false);
		container.informUser("El registro a sido eliminado");
	}

	private DomainObjectContainer container;

	/**
	 * injected: DomainObjectContainer
	 */
	public void setDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}

}
