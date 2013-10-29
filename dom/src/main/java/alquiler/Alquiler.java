package alquiler;

import java.util.Date;
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
import org.apache.isis.applib.util.TitleBuffer;
import org.apache.isis.applib.annotation.MemberGroups;

import autos.Auto;
import categoria.Categoria;
import cliente.Cliente;


@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@MemberGroups({"Cliente", "Categoria","Auto","Medio de Pago", "Fecha de Alquiler"})
@AutoComplete(repository=AlquilerServicio.class, action="autoComplete")


@ObjectType("ALQUILER")
public class Alquiler {
	
	public static enum TipoPago{
		EFECTIVO, CHEQUE, TARJETA_CREDITO, TARJETA_DEBITO;
	}
	
	@Named("Alquiler")
	// {{ Identification on the UI	
	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append("ALQUILER");	       
		return buf.toString();	
	}
	// }}
	
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
	
	// {{ Categoria	
	private Categoria categoria;
	@DescribedAs("La categoria del vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(name="Categoria",sequence="1")	
	public Categoria getCategoria() {
		return categoria;
	}	
	public void setCategoria(final Categoria categoria)	{		
		this.categoria=categoria;
	}	
	// }}
	
	// {{ Auto	
	private Auto auto;
	@DescribedAs("El vehiculo.")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(name="Auto",sequence="1")	
	public Auto getAuto() {
		return auto;
	}	
	public void setAuto(final Auto auto)	{		
		this.auto=auto;
	}	
	// }}
	
	// {{ Cliente		
	private Cliente clienteId;
	@DescribedAs("Numero de CUIL/CUIT")
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(name="Cliente",sequence="2")	
	public Cliente getClienteId() {
		return clienteId;
	}	
	public void setClienteId(final Cliente clienteId)	{		
		this.clienteId=clienteId;
	}	
	// }}
	
	// {{ Tipo de Pago
	private TipoPago tipoPago;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(name="Medio de Pago",sequence="1")	
	public TipoPago getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(final TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}
	// }}	
	
	// {{ Precio 
	private float precio;
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(name="Medio de Pago",sequence="2")	
	public float getPrecioAlquiler(){
		return precio;
	}
	public void setPrecioAlquiler(final float precio){
		this.precio=precio;
	}
	// }}
	
	// {{ Numero de Recibo
	private int recibo;
	
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@MemberOrder(name="Medio de Pago",sequence="3")
	public int getNumeroRecibo() {
		return recibo;
	}
	public void setNumeroRecibo(final int recibo) {
		this.recibo = recibo;
	}
	// }}	
	
  	// {{ Fecha de Alquiler del vehiculo
    private Date fechaAlq;
    @DescribedAs("Señala la fecha de alquiler del vehiculo.")
    @MemberOrder(name="Fecha de Alquiler",sequence="1")
    public Date getFechaAlquiler() {
        return fechaAlq; 
    }
    public void setFechaAlquiler(final Date fechaAlq) {
        this.fechaAlq= fechaAlq; 
    }    
    public void clearFechaAlquiler() {
        setFechaAlquiler(null); 
    }  
    // }}
	
  	// {{ Fecha de Devolucion del vehiculo
    private Date fechaDev;
    @DescribedAs("Señala la fecha de devolución del vehiculo.")
    @MemberOrder(name="Fecha de Alquiler",sequence="2")
    public Date getFechaDevolucion() {
        return fechaDev; 
    }
    public void setFechaDevolucion(final Date fechaDev) {
        this.fechaDev= fechaDev; 
    }    
    public void clearFechaDevolucion() {
        setFechaAlquiler(null); 
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
   	public void remove(){
   		setActivo(false);
   	}   	
   	//}}
	
    // {{ injected: DomainObjectContainer
    @SuppressWarnings("unused")
    private DomainObjectContainer container;
    
    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
       
    }
    // }}	
}

