package autos;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.DescribedAs;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Named;
import categoria.Categoria;
import marca.Marca;

/**
 * La entidad Auto representa los vehiculos que manejará nuestra empresa.
 * 
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "listadoAutosActivos", language = "JDOQL", value = "SELECT FROM autos.Auto WHERE activo==true"),
		@javax.jdo.annotations.Query(name = "findAutos", language = "JDOQL", value = "SELECT FROM autos.Auto WHERE patente == :patente ") })
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("AUTO")
@AutoComplete(repository = AutoServicio.class, action = "autoComplete")
public class Auto {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * @return String
	 */
	public String iconName() {
		return "auto";
	}

	/**
	 * Enumeracion que determina los Tipos de Combustibles
	 */
	public static enum TipoCombustible {
		NAFTA, DIESEL;
	}

	/**
	 * Enumeracion que determina las Compa&ntilde;ias de Seguro utilizadas por
	 * la Empresa.
	 */
	public static enum Seguro {
		LA_SEGUNDA, MAPFRE, LA_PATRONAL, LA_CAJA, ZURICH;
	}

	private String patente;

	/**
	 * Retorna patente del vehiculo
	 * 
	 * @return String
	 */
	@DescribedAs("El dominio del vehiculo.")
	@MemberOrder(sequence = "1")
	@Title
	@Hidden
	@Named("Patente")
	public String getPatente() {
		return patente;
	}

	/**
	 * Seteo de patente del vehiculo
	 * 
	 * @param patente
	 */
	public void setPatente(final String patente) {
		this.patente = patente;
	}

	private String usuario;

	/**
	 * Retorno de nombre del usuario
	 * 
	 * @return String
	 */
	@Hidden
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Seteo del nombre del usuario
	 * 
	 * @param usuario
	 */
	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	@Persistent
	private Marca marca;

	/**
	 * Retorna la marca del vehiculo.
	 * 
	 * @return Marca
	 */
	@DescribedAs("La marca del vehiculo.")
	@MemberOrder(sequence = "2")
	@Named("Marca")
	public Marca getMarca() {
		return marca;
	}

	/**
	 * Seteo de la marca del vehiculo
	 * 
	 * @param marca
	 */
	public void setMarca(final Marca marca) {
		this.marca = marca;
	}

	private String modelo;

	/**
	 * Se retorna el modelo del vehiculo.
	 * 
	 * @return String
	 */
	@DescribedAs("El modelo del vehiculo.")
	@MemberOrder(sequence = "3")
	@Named("Modelo")
	public String getModelo() {
		return modelo;
	}

	/**
	 * Se setea el modelo del vehiculo.
	 * 
	 * @param modelo
	 */
	public void setModelo(final String modelo) {
		this.modelo = modelo;
	}

	private int ano;

	/**
	 * Se retorna el a&ntilde;o del vehiculo.
	 * 
	 * @return int
	 */
	@DescribedAs("El año del vehiculo.")
	@MemberOrder(sequence = "4")
	@Named("Año")
	public int getAno() {
		return ano;
	}

	/**
	 * Se setea el a&ntilde;o del vehiculo.
	 * 
	 * @param ano
	 */
	public void setAno(final int ano) {
		this.ano = ano;
	}

	@Persistent
	private Categoria categoria;

	/**
	 * Se retorna la categoria del Auto.
	 * 
	 * @return Categoria
	 */
	@DescribedAs("La categoria del vehiculo.")
	@MemberOrder(sequence = "5")
	@Named("Categoria")
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * Se setea la categoria del Auto.
	 * 
	 * @param categoria
	 */
	public void setCategoria(final Categoria categoria) {
		this.categoria = categoria;
	}

	private String color;

	/**
	 * Se retorna el color del Auto.
	 * 
	 * @return String
	 */
	@DescribedAs("El color del vehiculo.")
	@MemberOrder(sequence = "6")
	@Named("Color")
	public String getColor() {
		return color;
	}

	/**
	 * Se setea el color del Auto.
	 * 
	 * @param color
	 */
	public void setColor(final String color) {
		this.color = color;
	}

	private int kms;

	/**
	 * Se retorna el kilometraje del auto.
	 * 
	 * @return int
	 */
	@DescribedAs("El kilometraje del vehiculo.")
	@MemberOrder(sequence = "7")
	@Named("Kilometraje")
	public int getKilometraje() {
		return kms;
	}

	/**
	 * Se setea el kilometraje del auto.
	 * 
	 * @param kms
	 */
	public void setKilometraje(final int kms) {
		this.kms = kms;
	}

	private int baul;

	/**
	 * Retorna la capacidad del baul del vehiculo.
	 * 
	 * @return int
	 */
	@DescribedAs("La capacidad del baul del vehiculo.")
	@MemberOrder(sequence = "8")
	@Named("Capacidad Baul (lts)")
	public int getCapacidadBaul() {
		return baul;
	}

	/**
	 * Setea la capacidad del baul del vehiculo.
	 * 
	 * @param baul
	 */
	public void setCapacidadBaul(final int baul) {
		this.baul = baul;
	}

	private TipoCombustible combustible;

	/**
	 * Retorno del tipo de combustible.
	 * 
	 * @return TipoCombustible
	 */
	@DescribedAs("El tipo de combustible del vehiculo.")
	@MemberOrder(sequence = "9")
	@Named("Tipo Combustible")
	public TipoCombustible getTipoCombustible() {
		return combustible;
	}

	/**
	 * Se setea el tipo de combustible
	 * 
	 * @param combustible
	 */
	public void setTipoCombustible(final TipoCombustible combustible) {
		this.combustible = combustible;
	}

	@Named("Fecha Compra")
	public String getFechaString() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		return formato.format(getFechaCompra());
	}

	private Date fechaCompra;

	/**
	 * Se retorna la fecha de compra del vehiculo.
	 * 
	 * @return Date
	 */
	@DescribedAs("Señala la fecha de compra del vehiculo.")
	@MemberOrder(sequence = "10")
	@Hidden
	public Date getFechaCompra() {
		return fechaCompra;
	}

	/**
	 * Se setea la fecha de compra del vehiculo.
	 * 
	 * @param fechaCompra
	 */
	public void setFechaCompra(final Date fechaCompra) {
		this.fechaCompra = fechaCompra;
	}

	private Seguro seguro;

	/**
	 * Se retorna el seguro del vehiculo
	 * 
	 * @return Seguro
	 */
	@DescribedAs("Señala el seguro del vehiculo.")
	@MemberOrder(sequence = "11")
	@Named("Compañia Seguro")
	public Seguro getSeguro() {
		return seguro;
	}

	/**
	 * Se setea el seguro del vehiculo
	 * 
	 * @param seguro
	 */
	public void setSeguro(final Seguro seguro) {
		this.seguro = seguro;
	}

	private boolean activo;

	/**
	 * Se retorna si el vehiculo esta activo o no.
	 * 
	 * @return boolean
	 */
	@Hidden
	@DescribedAs("Activo")
	@MemberOrder(sequence = "12")
	public boolean getActivo() {
		return activo;
	}

	/**
	 * Se setea si el auto está activo o no.
	 * 
	 * @param activo
	 */
	public void setActivo(final boolean activo) {
		this.activo = activo;
	}

	/**
	 * Accion provista por el Framework para remover el objeto Vehiculo.
	 */
	@Named("Borrar")
	public void remove() {
		setActivo(false);
		container.informUser("El registro a sido eliminado");
	}

	private DomainObjectContainer container;

	/**
	 * {{ injected: DomainObjectContainer
	 */
	public void setDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;

	}
}