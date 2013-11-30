package categoria;

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
 * 
 * Entidad que representa la Categoria del Vehiculo.
 * En ella se encuentran propiedades tales como Caja, Traccion, Cantidad de Puertas y Precio.
 *
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({
@javax.jdo.annotations.Query(name="listado_categoria", language="JDQL",value="SELECT FROM dom.categoria.Categoria WHERE activo== :true ")})
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("CATEGORIA")
@AutoComplete(repository=CategoriaServicio.class, action="autoComplete")

public class Categoria {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * @return String
	 */
	public String iconName(){
		return "categoria";
	}
	/**
	 * Enumeracion que determina los tipos de caja del vehiculo.
	 *
	 */
	public static enum Caja {
    	AUTOMATICA, MANUAL;
    }
	/**
	 * Enumeracion que determina la traccion del vehiculo.
	 *
	 */	
    public static enum Traccion {
    	CUATROx4 , CUATROx2 ;
    }
    /**
     * Titulo identificatorio en la UI.
     * Retorna el nombre de la Categoria.
     * 
     * @return String
     */
    public String title() {
		final TitleBuffer buf = new TitleBuffer();
        buf.append(getNombre());
        return buf.toString();
	}     
  
    private String ownedBy;
    /**
     * Retorna el nombre del usuario.
     */
    @Hidden	
	public String getOwnedBy() {
	    return ownedBy;
	}
    /**
     * Se setea el nombre del usuario.
     * @param ownedBy
     */
	public void setOwnedBy(final String ownedBy) {
	    this.ownedBy = ownedBy;
	}  
    
	private String categoria;
	/**
     * Retorna el nombre de la Categoria.
     * @return String
     */
	@Named("Categoria")
	@MemberOrder(sequence="1")
	public String getNombre() {
		return categoria;
	}
	/**
	 * Se setea el nombre de la Categoria.
	 * @param categoria
	 */
	public void setNombre(final String categoria) {
		this.categoria=categoria;
	}	
	
    private int cantPuertas ;
    /**
     * Retorna la cantidad de puertas del vehiculo.
     * @return int
     */
	@MemberOrder(sequence="2")
	public int getCantPuertas() {
		return cantPuertas;
	}
	/**
	 * Se setean la cantidad de puertas del vehiculo.
	 * @param cantPuertas
	 */
	public void setCantPuertas(final int cantPuertas) {
		this.cantPuertas=cantPuertas;
	}	
    
	private int cantPlazas ;
	/**
	 * Retorna la cantidad de plazas del vehiculo.
	 * @return int
	 */
	@Named("CantidadDePLazas")
	@MemberOrder(sequence="3")
	public int getCantPlazas() {
		return cantPlazas;
	}
	/**
	 * Se setea la cantidad de plazas del vehiculo.
	 * @param cantPlazas
	 */
	public void setCantPlazas(final int cantPlazas) {
		this.cantPlazas=cantPlazas;
	}

	private Caja caja;
	/**
	 * Retorna el tipo de caja que tiene el vehiculo.
	 * @return Caja
	 */
	@DescribedAs("Señala tipo de caja del vehiculo.")
	@Named("Caja")
	@MemberOrder(sequence="4")
	public Caja getCaja() {
		return caja;
	}	
	/**
	 * Se setea el tipo de caja que tiene el vehiculo.
	 * @param caja
	 */
	public void setCaja(final Caja caja) {
		this.caja=caja;
	}
	
	private Traccion traccion;
	/**
	 * Retorna el tipo de traccion que tiene el vehiculo.
	 * @return Traccion
	 */
	@DescribedAs("Señala tipo de traccion del vehiculo.")
	@Named("Traccion")
	@MemberOrder(sequence="5")	
	public Traccion getTraccion() {
		return traccion;
	}	
	/**
	 * Se setea el tipo de traccion que tiene el vehiculo.
	 * @param traccion
	 */
	public void setTraccion(final Traccion traccion) {
		this.traccion=traccion;
	}

	private String precio;
	/**
	 * Retorna el precio definido para la Categoria.
	 * @return String
	 */
	@DescribedAs("El precio de la categoria.")
	@Named("Precio")
    @MemberOrder(sequence = "6")
	public String getPrecio() {
		return precio;
	}
	/**
	 * Se setea el precio definido para la Categoria.
	 * @param precio
	 */
	public void setPrecio(final String precio) {
		this.precio=precio;
	}
	
   	private boolean activo;
   	/**
   	 * Retorna si la categoria está activa o no.
   	 * @return boolean
   	 */
   	@Hidden
   	@Named("Activo")
   	@MemberOrder(sequence="7")
    public boolean getActivo() {
   		return activo; 
   	}  
   	/**
   	 * Se setea la categoria.
   	 * @param activo
   	 */
   	public void setActivo(final boolean activo) {
   		this.activo=activo; 
   	}
    /**
     * Accion que borra la Categoria especificado.
     * Provisto por el Framework.
     */
   	@Named("Borrar")
    public void remove() {
        setActivo(false);
        container.informUser("El registro a sido eliminado");
	}		
	
    private DomainObjectContainer container;
	/**
	 * injected: DomainObjectContainer
	 * @return DomainObjectContainer
	 */
    protected DomainObjectContainer getContainer() {
    	return container;
    }
    /**
     * injected: DomainObjectContainer
     * @param container
     */
    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
    } 
}
