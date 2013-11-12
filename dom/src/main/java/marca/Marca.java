package marca;



import javax.jdo.annotations.IdentityType;
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


import javax.jdo.annotations.VersionStrategy;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({
@javax.jdo.annotations.Query(name="listado_marcas", language="JDQL",
							value="SELECT FROM dom.utilidades.Marca WHERE ownedBy == :ownedBy")})
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@ObjectType("MARCA")
@AutoComplete(repository=MarcaServicio.class, action="autoComplete")


public class Marca {
	public String iconName(){
		return "marca";
	}
	// {{ Identification on the UI	
	public String title(){
		final TitleBuffer buf = new TitleBuffer();		
		buf.append(getNombre());		       
		return buf.toString(); 
	}
	// }}
	// {{ OwnedBy (property)
	private String ownedBy;	
	@Hidden
	// not shown in the UI
	public String getOwnedBy() {
	    return ownedBy;	
	}
	public void setOwnedBy(final String ownedBy){
	    this.ownedBy = ownedBy; 
	}	    
	// }}	  
	//{{ Nombre de la Marca
	private String nombre;
	@DescribedAs("La marca del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Nombre")
	@MemberOrder(sequence="1")
	public String getNombre(){
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre=nombre;
	}
	// }}
	// {{ Campo Activo
   	private boolean activo;
   	@Hidden
   	@DescribedAs("Activo")   	
   	public boolean getActivo(){
   		return activo; 
   	}   	
   	public void setActivo(boolean activo){
   		this.activo=activo; 
   	}   	
   	public void remove(){
   		setActivo(false);   
   		container.warnUser("Se elimino el registro");
   	}   	
   	//}}
	// {{ Filtro
	public static Filter <Marca> thoseOwnedBy(final String currentUser){
        	return new Filter<Marca>(){
            @Override
            public boolean accept(final Marca marca){
                return Objects.equal(marca.getOwnedBy(), currentUser);
            }
        };
    }
	// }}
	// {{ injected: DomainObjectContainer
    private DomainObjectContainer container;
    public void setDomainObjectContainer(final DomainObjectContainer container){
        this.container = container;
    }
    // }}  	
}

