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
	public String iconName(){
		return "categoria";
	}
	//{{ENUM para tipo de caja
	public static enum Caja {
    	AUTOMATICA, MANUAL;
    }
	//}}
	//{{ENUM para traccion
    public static enum Traccion {
    	CUATROx4 , CUATROx2 ;
    }
    //}}
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
	//@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Categoria")
	@MemberOrder(sequence="1")
	public String getNombre() {
		return categoria;
	}	
	public void setNombre(final String categoria) {
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
	public void setCantPuertas(final int cantPuertas) {
		this.cantPuertas=cantPuertas;
	}	
    //}}
	//{{ Cantidad de Plazas
	private int cantPlazas ;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("CantidadDePLazas")
	@MemberOrder(sequence="3")
	public int getCantPlazas() {
		return cantPlazas;
	}	
	public void setCantPlazas(final int cantPlazas) {
		this.cantPlazas=cantPlazas;
	}
	//}}
	//{{ Caja
	private Caja caja;
	@DescribedAs("Señala tipo de caja del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Caja")
	@MemberOrder(sequence="4")
	public Caja getCaja() {
		return caja;
	}	
	public void setCaja(final Caja caja) {
		this.caja=caja;
	}
	//}}
	//{{ Traccion
	private Traccion traccion;
	@DescribedAs("Señala tipo de traccion del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Traccion")
	@MemberOrder(sequence="5")	
	public Traccion getTraccion() {
		return traccion;
	}	
	public void setTraccion(final Traccion traccion) {
		this.traccion=traccion;
	}
	//}}
	
	//{{ Precio
	private String precio;
	@DescribedAs("El precio de la categoria.")
    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Precio")
    @MemberOrder(sequence = "6")
	public String getPrecio() {
		return precio;
	}
	public void setPrecio(final String precio) {
		this.precio=precio;
	}
	//}}
	// {{ Activo
   	private boolean activo;
   	@Hidden
   	@Named("Activo")
   	@MemberOrder(sequence="7")
    public boolean getActivo() {
   		return activo; 
   	}   	
   	public void setActivo(final boolean activo) {
   		this.activo=activo; 
   	}
   	// }}   		
    // {{ Remove (action)
   	@Named("Borrar")
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
