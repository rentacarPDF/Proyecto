package autos;


import java.util.Date;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import categoria.Categoria;
import com.google.common.base.Objects;
import autos.Auto;
import autos.Auto.Seguro;
import autos.Auto.TipoCombustible;
import marca.Marca;
 

@Named("Flota")
public class AutoServicio extends AbstractFactoryAndRepository {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "auto";
	}
	
	/**
	 * Metodo mediante el cual se carga el Vehiculo con todas sus propiedades.
	 * 
	 * @param patente
	 * @param marca
	 * @param modelo
	 * @param ano
	 * @param categ
	 * @param color
	 * @param kms
	 * @param baul
	 * @param combustible
	 * @param fechaCompra
	 * @param seguro
	 * 
	 * @return Auto
	 */
	@MemberOrder(sequence = "1") 
	@Named("Cargar Vehiculo")
	public Auto cargarAuto(
		@RegEx(validation="[A-Za-z]{3}\\d{3}")
		@Named("Patente") String patente,
		@Named("Marca") Marca marca,
		@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
		@Named("Modelo") String modelo,
		@RegEx(validation = "[0-9]+")
		@Named("Año") int ano,
		@Named("Categoria") Categoria categ,
		@RegEx(validation="[A-Za-z ]+")
		@Named("Color") String color,
		@RegEx(validation = "[0-9]+")
		@Named("Kilometraje") int kms,
		@RegEx(validation = "[0-9]+")
		@Named("Capacidad Baul (lt)") int baul,
		@Named("Tipo de Combustible") TipoCombustible combustible,
		@Named("Fecha de Compra") Date fechaCompra,
		@Named("Compañía de Seguro")Seguro seguro) { 
		final boolean activo=true;
		final String usuario = currentUserName();
		return elAuto(patente.toUpperCase(),marca,modelo.toUpperCase(),ano,categ,color.toUpperCase(),kms,baul,combustible,fechaCompra,seguro,activo, usuario);
	}
	
	/**
	 * 
	 * Metodo en el cual se setean las propiedades del Vehiculo y se persiste.
	 * Se controla que la patente ingresada no exista ya en nuestro sistema.
	 * 
	 * @param patente
	 * @param marca
	 * @param modelo
	 * @param ano
	 * @param categ
	 * @param color
	 * @param kms
	 * @param baul
	 * @param combustible
	 * @param fechaCompra
	 * @param seguro
	 * @param activo
	 * @param userName
	 * 
	 * @return Auto
	 */
	@Hidden 
	public Auto elAuto(
		final String patente,
		final Marca marca, 
		final String modelo,
		final int ano,
		final Categoria categ, 
		final String color,
		final int kms, 
		final int baul,
		final TipoCombustible combustible,
		final Date fechaCompra,
		final Seguro seguro,
		final boolean activo,
		final String userName) {
		final List<Auto> mismaPatente = allMatches(Auto.class,
				new Filter<Auto>() {
				@Override
					public boolean accept(final Auto auto) {
						return Objects.equal(auto.getPatente(),patente);
					}			
				}	
		);
		Auto auto = newTransientInstance(Auto.class);
		if(mismaPatente.size()==0) {
		
			auto.setPatente(patente);
			auto.setMarca(marca);
			auto.setModelo(modelo);
			auto.setAno(ano);
			auto.setCategoria(categ);
			auto.setColor(color);
			auto.setKilometraje(kms);
			auto.setCapacidadBaul(baul);
			auto.setTipoCombustible(combustible);
			auto.setFechaCompra(fechaCompra);
			auto.setSeguro(seguro);
			auto.setActivo(activo);
			auto.setUsuario(userName);
			persistIfNotAlready(auto);
		}
		else {
			auto = null;		 
			getContainer().warnUser("EN EL SISTEMA YA SE ENCUENTRA UN VEHICULO CON ESTE DOMINIO");
		}		
		return auto;
    }
	/**
	 * Metodo provisto por el Framework que valida el metodo 
	 * {@link AutoServicio#cargarAuto}
	 * 
	 * @param patente
	 * @param marca
	 * @param modelo
	 * @param ano
	 * @param categ
	 * @param color
	 * @param kms
	 * @param baul
	 * @param combustible
	 * @param fecha
	 * @param seguro
	 * 
	 * @return String
	 */
	public String validateCargarAuto(
			 String patente,
			 Marca marca, 
			 String modelo,
			 int ano,
			 Categoria categ, 
			 String color,
			 int kms, 
			 int baul,
			 TipoCombustible combustible,
			 Date fecha,
			 Seguro seguro)
	{
	
			LocalDate fechaActual=new LocalDate();
			if (fecha.getTime()> fechaActual.toDate().getTime()){
				return "La fecha de compra no puede ser mayor al dia de hoy";
			}else{
				return null;
			}
		
	}
	/**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link AutoServicio#cargarAuto}
     * 
     * @return List<Marca>
     */
	public List<Marca> choices1CargarAuto(){
		List<Marca> items = listaMarcasActivas();		
		return items;
	}
	/**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link AutoServicio#cargarAuto}
     * 
     * @return List<Categoria>
     */
	public List<Categoria> choices4CargarAuto(){
		List<Categoria> items = listaCategoriasActivas();		
		return items;
	}
	/**
	 * Se retorna un listado de marcas activas
	 * @return List<Marca>
	 */
    protected List<Marca> listaMarcasActivas() {
        return allMatches(Marca.class, new Filter<Marca>() {
            @Override
            public boolean accept(final Marca t) {            	          	
                return t.getActivo();            	          	
            }
        });
    }
    /**
     * Retorna una lista de Categorias Activas
     * @return List<Categoria>
     */
	protected List<Categoria> listaCategoriasActivas() {
        return allMatches(Categoria.class, new Filter<Categoria>() {
            @Override
            public boolean accept(final Categoria t) {            	          	
                return t.getActivo();            	          	
            }
        });
    }
	/**
	 * Busqueda de Vehiculo.
	 * Se retorna una lista de vehiculos.
	 * 
	 * @param auto
	 * 
	 * @return List<Auto>
	 */
	@MemberOrder(sequence = "2")
	@Named("Buscar Vehiculo")
	public List<Auto> busquedaAuto(final Auto auto) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(final Auto t) {		
		return t.getPatente().equals(auto.getPatente()); 
		}
	  });				
	}
	public List<Auto> choices0BusquedaAuto(){
		return listadoAutosActivos();
	}
	/**
     * Retorna un listado de vehiculos activos
     * @return List<Auto> 
     */
    @ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "3") 
    @Named("Listado de Vehiculos")
    public List<Auto> listadoAutosActivos(){
    	return allMatches(QueryDefault.create(Auto.class,"listadoAutosActivos"));
    }    
    /**
     * Accion de Autocompletado generada por el framework, 
     * retorna una lista de los objetos de la entidad.
     *
     * @param auto
     * 
     * @return List<Auto>
     */
	@Hidden    
	public List<Auto> autoComplete(final String auto) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(final Auto t) {		
			return t.getPatente().contains(auto) && t.getActivo();
		}
	  });				
	}
	
	/**
	 * Helpers
	 * 
	 * Retorna un boolean que determina 
	 * si el usuario que se le está pasando por parametro es el mismo.
	 * 
	 * @param t
	 * @return boolean
	 * 
	 */
	protected boolean usuarioCurrentUser(final Auto auto) {
	    return Objects.equal(auto.getUsuario(), currentUserName());
	}
	/**
	 * Helpers
	 * 
	 * Retorna el usuario.
	 * 
	 * @return String
	 */
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}		
}