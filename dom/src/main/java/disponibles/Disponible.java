package disponibles;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.Bulk;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;
import org.joda.time.LocalDate;
import categoria.Categoria;
import alquiler.Alquiler;

/**
 * 
 * Sirve para mostrar el Estado de alquiler de los vehiculos en una fecha
 * determinada o un rango de fechas, y que el usuario pueda seleccionar el
 * vehiculo que desee alquilar. Los objetos de esta clase se persisten y luego
 * se borran al momento de realizar el alquiler.
 * 
 * Por lo tanto no es necesario que esta Entidad se persista ya que esta
 * informacion ser&aacute; almacenada en la BD una vez que se efectue el
 * alquiler.
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@javax.jdo.annotations.Queries({ @javax.jdo.annotations.Query(name = "Disponibles", language = "JDOQL", value = "SELECT FROM disponibles.Disponible") })
@ObjectType("Disponibles")
@Audited
public class Disponible {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * @return String
	 */

	public String iconName() {
		return "auto";
	}

	/**
	 * Titulo identificatorio en la UI
	 * 
	 * @return String
	 */
	@Named("Auto")
	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getPatente());
		return buf.toString();
	}

	private String auto;

	/**
	 * Retorna la patente del vehiculo.
	 * 
	 * @return String
	 */
	@Hidden
	@Named("Patente")
	public String getPatente() {
		return auto;
	}

	/**
	 * Setea la patente del vehiculo.
	 * 
	 * @param auto
	 */
	public void setPatente(final String auto) {
		this.auto = auto;
	}

	private boolean seleccionar;

	/**
	 * Propiedad provista por el Framework, retorna un boolean si está o no
	 * seleccionada. Graficamente se representa con un checkbox
	 * 
	 * @return boolean
	 */
	@Named("Seleccionada")
	public boolean isEstaSeleccionada() {
		return seleccionar;
	}

	/**
	 * Se setea si está o no seleccionada.
	 * 
	 * @param seleccionar
	 */
	public void setEstaSeleccionada(final boolean seleccionar) {
		this.seleccionar = seleccionar;
	}

	private LocalDate fecha;

	/**
	 * Retorna la fecha.
	 * 
	 * @return LocalDate
	 */
	@Named("Fecha")
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * Se setea la fecha.
	 * 
	 * @param fecha
	 */
	public void setFecha(final LocalDate fecha) {
		this.fecha = fecha;
	}

	private Categoria categoria;

	/**
	 * Retorna la categoria del vehiculo
	 * 
	 * @return Categoria
	 */
	@Named("Categoria")
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * Se setea la categoria del vehiculo.
	 * 
	 * @param categoria
	 */
	public void setCategoria(final Categoria categoria) {
		this.categoria = categoria;
	}

	private Alquiler alquiler;

	/**
	 * Retorna el Estado del Alquiler.
	 * 
	 * @return Alquiler
	 */
	@Named("Estado Alquiler")
	public Alquiler getAlquiler() {
		return alquiler;
	}

	public void setAlquiler(final Alquiler alquiler) {
		this.alquiler = alquiler;
	}

	/**
	 * Accion que provee el Framework para seleccionar objetos de una lista a
	 * través de un checkbox.
	 * 
	 * Se utiliza para seleccionar el/los vehiculo/s disponibles que selecciona
	 * el cliente para alquilar.
	 * 
	 * @return Disponible
	 */
	@Named("Seleccionar")
	@Bulk
	public Disponible reserva() {
		if (getAlquiler() == null) {
			if (isEstaSeleccionada())
				setEstaSeleccionada(false);
			else
				setEstaSeleccionada(true);
		}
		return this;
	}

	/**
	 * Metodo provisto por el framework que deshabilita la opcion de poder
	 * editar la reserva.
	 * 
	 * Chequea si la reserva ya está seleccionada.
	 * 
	 * @return String
	 */
	public String disableReserva() {
		return seleccionar ? "Ya esta seleccionada!" : null;
	}

	private String modelo;

	/**
	 * Retorna el modelo del vehiculo.
	 * 
	 * @return String
	 */
	@Named("Modelo")
	public String getModeloAuto() {
		return modelo;
	}

	/**
	 * Setea el modelo del vehiculo.
	 * 
	 * @param modelo
	 */
	public void setModeloAuto(final String modelo) {
		this.modelo = modelo;
	}

	@SuppressWarnings("unused")
	private DisponibleServicio servicio;

	/**
	 * Se inyecta el servicio disponible.
	 * 
	 * @param serv
	 */
	public void injectDisponiblesServicio(final DisponibleServicio serv) {
		this.servicio = serv;
	}

	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	public void injectDomainObjectContainer(
			final DomainObjectContainer container) {
		this.container = container;
	}
}