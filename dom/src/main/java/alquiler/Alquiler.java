package alquiler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;

import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotPersisted;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.annotation.MemberGroups;

import cliente.Cliente;
import disponibles.AutoPorFecha;



@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.APPLICATION)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@MemberGroups({"Estado","Datos del Alquiler","Autos"})
@javax.jdo.annotations.Query(name="traerAlquileres", language="JDOQL",value="SELECT FROM alquiler.Alquiler2 order by numero asc")
@AutoComplete(repository=AlquilerServicio.class, action="autoComplete")
@Audited 
@ObjectType("ALQUILER")


public class Alquiler {
    
	public static enum EstadoAlquiler{
		RESERVADO, EN_PROCESO, FINALIZADO;
	}
	
	public static enum TipoPago{
		EFECTIVO, CHEQUE, TARJETA_CREDITO, TARJETA_DEBITO;
	}
	
	//{{Titulo
    public String title(){
            return getEstado().toString();        
    }
    //}}
    
    //{{Numero de la reserva
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long numero;
    @Disabled
    @Named("Nro Alquiler")
    @MemberOrder(name="Datos del Alquiler",sequence="1")
    public Long getNumero() {
            return numero;
    }    
    public void setNumero(final Long numero) {
            this.numero = numero;
    }
    //}}
    
    private String nombreEstado;    
    @Named("Estado")
    @NotPersisted
    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Estado",sequence="1")
    public String getNombreEstado() {
            nombreEstado = getEstado().toString();
            return nombreEstado;
    }    
    private EstadoAlquiler estado;    
    @Hidden
    public EstadoAlquiler getEstado() {
            return estado;
    }
    public void setEstado(final EstadoAlquiler estado) {
            this.estado = estado;
    }
    //}}
    
    //{{Fecha en la que se realiza la reserva
    @Named("Fecha")
    @MemberOrder(name="Datos del Alquiler",sequence="2")
    public String getFechaString() {
            if(getFecha() != null) {
                    SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                    return formato.format(getFecha());
            }
            return null;
    }    
    private Date fecha;
    @Hidden
    public Date getFecha() {
            return fecha;
    }
    public void setFecha(final Date fecha) {
            this.fecha = fecha;
    }
    //}}

	// {{ Precio 
	private float precio;
    @Named("Precio")
    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Datos del Alquiler",sequence="3")
	public float getPrecioAlquiler(){
		return precio;
	}
	public void setPrecioAlquiler(final float precio){
		this.precio=precio;
	}	
	// }}
	
	// {{ Tipo de Pago
	private TipoPago tipoPago;
    @Named("Tipo de Pago")
    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Datos del Alquiler",sequence="4")
	public TipoPago getTipoPago() {
		return tipoPago;
	}
	public void setTipoPago(final TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}
	// }}	
	
	// {{ Numero de Recibo
	private int recibo;
    @Named("Nro Recibo")
    @Hidden(where=Where.ALL_TABLES)
    @MemberOrder(name="Datos del Alquiler",sequence="5")
	public int getNumeroRecibo() {
		return recibo;
	}
	public void setNumeroRecibo(final int recibo) {
		this.recibo = recibo;
	}
	// }}	
	
	// {{ Lista de Autos 
	@Persistent(mappedBy="alquiler")	
	private List<AutoPorFecha> autos = new ArrayList<AutoPorFecha>();
    public List<AutoPorFecha> getAutos() {
        return autos;
    }
    public void setAutos(List<AutoPorFecha> listaAutos) {
    	this.autos = listaAutos;
    }
    @Named("Borrar")
    @MemberOrder(name="Autos",sequence="1")
    public Alquiler removeFromAutos(final AutoPorFecha auto) {
            autos.remove(auto);
            container.removeIfNotAlready(auto);
            return this;
    }
    @Hidden
    public void addToAutos(AutoPorFecha auto) {
     if(auto == null || autos.contains(auto)) {
             return;
     }
     auto.setAlquiler(this);
     autos.add(auto);
    }
    // }}
    
	// {{ Cliente		
	private Cliente clienteId;
	@DescribedAs("Numero de CUIL/CUIT")
	@Disabled
	@Named("CUIL/CUIT")
	@MemberOrder(name="Cliente",sequence="1")	
	public Cliente getClienteId() {
		return clienteId;
	}	
	public void setClienteId(final Cliente clienteId)	{		
		this.clienteId=clienteId;
	}	
	// }}
	
	// {{ Nombre Cliente
	private String nombre;
	@Disabled
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Nombre")
	@MemberOrder(name="Cliente",sequence="2")	
	public String getNombreCliente() {
		return nombre;
	}
	public void setNombreCliente(String nombre) {
		this.nombre = nombre;
	}
	// }}

	// {{ Apellido Cliente
	private String apellido;
	@Disabled
	@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
	@Named("Apellido")
	@MemberOrder(name="Cliente",sequence="3")	
	public String getApellidoCliente() {
		return apellido;
	}
	public void setApellidoCliente(String apellido) {
		this.apellido = apellido;
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
	
	@MemberOrder(name="Estado",sequence="2")
	public Alquiler enProceso(){
		setEstado(EstadoAlquiler.EN_PROCESO);
		return this;
	}
    public String disableEnProceso() {
        if(getEstado() == EstadoAlquiler.RESERVADO) {
                return null;
        }
        else {
                if(getEstado() == EstadoAlquiler.EN_PROCESO) {
                        return "El Alquiler ya se encuentra EN PROCESO";
                }
                else {
                        return "Tiene que estar RESERVADO para pasar a EN PROCESO";
                }
        }
    }
    
	@MemberOrder(name="Estado",sequence="3")
	public Alquiler finalizado(){
		setEstado(EstadoAlquiler.FINALIZADO);
		return this;
	}
    public String disableFinalizado() {
        if(getEstado() == EstadoAlquiler.FINALIZADO) {
                return null;
        }
        else {
                if(getEstado() == EstadoAlquiler.FINALIZADO) {
                        return "El Alquiler ya se encuentra FINALIZADO";
                }
                else {
                        return "Tiene que estar EN PROCESO para pasar FINALIZAR";
                }
        }
    }
    
    // {{ injected: DomainObjectContainer
    private DomainObjectContainer container;
    
    public void setDomainObjectContainer(final DomainObjectContainer container) {
        this.container = container;
       
    }
    // }}    
}


