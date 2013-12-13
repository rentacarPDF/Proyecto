package disponibles;

import java.text.SimpleDateFormat;
import java.util.Comparator;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Audited;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Disabled;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.util.TitleBuffer;
import org.joda.time.LocalDate;
import alquiler.Alquiler;
import categoria.Categoria;

/**
 * 
 * Clase que devuelve una instancia por cada vehiculo y cada d&iacute;a entre un
 * rango de fechas, manteniendo la relacion entre ambas.
 * 
 * @see Alquiler
 * @see Categoria
 * 
 */
@SuppressWarnings("rawtypes")
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "auto_para_alquilar", language = "JDOQL", value = "SELECT FROM disponiles.Disponibles WHERE estaSeleccionada == true"),
		@javax.jdo.annotations.Query(name = "auto_libre", language = "JDOQL", value = "SELECT FROM disponibles.Disponibles WHERE estaSeleccionada == false"), })
@ObjectType("AutosPorFecha")
@AutoComplete(repository = DisponibleServicio.class, action = "autosAlquilados")
@Audited
public class AutoPorFecha implements Comparator {
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
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		buf.append(formato.format(getFecha().toDate()));
		return buf.toString();
	}

	private LocalDate fecha;

	/**
	 * Retorno de la fecha
	 * 
	 * @return LocalDate
	 */
	@Disabled
	@Hidden
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

	private String auto;

	/**
	 * Retorno la patente del vehiculo.
	 * 
	 * @return String
	 */
	@Disabled
	@MemberOrder(sequence= "1")
	@Named("Auto")
	public String getPatente() {
		return auto;
	}

	/**
	 * Se setea la patente del vehiculo.
	 * 
	 * @param auto
	 */
	public void setPatente(final String auto) {
		this.auto = auto;
	}

	private Categoria categoria;

	/**
	 * Retorno de la categoria del vehiculo.
	 * 
	 * @return Categoria
	 */
	@Disabled
	@MemberOrder(sequence= "3")
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
	 * Retorno del Estado del Alquiler
	 * 
	 * @return Alquiler
	 */
	@MemberOrder(sequence= "4")
	@Named("Estado Alquiler")
	@Disabled
	public Alquiler getAlquiler() {
		return alquiler;
	}

	/**
	 * Se setea el Estado del Alquiler.
	 * 
	 * @param alquiler
	 */
	public void setAlquiler(final Alquiler alquiler) {
		this.alquiler = alquiler;
	}

	private String modelo;

	/**
	 * Retorno del modelo del vehiculo.
	 * 
	 * @return String
	 */
	@Disabled
	@MemberOrder(sequence= "2")
	@Named("Modelo")
	public String getModeloAuto() {
		return modelo;
	}

	/**
	 * Se setea el modelo del vehiculo.
	 * 
	 * @param modelo
	 */
	public void setModeloAuto(final String modelo) {
		this.modelo = modelo;
	}

	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	public void injectDomainObjectContainer(
			final DomainObjectContainer container) {
		this.container = container;
	}

	/**
	 * Metodo comparador de fechas.
	 * 
	 * @param o1
	 * @param o2
	 * 
	 * @return int
	 */
	@Override
	@Hidden
	public int compare(Object o1, Object o2) {
		AutoPorFecha fecha1 = (AutoPorFecha) o1;
		AutoPorFecha fecha2 = (AutoPorFecha) o2;
		return fecha1.getFecha().compareTo(fecha2.getFecha());
	}
}