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
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.util.TitleBuffer;
import com.google.common.base.Objects;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "listado_cliente", language = "JDQL", value = "SELECT * FROM dom.cliente.Cliente WHERE activo== :true ") })
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("Cliente")
@AutoComplete(repository = ClienteServicio.class, action = "autoComplete")

public class Cliente {
	public String iconName(){
		return "cliente";
	}
     //{{ENUM tipoId Cliente
	public static enum TipoId {
		CUIL, CUIT;
	}	
	//}}
	 
	// {{ Identification on the UI
	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getNumeroIdent());
		return buf.toString();
	}
	// }}
	// {{ OwnedBy (property)
	private String ownedBy;
	@Hidden
	public String getOwnedBy() {
		return ownedBy;
	}
	public void setOwnedBy(final String ownedBy) {
		this.ownedBy = ownedBy;
	}
	// }}
	// {{ Nombre
	private String nombre;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Nombre")
	@MemberOrder(sequence = "1")
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	// }}

	// {{ Apellido
	private String apellido;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Apellido")
	@MemberOrder(sequence = "2")
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	// }}
	// {{ Tipo de Identificacion Tributaria
	private TipoId tipo;
	@DescribedAs("Señala el tipo de documento")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("TipoId")
	@MemberOrder(sequence = "3")
	public TipoId getTipoId() {
		return tipo;
	}
	public void setTipoId(TipoId tipo) {
		this.tipo = tipo;
	}
	// }}
	// {{ Numero de Identificacion Tributaria
	private String numeroIdent;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence = "4")
	public String getNumeroIdent() {
		return numeroIdent;
	}
	public void setNumeroIdent(String numeroId) {
		this.numeroIdent = numeroId;
	}
	// }}
	// {{ Numero de telefono
	private int numeroTel;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("NumeroDeTel")
	@MemberOrder(sequence = "5")
	public int getNumeroTel() {
		return numeroTel;
	}
	public void setNumeroTel(int numeroTel) {
		this.numeroTel = numeroTel;
	}
	// }}	
	
	// {{ Correo electronico
	private String mail;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("mail")
	@MemberOrder(sequence = "6")
	public String getEmail() {
		return mail;
	}
	public void setEmail(String mail) {
		this.mail = mail;
	}
	// }}	
	// {{ Activo
	private boolean activo;
	@Hidden
	@Named("Activo")
	@MemberOrder(sequence = "9")
	public boolean getActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	// }}
	// {{ Remove (action)
	public void remove() {
		setActivo(false);
	}
	// }}

	// {{ Filtro
	public static Filter<Cliente> thoseOwnedBy(final String currentUser) {
		return new Filter<Cliente>() {
			@Override
			public boolean accept(final Cliente cliente) {
				return Objects.equal(cliente.getOwnedBy(), currentUser);
			}
		};
	}
	// }}

	// {{ injected: DomainObjectContainer
	private DomainObjectContainer container;
	protected DomainObjectContainer getContainer() {
		return container;
	}
	public void setDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}
	// }}
}
