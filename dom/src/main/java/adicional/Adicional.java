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


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Query(name="listaAdicionales", language="JDOQL",value="SELECT FROM adicional.Adicional where activo==true")
@AutoComplete(repository=AdicionalServicio.class,action="autoComplete")
@ObjectType("ADICIONAL")
@Audited

public class Adicional {
    // {{ Identification on the UI
	public String iconName(){
		return "adicional";
	}
	@Named("Adicional")
    public String title() {		
        return getNombre().toString();
	}     
    // }}
	private String nombre;
	@Hidden
	@Named("Adicional")
	@MemberOrder(sequence="1")
    public String getNombre(){
    	return nombre;
    }
    public void setNombre(final String nombre){
    	this.nombre=nombre;
    }
    
    private String descripcion;
    @Named("Descripcion")
    @MultiLine(numberOfLines=3)
    @MemberOrder(sequence="2")
    public String getDescripcion() {
            return descripcion;
    }
    public void setDescripcion(final String descripcion) {
            this.descripcion = descripcion;
    }
	
    private float precio;
    @Named("Precio x dia")
    @MemberOrder(sequence="3")
    public float getPrecio() {
        return precio;
    }
    public void setPrecio(final float precio) {
        this.precio = precio;
    }
    private Alquiler alquiler;
    @Hidden
    @NotPersisted
    @Named("Alquiler")
    @MemberOrder(sequence="5")
    public Alquiler getAlquiler() {
            return alquiler;
    }
    public void setAlquiler(final Alquiler alquiler) {
            this.alquiler = alquiler;
    }
    // {{ Campo Activo
   	private boolean activo;
   	@Hidden
   	@Named("Activo")
   	@MemberOrder(sequence="6")
   	public boolean getActivo() {
   		return activo; 
   	}   	
   	public void setActivo(boolean activo){
   		this.activo=activo; 
   	}	
    // }}   	   	
   	//{{ Remove   	
    @Named("Borrar")
   	public void remove(){
   		setActivo(false);
   	}   	
   	//}}  
    private String ownedBy;
	@Hidden 
	public String getOwnedBy() {
	    return ownedBy;	
	}
	public void setOwnedBy(final String ownedBy){
	    this.ownedBy = ownedBy;	
	}	
	// }}
    // {{ injected: DomainObjectContainer
    @SuppressWarnings("unused")
	private DomainObjectContainer container;
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
       this.container = container;
    }   
}
