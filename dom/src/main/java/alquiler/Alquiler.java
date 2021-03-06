package alquiler;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.VersionStrategy;

import mails.CorreoEmpresa;
import mails.CorreoServicio;
import mails.Envio;
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
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.annotation.MemberGroups;
import org.joda.time.LocalDate;
import adicional.Adicional;
import com.google.common.collect.Lists;
import cliente.Cliente;
import disponibles.AutoPorFecha;
import disponibles.Disponible;
import disponibles.DisponibleServicio;
import encriptacion.EncriptaException;

/**
 * Entidad que representa el Alquiler de Autos Es la clase principal de nuestro
 * Sistema, mediante el cual se puede realizar el alquiler de un auto
 * seleccionado, entre una fecha determinada.
 * 
 * @see adicional.Adicional
 * @see cliente.Cliente
 * @see disponibles.AutoPorFecha
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.APPLICATION)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@MemberGroups({ "Estado", "Cliente", "Datos del Alquiler", "Autos",
		"Datos de Factura" })
@javax.jdo.annotations.Queries({@javax.jdo.annotations.Query(name = "traerAlquileres", language = "JDOQL", value = "SELECT FROM alquiler.Alquiler order by numero asc"),
	@javax.jdo.annotations.Query(name = "traerAlquileresPorApellido", language = "JDOQL", value = "SELECT FROM alquiler.Alquiler WHERE  apellido== :apellido")})
@AutoComplete(repository = AlquilerServicio.class, action = "autoComplete")
@Audited
@ObjectType("ALQUILER")
public class Alquiler {
	Envio en = new Envio();

	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * 
	 * El icono a mostrarse depende del estado de la reserva: RESERVADO,
	 * ALQUILADO, CERRADO
	 * 
	 */
	public String iconName() {
		if (getEstado() == EstadoAlquiler.ALQUILADO) {
			return "enproceso";
		} else {
			if (getEstado() == EstadoAlquiler.CERRADO) {
				return "cerrado";
			} else {
				if (getEstado() == EstadoAlquiler.RESERVADO) {
					return "reservado";
				} else {
					return "alquiler";
				}
			}
		}
	}

	/**
	 * Enumeracion que determina los posibles estados en los cuales pasa un
	 * Alquiler.
	 */
	public static enum EstadoAlquiler {
		RESERVADO, ALQUILADO, CERRADO;
	}

	/**
	 * Enumeracion que determina los posibles medios de pago.
	 */
	public static enum TipoPago {
		EFECTIVO, CHEQUE, TARJETA_CREDITO, TARJETA_DEBITO;
	}

	/**
	 * Titulo que se mostrar&aacute; en la UI. Se mostrar&aacute; el nombre de
	 * los Estados.
	 * 
	 * @return String
	 */
	public String title() {
		return getEstado().toString();
	}

	private String nombreEstado;

	@Named("Estado")
	@NotPersisted
	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Estado", sequence = "1")
	public String getNombreEstado() {
		nombreEstado = getEstado().toString();
		return nombreEstado;
	}

	private EstadoAlquiler estado;

	/**
	 * Se retorna el Estado del alquiler
	 * 
	 * @return EstadoAlquiler
	 */
	@Hidden
	public EstadoAlquiler getEstado() {
		return estado;
	}

	/**
	 * Se setea el Estado del Alquiler.
	 * 
	 * @param estado
	 */
	public void setEstado(final EstadoAlquiler estado) {
		this.estado = estado;
	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long numero;

	/**
	 * Se retorna el n&uacute;mero del Alquiler.
	 * 
	 * @return Long
	 */
	@Disabled
	@Named("Nro Alquiler")
	@MemberOrder(name = "Datos del Alquiler", sequence = "1")
	public Long getNumero() {
		return numero;
	}

	/**
	 * Se setea el n&uacute;mero del Alquiler.
	 * 
	 * @param numero
	 */
	public void setNumero(final Long numero) {
		this.numero = numero;
	}

	/**
	 * Fecha en la cual se realiza el alquiler.
	 * 
	 * @return String
	 */
	@Named("Fecha Reserva")
	@MemberOrder(name = "Datos del Alquiler", sequence = "2")
	public String getFechaString() {
		if (getFecha() != null) {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			return formato.format(getFecha());
		}
		return null;
	}

	private Date fecha;

	/**
	 * Se retorna la fecha del alquiler
	 * 
	 * @return Date
	 */
	@Hidden
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Se setea la fecha del Alquiler
	 * 
	 * @param fecha
	 */
	public void setFecha(final Date fecha) {
		this.fecha = fecha;
	}

	private BigDecimal senaMinima;

	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos del Alquiler", sequence = "3")
	@Named("Seña Mínima (20%)")
	@Disabled
	public BigDecimal getSenaMinima() {
		return senaMinima;
	}

	public void setSenaMinima(final BigDecimal senaMinima) {
		this.senaMinima = senaMinima;
	}

	@Hidden
	public Alquiler calculoSenaMinima() {
		BigDecimal porc = new BigDecimal("20");
		BigDecimal divisor = new BigDecimal("100");
		BigDecimal calc = new BigDecimal("0");
		calc = (getPrecioReserva().multiply(porc)).divide(divisor);
		calc.setScale(2, BigDecimal.ROUND_HALF_UP);

		setSenaMinima(calc);

		return this;
	}

	private BigDecimal sena;

	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos del Alquiler", sequence = "4")
	@Named("Seña")
	public BigDecimal getSena() {
		return sena;
	}

	public void setSena(final BigDecimal sena) {
		this.sena = sena;
	}

	public String disableSena() {
		if (getEstado() == EstadoAlquiler.RESERVADO) {
			return null;
		} else
			return getEstado() == EstadoAlquiler.CERRADO ? "El Alquiler esta CERRADO no se puede editar"
					: "El vehiculo esta ALQUILADO no se puede editar la seña";
	}

	/*
	 * @Named("Descontar Seña")
	 * 
	 * @MemberOrder(name="Datos del Alquiler",sequence="4") public Alquiler
	 * descuentoSeña(){ calculoPrecioTotal(); return this; }
	 */
	private boolean voucher;

	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos del Alquiler", sequence = "5")
	@Named("Voucher de Garantía")
	public boolean isVoucher() {
		return voucher;
	}

	public void setVoucher(final boolean voucher) {
		this.voucher = voucher;
	}

	private TipoPago tipoPago;

	/**
	 * Retorna el tipo de pago
	 * 
	 * @return TipoPago
	 */
	@Named("Tipo de Pago")
	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos de Factura", sequence = "1")
	public TipoPago getTipoPago() {
		return tipoPago;
	}

	/**
	 * Setea el tipo de pago
	 * 
	 * @param tipoPago
	 */
	public void setTipoPago(final TipoPago tipoPago) {
		this.tipoPago = tipoPago;
	}

	/**
	 * M&eacute;todo provisto por el framework que deshabilita la opcion de
	 * poder editar el tipo de pago.
	 * 
	 * @return String
	 */
	public String disableTipoPago() {
		if (isVoucher() == true) {
			if (getEstado() == EstadoAlquiler.RESERVADO) {
				return "El vehiculo debe ALQUILARSE para elegir el tipo de pago";
			} else
				return getEstado() == EstadoAlquiler.CERRADO ? null
						: "El vehiculo se abona al CERRAR el Alquiler";
		} else {
			if (getEstado() == EstadoAlquiler.RESERVADO) {
				return "El vehiculo debe ALQUILARSE para elegir el tipo de pago";
			} else
				return getEstado() == EstadoAlquiler.CERRADO ? "El Alquiler ya fue abonado"
						: null;
		}
	}

	private int factura;

	/**
	 * Retorna el n&uacute;mero de factura
	 * 
	 * @return int
	 */
	@Named("Nro Factura")
	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos de Factura", sequence = "2")
	public int getNumeroFactura() {
		return factura;
	}

	/**
	 * Setea el n&uacute;mero de factura
	 * 
	 * @param factura
	 */
	public void setNumeroFactura(final int factura) {
		this.factura = factura;
	}

	/**
	 * M&eacute;todo provisto por el framework que deshabilita la opcion de
	 * poder editar el numero de factura
	 * 
	 * @return String
	 */
	public String disableNumeroFactura() {
		if (isVoucher() == true) {
			if (getEstado() == EstadoAlquiler.RESERVADO) {
				return "El vehiculo debe ALQUILARSE para elegir el tipo de pago";
			} else
				return getEstado() == EstadoAlquiler.CERRADO ? null
						: "El vehiculo se abona al CERRAR el Alquiler";
		} else {
			if (getEstado() == EstadoAlquiler.RESERVADO) {
				return "El vehiculo debe ALQUILARSE para elegir el tipo de pago";
			} else
				return getEstado() == EstadoAlquiler.CERRADO ? "El Alquiler ya fue abonado"
						: null;
		}
	}

	private BigDecimal precioRes;

	/**
	 * Retorna el precio del Alquiler en Neto y solamente correspondiente al
	 * valor de la categoria del Auto.
	 * 
	 * @return BigDecimal
	 */
	@Named("Precio Autos")
	@Disabled
	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos de Factura", sequence = "3")
	public BigDecimal getPrecioReserva() {
		return precioRes;
	}

	/**
	 * Se setea el precio del Alquiler en Neto.
	 * 
	 * @param precio
	 */
	public void setPrecioReserva(final BigDecimal precioRes) {
		this.precioRes = precioRes;
	}

	private BigDecimal precioAdic;

	/**
	 * 
	 * 
	 * @return BigDecimal
	 */
	@Named("Precio Adicionales")
	@Disabled
	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos de Factura", sequence = "4")
	public BigDecimal getPrecioAdicional() {
		return precioAdic;
	}

	/**
	 * 
	 * @param precio
	 */
	public void setPrecioAdicional(final BigDecimal precioAdic) {
		this.precioAdic = precioAdic;
	}

	private BigDecimal precioTot;

	/**
	 * Se retorna la sumatoria del precio Neto m&aacute;s los adicionales.
	 * 
	 * @see alquiler.Alquiler#getPrecioReserva()
	 * @see alquiler.Alquiler#calculoPrecioReserva()
	 * 
	 * @return BigDecimal
	 */
	@Named("Precio Total")
	@Disabled
	@Hidden(where = Where.ALL_TABLES)
	@MemberOrder(name = "Datos de Factura", sequence = "5")
	public BigDecimal getPrecioTotal() {
		return precioTot;
	}

	/**
	 * Se setea el precio total calculado.
	 * 
	 * @param precioTot
	 */
	public void setPrecioTotal(final BigDecimal precioTot) {
		this.precioTot = precioTot;
	}

	/**
	 * M&eacute;todo que calcula el subtotal del Alquiler. Se calcula el precio
	 * por la categoria de cada auto y la cantidad de d&iacute;as. Se realiza
	 * una sumatoria de lo antes mencionado.
	 * 
	 * @return alquiler.Alquiler
	 */
	@Hidden
	public Alquiler calculoPrecioReserva() {
		List<AutoPorFecha> listaAutos = Lists.newArrayList(getAutos());

		BigDecimal sum = new BigDecimal("0");
		sum.setScale(5, BigDecimal.ROUND_HALF_UP);
		for (AutoPorFecha auto : listaAutos) {
			sum = sum.add(auto.getCategoria().getPrecio());
		}
		setPrecioReserva(sum);
		calculoSenaMinima();
		return this;
	}

	@Hidden
	public Alquiler calculoPrecioAdicional() {
		List<AutoPorFecha> listaAutos = Lists.newArrayList(getAutos());
		List<Adicional> listaAdicionales = Lists.newArrayList(getAdicionales());

		BigDecimal suma = new BigDecimal("0");
		suma.setScale(5, BigDecimal.ROUND_HALF_UP);

		BigDecimal aux = new BigDecimal("0");
		aux.setScale(5, BigDecimal.ROUND_HALF_UP);

		BigDecimal lista = new BigDecimal(listaAutos.size());
		lista.setScale(5, BigDecimal.ROUND_HALF_UP);

		for (Adicional adic : listaAdicionales) {
			aux = adic.getPrecio().multiply(lista);
			suma = suma.add(aux);
		}
		setPrecioAdicional(suma);
		return this;
	}

	/**
	 * Se calcula el precio total, que es la sumatoria de los precios de las
	 * categorias de los autos, mas los adicionales, por la cantidad de dias.
	 * 
	 * @return Alquiler
	 */
	@Hidden
	public Alquiler calculoPrecioTotal() {
		BigDecimal suma = new BigDecimal("0");
		suma.setScale(5, BigDecimal.ROUND_HALF_UP);
		suma = (getPrecioReserva().add(getPrecioAdicional()))
				.subtract(getSena());
		setPrecioTotal(suma);

		return this;
	}

	/**
	 * 
	 * Relacion bidireccional entre Autos y Alquiler. Se genera una lista de
	 * Autos, entre fechas determinadas.
	 * 
	 * @see autos.Auto
	 * 
	 * @return List<disponibles.AutoPorFecha>
	 */
	@Persistent(mappedBy = "alquiler")
	private List<AutoPorFecha> autos = new ArrayList<AutoPorFecha>();

	public List<AutoPorFecha> getAutos() {
		return autos;
	}

	/**
	 * Se Setea una lista de autos, entre una fecha determinada.
	 */
	public void setAutos(List<AutoPorFecha> listaAutos) {
		this.autos = listaAutos;
	}

	/**
	 * Accion que borra el Alquiler especificado. Provisto por el Framework.
	 * 
	 * @param auto
	 */
	@Named("Borrar")
	@MemberOrder(name = "Autos", sequence = "2")
	public Alquiler removeFromAutos(final AutoPorFecha auto) {
		autos.remove(auto);
		container.removeIfNotAlready(auto);
		calculoPrecioReserva();
		calculoPrecioTotal();
		return this;
	}

	/**
	 * Choices provisto por el Framework, que habilita una serie de opciones
	 * para una Propiedad/Accion. Choices para la accion
	 * {@link alquiler.Alquiler#removeFromAutos(AutoPorFecha)}
	 * 
	 * @see alquiler.Alquiler#removeFromAutos(AutoPorFecha)
	 * 
	 * @return List<disponibles.AutoPorFecha>
	 * 
	 */
	public List<AutoPorFecha> choices0RemoveFromAutos() {

		return Lists.newArrayList(getAutos());
	}

	/**
	 * Accion provista por el Framework. Deshabilita la opcion de borrar un
	 * elemento de la lista de Autos.
	 * 
	 * @param auto
	 * @return String
	 */
	public String disableRemoveFromAutos(AutoPorFecha auto) {
		if (getEstado() == EstadoAlquiler.RESERVADO
				|| getEstado() == EstadoAlquiler.ALQUILADO) {
			return autos.size() > 1 ? null
					: "Debe quedar al menos un auto para mantener el Alquiler";
		} else
			return "El Alquiler esta CERRADO no se pueden eliminar vehiculos";
	}

	@Named("Ver disponibilidad")
	@MemberOrder(name = "Autos", sequence = "1")
	public List<Disponible> disponibilidad() {

		List<AutoPorFecha> listaAutos = Lists.newArrayList(getAutos());

		LocalDate fechaAlquiler = new LocalDate(listaAutos
				.get(listaAutos.size() - 1).getFecha().toString());
		LocalDate fechaDevolucion = fechaAlquiler.plusDays(10);

		List<Disponible> listaAutosDisponibles = servDisp
				.entreFechasPlusAlquiler(fechaAlquiler, fechaDevolucion,
						listaAutos.get(0).getCategoria(), this);

		return listaAutosDisponibles;
	}

	/**
	 * Accion provista por el Framework. Deshabilita la opcion de agregar un
	 * elemento a la lista de Autos.
	 * 
	 * @param auto
	 * @return String
	 */
	public String disableDisponibilidad() {
		if (getEstado() == EstadoAlquiler.RESERVADO) {
			return "Para agregar dias, debe estar en estado ALQUILADO";
		} else {
			if(getEstado() == EstadoAlquiler.CERRADO) {
				return "El Alquiler esta CERRADO no se pueden agregar vehiculos";
			}
		return null;
		}

	}

	/**
	 * Accion que agrega los autos a la lista.
	 * 
	 * @param auto
	 */
	@Hidden
	public void addToAutos(AutoPorFecha auto) {
		if (auto == null || autos.contains(auto)) {
			return;
		}
		auto.setAlquiler(this);
		autos.add(auto);
		calculoPrecioReserva();
		calculoPrecioTotal();

	}

	@Persistent(mappedBy = "alquiler")
	private List<Adicional> adicionales = new ArrayList<Adicional>();

	/**
	 * Retorno de Lista de Adicionales con relacion bidireccional con el
	 * Alquiler.
	 * 
	 * @return List<adicional.Adicional>
	 * 
	 */
	public List<Adicional> getAdicionales() {
		return adicionales;
	}

	/**
	 * Seteo de Lista de Adicionales.
	 * 
	 * @param listaAdicionales
	 * 
	 */
	public void setAdicionales(List<Adicional> listaAdicionales) {
		this.adicionales = listaAdicionales;
	}

	/**
	 * Accion que agrega un Adicional a la lista de Adicionales.
	 * 
	 * @param adicional
	 * @return Alquiler
	 */
	@Named("Añadir")
	@MemberOrder(name = "Adicionales", sequence = "1")
	public Alquiler agregar(@Named("Adicional") Adicional adicional) {
		Adicional adic = container.newTransientInstance(Adicional.class);
		adic.setNombre(adicional.getNombre());
		adic.setDescripcion(adicional.getDescripcion());
		adic.setPrecio(adicional.getPrecio());

		addToAdicionales(adic);
		return this;
	}

	/**
	 * Accion provista por el Framework que deshabilita la opcion de agregar
	 * elementos a la lista Adicionales.
	 * 
	 * @param adicional
	 * @return String
	 */
	public String disableAgregar(Adicional adicional) {
		if (getEstado() == EstadoAlquiler.RESERVADO) {
			return null;
		} else
			return getEstado() == EstadoAlquiler.ALQUILADO ? "El vehiculo esta ALQUILADO no se pueden agregar adicionales"
					: "El Alquiler esta CERRADO no se puede editar";
	}

	/**
	 * Accion provista por el Framework que permite agregar elementos a la lista
	 * Adicionales a un Alquiler. Si es que esta lista no está vacia.
	 * 
	 * @param adic
	 */
	@Hidden
	public void addToAdicionales(Adicional adic) {
		if (adic == null || adicionales.contains(adic)) {
			return;
		}
		adic.setAlquiler(this);
		adicionales.add(adic);
		calculoPrecioAdicional();
		calculoPrecioTotal();
	}

	/**
	 * Accion provista por el Framework que permite eliminar elementos a la
	 * lista Adicionales.
	 * 
	 * @param adic
	 * @return Alquiler
	 */
	@Named("Borrar")
	@MemberOrder(name = "Adicionales", sequence = "2")
	public Alquiler removeFromAdicionales(final Adicional adic) {
		adicionales.remove(adic);
		calculoPrecioAdicional();
		calculoPrecioTotal();
		return this;
	}

	/**
	 * Accion provista por el Framework que deshabilita la opcion de remover
	 * elementos a la lista Adicionales.
	 * 
	 * @param adicional
	 * @return String
	 */
	public String disableRemoveFromAdicionales(Adicional adicional) {
		if (getEstado() == EstadoAlquiler.RESERVADO) {
			return adicionales.size() > 0 ? null
					: "No existe Adicionales para este Alquiler";
		} else {
			return getEstado() == EstadoAlquiler.ALQUILADO ? "El vehiculo se encuentra en poder del cliente, no se puede editar"
					: "El Alquiler esta CERRADO no se puede editar";
		}
	}

	/**
	 * Choices provisto por el Framework, que habilita una serie de opciones
	 * para una Propiedad/Accion.
	 * 
	 * Choices para la accion
	 * {@link alquiler.Alquiler#removeFromAdicionales(Adicional)}
	 * 
	 * @return List<Adicional>
	 */
	public List<Adicional> choices0RemoveFromAdicionales() {
		return Lists.newArrayList(getAdicionales());
	}

	private Cliente clienteId;

	/**
	 * Retorna el Nro de Cuil/Cuit del Cliente que realiza el Alquiler
	 * 
	 * @return Cliente
	 */
	@DescribedAs("Numero de CUIL/CUIT")
	@Disabled
	@Named("CUIL/CUIT")
	@MemberOrder(name = "Cliente", sequence = "1")
	public Cliente getClienteId() {
		return clienteId;
	}

	/**
	 * Setea el Nro de Cuil/Cuit del Cliente que realiza el Alquiler
	 * 
	 * @param clienteId
	 */
	public void setClienteId(final Cliente clienteId) {
		this.clienteId = clienteId;
	}

	private String nombre;

	/**
	 * Retorna el nombre del Cliente que realiza el Alquiler.
	 * 
	 * @return String
	 */
	@Disabled
	@Named("Nombre")
	@MemberOrder(name = "Cliente", sequence = "2")
	public String getNombreCliente() {
		return nombre;
	}

	/**
	 * Setea el nombre del Cliente que realiza el Alquiler.
	 * 
	 * @param nombre
	 */
	public void setNombreCliente(String nombre) {
		this.nombre = nombre;
	}

	private String apellido;

	/**
	 * Retorna el apellido del Cliente que realiza el Alquiler
	 * 
	 * @return String
	 */
	@Disabled
	@Named("Apellido")
	@MemberOrder(name = "Cliente", sequence = "3")
	public String getApellidoCliente() {
		return apellido;
	}

	/**
	 * Setea el apellido del Cliente que realiza el Alquiler
	 * 
	 * @param apellido
	 */
	public void setApellidoCliente(String apellido) {
		this.apellido = apellido;
	}

	/**
	 * Accion que permite el borrado del Alquiler
	 */
	@Named("Borrar")
	public void borrarAlquiler() {
		for (Adicional adic : getAdicionales()) {
			container.removeIfNotAlready(adic);
		}
		for (AutoPorFecha auto : getAutos()) {
			container.removeIfNotAlready(auto);
		}
		container.informUser("Alquiler número: " + getNumero()
				+ " a sido eliminado");
		container.removeIfNotAlready(this);
	}

	/**
	 * Accion provista por el Framework que permite deshabilitar la accion de
	 * borrar el Alquiler.
	 * 
	 * @see Alquiler#borrarAlquiler()
	 * @return String
	 */
	public String disableBorrarAlquiler() {
		if (getEstado() == EstadoAlquiler.RESERVADO) {
			return null;
		} else
			return getEstado() == EstadoAlquiler.ALQUILADO ? "El Vehiculo esta ALQUILADO no se puede borrar el Alquiler"
					: "El Alquiler esta CERRADO no se puede borrar";
	}

	private String usuario;

	/**
	 * Propiedad que retorna el usuario.
	 * 
	 * @return String
	 */
	@Hidden
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Propiedad que setea el usuario.
	 * 
	 * @param usuario
	 */
	public void setUsuario(final String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Accion que pasa el estado del Alquiler a: ALQUILADO
	 * 
	 * @return Alquiler
	 * @throws EncriptaException 
	 */
	@MemberOrder(name = "Estado", sequence = "2")
	public Alquiler alquilado() throws EncriptaException {

		setEstado(EstadoAlquiler.ALQUILADO);
		calculoPrecioTotal();

		CorreoEmpresa correoEmpresa=new CorreoEmpresa();
		correoEmpresa=cs.buscarConfiguracionPorEmail("rentacarpdf");
		// envio de emails cuando pasa al estado alquilado
		
		System.out.println("EN ALQUILADO %%% "+correoEmpresa.getCorreo());
		System.out.println("EN ALQUILADO %%% "+correoEmpresa.getPass());
		if(correoEmpresa!=null){
			Envio en = new Envio();
			en.setProperties(correoEmpresa);
			en.enviar(armarEmail(), this.clienteId.getEmail());
		}
		return this;
	}

	/**
	 * Accion provista por el Framework que deshabilita la transicion del estado
	 * del Alquiler a: ALQUILADO
	 * 
	 * @return String
	 */
	public String disableAlquilado() {
		if (getEstado() == EstadoAlquiler.RESERVADO) {
			return null;
		} else
			return getEstado() == EstadoAlquiler.ALQUILADO ? "El Alquiler ya se encuentra ALQUILADO"
					: "El Alquiler debe estar RESERVADO para pasar a ALQUILADO";
	}

	/**
	 * Accion que pasa el estado del Alquiler a: CERRADO
	 * 
	 * @return Alquiler
	 * @throws EncriptaException 
	 */
	@MemberOrder(name = "Estado", sequence = "3")
	public Alquiler cerrado() throws EncriptaException {
		setEstado(EstadoAlquiler.CERRADO);

		CorreoEmpresa correoEmpresa=new CorreoEmpresa();
		correoEmpresa=cs.buscarConfiguracionPorEmail("rentacarpdf");
		System.out.println("EN CERRADO %%% "+correoEmpresa.getCorreo());
		System.out.println("EN CERRADO %%% "+correoEmpresa.getPass());
		// envio de emails cuando pasa al estado cerrado
		if(correoEmpresa!=null){
			Envio en = new Envio();
			en.setProperties(correoEmpresa);
			en.enviar(armarEmail(), this.clienteId.getEmail());
		}
		return this;
	}

	/**
	 * Accion provista por el Framework que deshabilita la transicion del estado
	 * del Alquiler a: CERRADO.
	 * 
	 * @return String
	 */
	public String disableCerrado() {
		if (getEstado() == EstadoAlquiler.ALQUILADO) {
			return null;
		} else
			return getEstado() == EstadoAlquiler.CERRADO ? "El Alquiler ya se encuentra CERRADO"
					: "El Alquiler debe estar ALQUILADO para poder CERRARLO";
	}

	private DomainObjectContainer container;

	/**
	 * injected: DomainObjectContainer
	 */
	public void setDomainObjectContainer(final DomainObjectContainer container) {
		this.container = container;
	}

	private DisponibleServicio servDisp;

	/**
	 * Se inyecta el servicio de Disponible
	 * 
	 * @param serv
	 */
	public void injectDisponiblesServicio(final DisponibleServicio serv) {
		this.servDisp = serv;
	}

	@SuppressWarnings("unused")
	private AlquilerServicio servAlq;

	/**
	 * Se inyecta el servicio de Alquiler.
	 * 
	 * @param serv
	 */
	public void injectDisponiblesServicio(final AlquilerServicio serv) {
		this.servAlq = serv;
	}

	private CorreoServicio cs;

	/**
	 * Se inyecta el servicio de Correo.
	 * 
	 * @param cs
	 */
	public void injectCorreoServicio(final CorreoServicio cs){
		this.cs=cs;
	}
	/**
	 * Se arma el email con todos los datos del Alquiler.
	 * 
	 * @return String
	 */
	private String armarEmail() {

		List<AutoPorFecha> listaAutos;
		listaAutos = getAutos();

		List<Adicional> listaAdicionales;
		listaAdicionales = getAdicionales();

		String mensaje = "<b> Sr/a. "
				+ getClienteId().getNombre()
				+ "  "
				+ getClienteId().getApellido()
				+ "</b>, Usted ha realizado un Alquiler en nuestra Sucursal. <br> ¡Gracias por elegirnos!<br>";

		mensaje += " <h1>  Detalle de Alquiler </h1> <br>";

		mensaje += "<b>Patente del Vehiculo: </b>"
				+ listaAutos.get(0).getPatente() + "<br>";
		mensaje += "<b>Cantidad de dias: </b>" + listaAutos.size() + "<br>";
		mensaje += "<b>Fecha/s: </b> <br>";
		for (AutoPorFecha a : listaAutos) {
			mensaje += " - " + a.getFecha().toString("dd/MM/yyyy") + "<br>";
		}

		// Adicionales
		if (listaAdicionales.size() > 0) {
			mensaje += "<b>Adicionales: </b><br>";
		}

		if (listaAdicionales.size() > 0) {
			for (Adicional a : listaAdicionales) {
				mensaje += "   -   " + a.getNombre() + " - Precio por dia:  $"
						+ a.getPrecio() + "<br>";
			}
		}

		if (getNumeroFactura() != 0) {
			mensaje += "<b>Nro de Factura:</b> " + getNumeroFactura() + "<br>";
		}

		mensaje += "<b>Forma de Pago: </b>" + getTipoPago() + "<br>";
		// seña
		mensaje += "<b>Seña minima: </b>  $" + getSenaMinima() + "<br>";

		if (getSena().intValue() != 0) {
			mensaje += "<b> Seña:</b>  $" + getSena() + "<br>";
		} else {
			mensaje += "<b> Seña:</b>  No.<br>";
		}
		// voucher
		if (isVoucher()) {

			mensaje += "<b>Voucher:</b> si <br>";

		}
		mensaje += "<b>Precio Vehiculos:</b> $" + getPrecioReserva() + "<br>";
		if (listaAdicionales.size() > 0) {
			mensaje += "<b>Precio Total Adicional/es:</b> $"
					+ getPrecioAdicional() + "<br>";
		}
		mensaje += "<b>Precio Final Total:</b> $" + getPrecioTotal() + "<br>";

		return mensaje;

	}

}