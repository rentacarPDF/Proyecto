package disponibles;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;

import alquiler.Alquiler;
import categoria.Categoria;



@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(name="auto_para_alquilar", language="JDOQL",value="SELECT FROM disponiles.Disponibles WHERE estaSeleccionada == true"),
        @javax.jdo.annotations.Query(name="auto_libre", language="JDOQL",value="SELECT FROM disponibles.Disponibles WHERE estaSeleccionada == false"),
})
@ObjectType("AutosPorFecha")
@AutoComplete(repository=DisponibleServicio.class,action="autosAlquilados")
@Audited

public class AutoPorFecha {
	
	public String iconName(){
		return "auto";
	}
	
	// {{ Identification on the UI
	@Named("Auto")
	public String title() {		
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getPatente());	       
		return buf.toString();	
	}
	// }}
	// {{ Fecha
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
    private String auto;
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
    private String modelo;
    @Named("Modelo")
    public String getModeloAuto(){
    	return modelo;
    }
    public void setModeloAuto(final String modelo){
    	this.modelo=modelo;
    }
    // }}
    @SuppressWarnings("unused")
	private DomainObjectContainer container;    
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
    }
    //}}
}