package autos;


import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.annotation.Named;

import categoria.Categoria;
import com.google.common.base.Objects;
import marca.Marca;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({
@javax.jdo.annotations.Query(name="listadoAutosActivos", language="JDOQL",value="SELECT FROM autos.Auto WHERE activo==true"),
@javax.jdo.annotations.Query(name="findAutos", language="JDOQL",value="SELECT FROM autos.Auto WHERE patente == :patente ")})
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")

@ObjectType("AUTO")
@AutoComplete(repository=AutoServicio.class, action="autoComplete")


public class Auto {
	
	public static enum TipoCombustible {
		NAFTA, DIESEL; 
	}
	public static enum Seguro{
		LA_SEGUNDA, MAPFRE, LA_PATRONAL, LA_CAJA, ZURICH; 
	}	
	
	// {{ Patente	
	private String patente;
	@DescribedAs("El dominio del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*") // words, spaces and selected punctuation	
	@MemberOrder(sequence="1")
	@Title
	@Hidden
	@Named("Patente")
	public String getPatente(){
		return patente; 
	}	
	public void setPatente(String patente){
		this.patente=patente; 
	} 	
	
	// {{ {{ OwnedBy (property)	
	private String ownedBy;
	@Hidden 
	public String getOwnedBy() {
	    return ownedBy;	
	}
	public void setOwnedBy(final String ownedBy){
	    this.ownedBy = ownedBy;	
	}	
	// }}
	
	// {{ Marca
	@Persistent
	private Marca marca;
	@DescribedAs("La marca del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence="2")	
	@Named("Marca")
	public Marca getMarca() {
		return marca;
	}	
	public void setMarca(final Marca marca)	{		
		this.marca=marca;
	}	
	// }}
	
	// {{ Modelo
	private String modelo;
    @DescribedAs("El modelo del vehiculo.")
    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(sequence = "3")
    @Named("Modelo")
	public String getModelo(){
		return modelo;
	}
    public void setModelo(final String modelo) {
        this.modelo = modelo; 
    }
    // }}
    
    // {{ Año    
    private int ano;
    @DescribedAs("El año del vehiculo.")
    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(sequence = "4")
    @Named("Año")
    public int getAno() {
        return ano; 
    }
    public void setAno(final int ano) {
        this.ano = ano; 
    }   
    // }}  
    
	// {{ Categoria
	@Persistent
	private Categoria categoria;
	@DescribedAs("La categoria del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(sequence="5")	
	@Named("Categoria")
	public Categoria getCategoria() {
		return categoria;
	}	
	public void setCategoria(final Categoria categoria)	{		
		this.categoria=categoria;
	}	
	// }}
    
    // {{ Color
    private String color;
    @DescribedAs("El color del vehiculo.")
    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(sequence = "6")
    @Named("Color")
    public String getColor() {
        return color; 
    }
    public void setColor(final String color) {
        this.color = color; 
    }    
    // }}
    
    // {{ Kilometraje    
    private int kms;
    @DescribedAs("El kilometraje del vehiculo.")
    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(sequence = "7")
    @Named("Kilometraje")
    public int getKilometraje() {
        return kms; 
    }
    public void setKilometraje(final int kms) {
        this.kms = kms; 
    }    
    // }}
    
    // {{ Capacidad del Baul
    private int baul;
    @DescribedAs("La capacidad del baul del vehiculo.")
    @RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
    @MemberOrder(sequence = "8")
    @Named("Capacidad Baul (lts)")
    public int getCapacidadBaul() {
        return baul; 
    }
    public void setCapacidadBaul(final int baul) {
        this.baul = baul; 
    }     
    // }}
    
    // {{ Tipo de Combustible
 	private TipoCombustible combustible;
 	@DescribedAs("El tipo de combustible del vehiculo.")
 	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
 	@MemberOrder(sequence="9")
 	@Named("Tipo Combustible")
 	public TipoCombustible getTipoCombustible(){
 		return combustible; 
 	} 	
 	public void setTipoCombustible(TipoCombustible combustible){
 		this.combustible=combustible; 
 	}  	
 	// }}
  	
  	// {{ Fecha de Compra del vehiculo
    @Named("Fecha Compra")
    public String getFechaString() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(getFechaCompra());
    }
    private Date fechaCompra;
    @DescribedAs("Señala la fecha de compra del vehiculo.")
    @MemberOrder(sequence="10")
    @Hidden
    public Date getFechaCompra() {
        return fechaCompra; 
    }
    public void setFechaCompra(final Date fechaCompra) {
        this.fechaCompra= fechaCompra; 
    }    
    public void clearFechaCompra() {
        setFechaCompra(null); 
    }  
    // }}
    
    // {{ Seguro del vehiculo
   	private Seguro seguro;
   	@DescribedAs("Señala el seguro del vehiculo.")
   	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
   	@MemberOrder(sequence="11")
    @Named("Compañia Seguro")
   	public Seguro getSeguro() {
   		return seguro; 
   	}   	
   	public void setSeguro(Seguro seguro){
   		this.seguro=seguro; 
   	}	
    // }}
   	
    // {{ Campo Activo
   	private boolean activo;
   	@Hidden
   	@DescribedAs("Activo")
   	@MemberOrder(sequence="12")
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
   	
   	// {{ Filtro
   	public static Filter<Auto> thoseOwnedBy(final String currentUser){
        return new Filter<Auto>() {
        @Override
        public boolean accept(final Auto auto) {
                return Objects.equal(auto.getOwnedBy(), currentUser);
                
            }
        };
    }
   	// }}   	
        
    // {{ injected: DomainObjectContainer
    @SuppressWarnings("unused")
    private DomainObjectContainer container;
    
    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
       
    }
    // }}	
}