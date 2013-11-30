package adicional;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import alquiler.Alquiler;

/**
 * Entidad que representa los gastos adicionales que pueden haber en un Alquiler. 
 * @see alquiler.Alquiler
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="listaAdicionales", language="JDOQL",value="SELECT FROM adicional.Adicional where activo==true")
@AutoComplete(repository=AdicionalServicio.class,action="autoComplete")
@ObjectType("ADICIONAL")
@Audited
public class Adicional {

	/**
	 * Nombre del icono de la entidad Adicional.
	 * @return String
	 */
	
	public String iconName(){
		return "adicionales";
	}
	/**
	 * Titulo Identificatorio en la UI
	 * Retorna el nombre de la entidad.
	 * @return String
	 */
	@Named("Adicional")
    public String title() {		
        return getNombre().toString();
	}    
	
	private String nombre;
	/**
	 * Retorna el nombre del Adicional 
	 * @return String
	 */	
	@Hidden
	@Named("Adicional")
	@MemberOrder(sequence="1")
    public String getNombre(){
    	return nombre;
    }
	/**
	 * Setea el nombre del adicional
	 * @param nombre
	 */
    public void setNombre(final String nombre){
    	this.nombre=nombre;
    }
   
    private String descripcion;
    /**
     * Retorna la descripcion del Adicional
     * @return String
     */
    @Named("Descripcion")
    @MultiLine(numberOfLines=3)
    @MemberOrder(sequence="2")
    public String getDescripcion() {
            return descripcion;
    }
    /**
     * Setea la descripcion del Adicional
     * @param descripcion
     */
    public void setDescripcion(final String descripcion) {
            this.descripcion = descripcion;
    }
	
    private String precio;
    /**
     * Retorna el precio por d&iacute;a del Adicional
     * @return String
     */
    @Named("Precio x dia")
    @MemberOrder(sequence="3")
    public String getPrecio() {
        return precio;
    }
    /**
     * Setea el precio del Adicional
     * @param precio
     */
    public void setPrecio(final String precio) {
        this.precio = precio;
    }
    private Alquiler alquiler;
    /**
     * Retorno del alquiler.
     * @return alquiler.Alquiler
     */
    @Hidden
    @NotPersisted
    @Named("Alquiler")
    @MemberOrder(sequence="5")
    public Alquiler getAlquiler() {
            return alquiler;
    }
    /**
     * Setea el alquiler
     * @param alquiler
     */
    public void setAlquiler(final Alquiler alquiler) {
            this.alquiler = alquiler;
    }
   	private boolean activo;
   	/**
   	 * Retorna si el Adicional est&aacute; activo o no
   	 * @return boolean
   	 */
   	@Hidden
   	@Named("Activo")
   	@MemberOrder(sequence="6")
   	public boolean getActivo() {
   		return activo; 
   	}   	
   	/**
   	 * Se Setea el activo
   	 * @param activo
   	 */
   	public void setActivo(boolean activo){
   		this.activo=activo; 
   	}	
	
    @Named("Borrar")
    /**
     * Accion que provee el framework para eliminar un objeto.
     */
   	public void remove(){
   		setActivo(false);
   		container.informUser("El registro a sido eliminado");
   	}   	 
    private String usuario;
    /**
     * Retorna el nombre del usuario
     * @return String
     */
	@Hidden 
	public String getUsuario() {
	    return usuario;	
	}
	/**
	 * Setea el nombre del usuario
	 * @param ownedBy
	 */
	public void setUsuario(final String usuario){
	    this.usuario = usuario;	
	}	
	 
	private DomainObjectContainer container;
    /**
	 * injected: DomainObjectContainer
	 */
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
       this.container = container;
    }   
}
