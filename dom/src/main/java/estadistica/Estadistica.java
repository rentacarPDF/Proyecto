package estadistica;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;
import categoria.Categoria;
/**
 * Clase que representa la Entidad Estad&iacute;stica de nuestro sistema.
 * Se realizan estadisticas de los alquileres en forma anual y por per&iacute;odos.
 */
@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy=VersionStrategy.VERSION_NUMBER, column="VERSION")
@javax.jdo.annotations.Queries({
 @javax.jdo.annotations.Query(name="listarEstadisticas",
        							language="JDOQL",
        							value="SELECT FROM estadistica.Estadistica"),
 @javax.jdo.annotations.Query(name="borrarEstadisticas",
        							language="JDOQL",
        							value="DELETE FROM estadistica.Estadistica"),
 @javax.jdo.annotations.Query(name="listaEstadisticasPorAuto",
        							language="JDOQL",
        							value="SELECT FROM estadistica.Estadistica where auto== :auto")
})
@ObjectType("Estadistica")
@Audited

public class Estadistica {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "estadistica";
	}
	/**
	 * Enumeracion que determina los meses.
	 *
	 */
	public static enum Mes{
		ENERO, FEBRERO, MARZO, ABRIL, MAYO, JUNIO, 
		JULIO, AGOSTO, SEPTIEMBRE, OCTUBRE, NOVIEMBRE, DICIEMBRE;
	}	
	
	/**
	 * Titulo identificatorio en la UI.
	 * @return String
	 */
	@Named("Estadistica")
	public String title() {		
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getPatente());	       
		return buf.toString();	
	}
    private String auto;
    /**
     * Retorna la patente del vehiculo.
     * @return String
     */
    @Disabled
    @Hidden
    @MemberOrder(sequence="6")
    @Named("Auto")
    public String getPatente(){
    	return auto;
    }
    /**
     * Setea la patente del vehiculo.
     * @param auto
     */
    public void setPatente(final String auto){
    	this.auto=auto;
    }
   
    private Categoria categoria;
    /**
     * Retorna la categoria del vehiculo.
     * @return Categoria
     */
    @Disabled
    @MemberOrder(sequence="3")
    @Named("Categoria")
    public Categoria getCategoria(){
    	return categoria;
    }
    /**
     * Setea la categoria del vehiculo.
     * @param categoria
     */
    public void setCategoria(final Categoria categoria){
    	this.categoria=categoria;
    }
 
    private String modelo;
    /**
     * Retorna el modelo del vehiculo.
     * @return String
     */
    @Disabled
    @MemberOrder(sequence="1")
    @Named("Modelo")
    public String getModeloAuto(){
    	return modelo;
    }
    /**
     * Setea el modelo del vehiculo.
     * @param modelo
     */
    public void setModeloAuto(final String modelo){
    	this.modelo=modelo;
    }
 
    private String mes;
    /**
     * Retorna el mes del alquiler del vehiculo.
     * @return String
     */
    @Disabled
    @MemberOrder(sequence="4")
    @Named("Mes de Alquiler")
    public String getMes(){
    	return mes;
    }
    /**
     * Setea el mes del alquiler del vehiculo.
     * @param mes
     */
    public void setMes(final String mes){
    	this.mes=mes;
    }
    
    private int cantidadAlquileres;
    /**
     * Retorna la cantidad de alquileres.
     * @return int
     */
    @Disabled
    @MemberOrder(sequence="2")
    @Named("Cantidad de Alquileres")
    public int getCantAlq(){
    	return cantidadAlquileres;
    }
    /**
     * Setea la cantidad de alquileres.
     * @param cantidadAlquileres
     */
    public void setCantAlq(final int cantidadAlquileres){
    	this.cantidadAlquileres=cantidadAlquileres;
    }
    
    @SuppressWarnings("unused")
	private DomainObjectContainer container; 
    /**
     * {{//Injected: DomainObjectContainer
     * @param container
     */
    public void injectDomainObjectContainer(final DomainObjectContainer container) {
    	this.container=container;
    }
    
}

