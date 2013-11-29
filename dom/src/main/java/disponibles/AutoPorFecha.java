package disponibles;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;
import org.joda.time.LocalDate;
import alquiler.Alquiler;
import categoria.Categoria;
/**
 * 
 * Clase que devuelve una instancia por cada vehiculo y cada d&iacute;a entre un rango de fechas, manteniendo la relacion entre ambas.
 * 
 * @see Alquiler
 * @see Categoria
 *
 */
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
	 /**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * @return String
	 */
	public String iconName(){
		return "auto";
	}
	
	/**
	 * Titulo identificatorio en la UI
	 * @return String
	 */
	@Named("Auto")
	public String title() {		
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getPatente());	       
		return buf.toString();	
	}
	
    private LocalDate fecha;
    @Named("Fecha")
    /**
     * Retorno de la fecha
     * @return LocalDate
     */
    public LocalDate getFecha() {
            return fecha;
    }
    /**
     * Se setea la fecha.
     * @param fecha
     */
    public void setFecha(final LocalDate fecha) {
            this.fecha = fecha;
    }
   
    private String auto;
    /**
     * Retorno la patente del vehiculo.
     * @return String
     */
    @Disabled
    @Named("Auto")
    public String getPatente(){
    	return auto;
    }
    /**
     * Se setea la patente del vehiculo.
     * @param auto
     */
    public void setPatente(final String auto){
    	this.auto=auto;
    }
    
    private Categoria categoria;
    /**
     * Retorno de la categoria del vehiculo.
     * @return Categoria
     */
    @Disabled
    @Named("Categoria")
    public Categoria getCategoria(){
    	return categoria;
    }
    /**
     * Se setea la categoria del vehiculo.
     * @param categoria
     */
    public void setCategoria(final Categoria categoria){
    	this.categoria=categoria;
    }
   
    private Alquiler alquiler;
    @Named("Estado Alquiler")
    /**
     * Retorno del Estado del Alquiler
     * @return Alquiler
     */
    public Alquiler getAlquiler(){
    	return alquiler;
    }
    /**
     * Se setea el Estado del Alquiler.
     * @param alquiler
     */
    public void setAlquiler(final Alquiler alquiler){
    	this.alquiler=alquiler;
    }
 
    private String modelo;
    /**
     * Retorno del modelo del vehiculo.
     * @return String
     */
    @Disabled
    @Named("Modelo")
    public String getModeloAuto(){
    	return modelo;
    }
    /**
     * Se setea el modelo del vehiculo.
     * @param modelo
     */
    public void setModeloAuto(final String modelo){
    	this.modelo=modelo;
    }
   
    @SuppressWarnings("unused")
	private DomainObjectContainer container;    
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
    	this.container=container;
    } 
}