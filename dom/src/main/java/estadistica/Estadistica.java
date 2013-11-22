package estadistica;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;
import org.joda.time.LocalDate;


import categoria.Categoria;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(name="listarEstadisticas", language="JDOQL",value="SELECT FROM estadistica.Estadistica"),
        @javax.jdo.annotations.Query(name="borrarEstadisticas", language="JDOQL",value="DELETE FROM estadistica.Estadistica"),
})
@ObjectType("Estadistica")
@Audited

public class Estadistica {
	
	public static enum Mes{
		ENERO, FEBRERO, MARZO, ABRIL, MAYO, JUNIO, 
		JULIO, AGOSTO, SEPTIEMBRE, OCTUBRE, NOVIEMBRE, DICIEMBRE;
	}	
	public String iconName(){
		return "estadistica";
	}	
	// {{ Identification on the UI
	@Named("Estadistica")
	public String title() {		
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getPatente());	       
		return buf.toString();	
	}
	// }}

    // {{ Auto
    private String auto;
    @Disabled
    @Hidden
    @MemberOrder(sequence="6")
    @Named("Auto")
    public String getPatente(){
    	return auto;
    }
    public void setPatente(final String auto){
    	this.auto=auto;
    }
    // }}
    
    // {{ Categoria
    private Categoria categoria;
    @Disabled
    @MemberOrder(sequence="3")
    @Named("Categoria")
    public Categoria getCategoria(){
    	return categoria;
    }
    public void setCategoria(final Categoria categoria){
    	this.categoria=categoria;
    }
    // }}
    
    // {{ Modelo
    private String modelo;
    @Disabled
    @MemberOrder(sequence="1")
    @Named("Modelo")
    public String getModeloAuto(){
    	return modelo;
    }
    public void setModeloAuto(final String modelo){
    	this.modelo=modelo;
    }
    // }}
    
    // {{ Mes
    private String mes;
    @Disabled
    @MemberOrder(sequence="4")
    @Named("Mes de Alquiler")
    public String getMes(){
    	return mes;
    }
    public void setMes(final String mes){
    	this.mes=mes;
    }
    // }}
    
    // {{ Cantidad Alquileres
    private int cantAlq;
    @Disabled
    @MemberOrder(sequence="2")
    @Named("Cantidad de Alquileres")
    public int getCantAlq(){
    	return cantAlq;
    }
    public void setCantAlq(final int cantAlq){
    	this.cantAlq=cantAlq;
    }
    // }}
    
   	// {{ Seleccion Categoria
   	private boolean seleccionCat;
   	@Hidden
   	@MemberOrder(sequence="5")
   	@DescribedAs("SeleccionCategoria")
   	public boolean getSeleccionCategoria() {
   		return seleccionCat; 
   	}   	
   	public void setSeleccionCategoria(final boolean seleccionCat){
   		this.seleccionCat=seleccionCat; 
   	}	
    // }}  
    
    @SuppressWarnings("unused")
	private DomainObjectContainer container;    
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
    }
    //}}
}

