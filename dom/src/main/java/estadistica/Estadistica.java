package estadistica;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
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
	// {{ Fecha
	/*
    @Named("Fecha")
    public String getFechaString() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(getFecha());
    }
    */
    private LocalDate fecha;
    @Hidden
    public LocalDate getFecha() {
            return fecha;
    }
    public void setFecha(final LocalDate fecha) {
            this.fecha = fecha;
    }
    // }}
    // {{	
    private String auto;
    @Disabled
    @Named("Auto")
    public String getPatente(){
    	return auto;
    }
    public void setPatente(final String auto){
    	this.auto=auto;
    }
    // }}
    // {{
    private Categoria categoria;
    @Disabled
    @Named("Categoria")
    public Categoria getCategoria(){
    	return categoria;
    }
    public void setCategoria(final Categoria categoria){
    	this.categoria=categoria;
    }
    // }}
    
    // {{
    private String modelo;
    @Disabled
    @Named("Modelo")
    public String getModeloAuto(){
    	return modelo;
    }
    public void setModeloAuto(final String modelo){
    	this.modelo=modelo;
    }
    // }}
    
    // {{
    private int cantAlq;
    @Disabled
    @Named("Cantidad de Alquileres")
    public int getCantAlq(){
    	return cantAlq;
    }
    public void setCantAlq(final int cantAlq){
    	this.cantAlq=cantAlq;
    }
    // }}
    
    @SuppressWarnings("unused")
	private DomainObjectContainer container;    
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
    }
    //}}
}

