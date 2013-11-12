package disponibles;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;

import categoria.Categoria;
import alquiler.Alquiler;



@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries( {
    @javax.jdo.annotations.Query(
            name="Disponibles", language="JDOQL",
            value="SELECT FROM dom.disponibles.Disponibles")
})
@ObjectType("Disponibles")
@Audited
public class Disponible {
	public String iconName(){
		return "auto";
	}
	@Named("Auto")
	// {{ Identification on the UI
	public String title() {		
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getPatente());	       
		return buf.toString();	
	}
	// }}
    // {{	
    private String auto;
    @Hidden
    @Named("Patente")
    public String getPatente(){
    	return auto;
    }
    public void setPatente(final String auto){
    	this.auto=auto;
    }
    // }}
    // {{
    private boolean seleccionar;    
    @Named("Seleccionada")
    public boolean isEstaSeleccionada() {
            return seleccionar;
    }
    public void setEstaSeleccionada(final boolean seleccionar) {
            this.seleccionar = seleccionar;
    }
    // }}
    // {{
    @Named("Fecha")
    public String getFechaString() {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(getFecha());
    }
    private Date fecha;
    @Hidden
    public Date getFecha() {
            return fecha;
    }
    public void setFecha(final Date fecha) {
            this.fecha = fecha;
    }
    // }}
    // {{
    private Categoria categoria;
    @Named("Categoria")
    public Categoria getCategoria(){
    	return categoria;
    }
    public void setCategoria(final Categoria categoria){
    	this.categoria=categoria;
    }
    // }}
    // {{
    private Alquiler alquiler;
    @Named("Estado Alquiler")
    public Alquiler getAlquiler(){
    	return alquiler;
    }
    public void setAlquiler(final Alquiler alquiler){
    	this.alquiler=alquiler;
    }
    // }}
    // {{
    @Named("Seleccionar")
    @Bulk    
    public Disponible reserva(){
    	if (getAlquiler()==null){
    		if(isEstaSeleccionada())
    			setEstaSeleccionada(false);
    		else
    			setEstaSeleccionada(true);  		
    	}    	
    	return this;    	
    }
    // }}    
    public String disableReserva() {
    return seleccionar ? "Ya esta seleccionada!" : null;
    }  
    // {{Modelo
    private String modelo;
    @Named("Modelo")
    public String getModeloAuto(){
    	return modelo;
    }
    public void setModeloAuto(final String modelo){
    	this.modelo=modelo;
    }
    // }}
    // {{ Inyeccion del Servicio
    @SuppressWarnings("unused")
	private DisponibleServicio servicio;
    public void injectDisponiblesServicio(final DisponibleServicio serv){
    	this.servicio=serv;
    }    
    // {{ injected: DomainObjectContainer 
	@SuppressWarnings("unused")
	private DomainObjectContainer container;
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
     this.container = container;
    }
}