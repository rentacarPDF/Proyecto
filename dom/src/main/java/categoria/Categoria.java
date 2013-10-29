package categoria;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.util.TitleBuffer;


import com.google.common.base.Objects;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({
@javax.jdo.annotations.Query(name="listado_categoria", language="JDQL",value="SELECT FROM dom.categoria.Categoria WHERE activo== :true ")})
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")

@ObjectType("CATEGORIA")
@AutoComplete(repository=CategoriaServicio.class, action="autoComplete")

public class Categoria {

    public static enum Caja {
    	AUTOMATICA, MANUAL;
    }
    
    public static enum Traccion {
    	Cuatrox4 , Cuatrox2 ;
    }

    // {{ Identification on the UI
    public String title() {
		final TitleBuffer buf = new TitleBuffer();
        buf.append(getNombre());
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
    //}}
	
	//{{ Nombre de Categoria
	private String categoria;
	
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence="1")
	public String getNombre() {
		return categoria;
	}	
	public void setNombre(String categoria) {
		this.categoria=categoria;
	}	
	//}}
	
	//{{ Cantidad de puertas
    private int cantPuertas ;	
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence="2")
	public int getCantPuertas() {
		return cantPuertas;
	}	
	public void setCantPuertas(int cantPuertas) {
		this.cantPuertas=cantPuertas;
	}	
    //}}
	
	//{{ Cantidad de Plazas
	private int cantPlazas ;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence="3")
	public int getCantPlazas() {
		return cantPlazas;
	}	
	public void setCantPlazas(int cantPlazas) {
		this.cantPlazas=cantPlazas;
	}
	//}}
	
	//{{ Caja
	private Caja caja;
	@DescribedAs("Señala tipo de caja del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence="4")
	public Caja getCaja() {
		return caja;
	}	
	public void setCaja(Caja caja) {
		this.caja=caja;
	}
	//}}
	
	//{{ Traccion
	private Traccion traccion;
	@DescribedAs("Señala tipo de traccion del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence="5")	
	public Traccion getTraccion() {
		return traccion;
	}	
	public void setTraccion(Traccion traccion) {
		this.traccion=traccion;
	}
	//}}
	
	//{{ Precio
	private int precio;
	@DescribedAs("El precio de la categoria.")
    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(sequence = "6")
	public int getPrecio() {
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio=precio;
	}
	//}}
	
	// {{ Activo
   	private boolean activo;
   	@Hidden
   	@MemberOrder(sequence="7")
    public boolean getActivo() {
   		return activo; 
   	}   	
   	public void setActivo(boolean activo) {
   		this.activo=activo; 
   	}
   	// }}   		
    // {{ Remove (action)
    public void remove() {
        setActivo(false);
	}
    // }}
	
	// {{ Filtro 
   	public static Filter<Categoria> thoseOwnedBy(final String currentUser) {
        return new Filter<Categoria>() {
        @Override
        public boolean accept(final Categoria categoria) {
                return Objects.equal(categoria.getOwnedBy(), currentUser);
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
