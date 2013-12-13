package disponibles;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;


import categoria.Categoria;
import alquiler.Alquiler;
import autos.Auto;

@Named("Disponibles")
public class DisponibleServicio extends AbstractFactoryAndRepository {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "disponible";
	}
	/**
	 * Se consulta la disponibilidad de vehiculos entre dos fechas y seleccionando una categoria.
	 * Se retorna una lista de vehiculos disponibles.
	 * 
	 * @param fechaAlquiler
	 * @param fechaDevolucion
	 * @param categoria
	 * 
	 * @return List<Disponible>
	 */
	@MemberOrder(sequence = "1")
	@Named("Entre fechas por Categoria")
	public List<Disponible> entreFechas(
			@Named("Fecha de alquiler:") LocalDate fechaAlquiler,
			@Optional
			@Named("Fecha de devolución:") LocalDate fechaDevolucion,
			@Named("Categoria") Categoria categoria) {
		eliminarDisponibilidad();
		List<Disponible> listaAutosDisponibles = new ArrayList<Disponible>();
		final List<Auto> autos = listaAutos();
		LocalDate fechaAux = fechaAlquiler;
		LocalDate hastaAux = (fechaDevolucion != null) ? fechaDevolucion : fechaAlquiler;

		for (int i = 0; i <= calculoDias(fechaAlquiler, hastaAux); i++) {
			for (Auto auto : autos) {
				Disponible disp = newTransientInstance(Disponible.class);
				if (existeAlquiler(fechaAux, auto.getPatente()) != null) {
					AutoPorFecha autoFecha = existeAlquiler(fechaAux,
							auto.getPatente());
					if (autoFecha.getCategoria().equals(categoria)) {
						disp.setPatente(autoFecha.getPatente());
						disp.setCategoria(autoFecha.getCategoria());
						disp.setAlquiler(autoFecha.getAlquiler());
						disp.setModeloAuto(autoFecha.getModeloAuto());
						disp.setDesdePorFechas(true);
						disp.setFecha(fechaAux);
						persistIfNotAlready(disp);
						listaAutosDisponibles.add(disp);
					}
				} else {
					if (auto.getCategoria().equals(categoria)) {
						disp.setPatente(auto.getPatente());
						disp.setModeloAuto(auto.getModelo());
						disp.setCategoria(auto.getCategoria());
						disp.setDesdePorFechas(true);	
						disp.setFecha(fechaAux);
						persistIfNotAlready(disp);
						listaAutosDisponibles.add(disp);
					}
				}
			}
			fechaAux = fechaAlquiler.plusDays(i + 1);
		}
		return listaAutosDisponibles;
	}
	
	@MemberOrder(sequence = "1")
	@Named("Entre fechas por Categoria")
	@Programmatic
	public List<Disponible> entreFechasPlusAlquiler(
			@Named("Fecha de alquiler:") LocalDate fechaAlquiler,
			@Optional
			@Named("Fecha de devolución:") LocalDate fechaDevolucion,
			@Named("Categoria") Categoria categoria,
			@Optional
			@Named("Alquiler") Alquiler alquiler) {

		eliminarDisponibilidad();
		
		List<Disponible> listaAutosDisponibles = new ArrayList<Disponible>();
		final List<Auto> autos = listaAutos();
		LocalDate fechaAux = fechaAlquiler;
		LocalDate hastaAux = (fechaDevolucion != null) ? fechaDevolucion : fechaAlquiler;

		for (int i = 0; i <= calculoDias(fechaAlquiler, hastaAux); i++) {
			for (Auto auto : autos) {
				Disponible disp = newTransientInstance(Disponible.class);
				if (existeAlquiler(fechaAux, auto.getPatente()) != null) {
					AutoPorFecha autoFecha = existeAlquiler(fechaAux,
							auto.getPatente());
					if (autoFecha.getCategoria().equals(categoria)) {
						
					}
				} else {
					if (auto.getCategoria().equals(categoria)) {
						disp.setPatente(auto.getPatente());
						disp.setModeloAuto(auto.getModelo());
						disp.setCategoria(auto.getCategoria());
						disp.setAlquilerQueLlama(alquiler);
						disp.setDesdePorFechas(false);
						disp.setFecha(fechaAux);
						persistIfNotAlready(disp);
						listaAutosDisponibles.add(disp);
					}
				}
			}
			fechaAux = fechaAlquiler.plusDays(i + 1);
		}
		return listaAutosDisponibles;
	}
	
	
	 /**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link DisponibleServicio#entreFechas(LocalDate, LocalDate, Categoria)}
     * 
     * @return List<Categoria>
     */
	public List<Categoria> choices2EntreFechas() {
		List<Categoria> items = listaCategoriasActivas();
		return items;
	}
	/**
	 * Retorna una lista de Categorias activas.
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
	 * Calculo de cantidad de d&iacute;s entre fechaDesde y fechaHasta.
	 * @param fechaDesde
	 * @param fechaHasta
	 * 
	 * @return int	
	 */
	protected int calculoDias(final LocalDate fechaDesde, final LocalDate fechaHasta) {
		long inicio = fechaDesde.toDate().getTime();
		long fin = fechaHasta.toDate().getTime();
		long diferencia = fin - inicio;
		long resultado = diferencia / (24 * 60 * 60 * 1000);
		return (int) resultado;
	}
	/**
	 * Accion que provee el Framework para validar un parámetro.
	 * 
	 * Se valida el ingreso de Fechas en el método {@link DisponibleServicio#entreFechas(LocalDate, LocalDate, Categoria)}
	 *
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param categoria
	 * 
	 * @return String
	 * 
	 */
	public String validateEntreFechas(LocalDate fechaDesde, LocalDate fechaHasta,
			Categoria categoria) {
		if (fechaHasta == null) {
			return null;
		} else {	
			LocalDate fechaActual=new LocalDate();
			if (fechaDesde.isBefore(fechaActual)) {
				return "La fecha Alquiler no puede ser menor a la fecha de Hoy";
			} else {
				if (fechaHasta.isBefore(fechaDesde) || fechaHasta.isEqual(fechaDesde)) {
					return "La fecha de Alquiler debe ser menor a la fecha de Devolucion";
				} else {
					return null;
				}
			}
		}
	}
	/**
	 * Retorna una lista de Autos activos.
	 * @return List<Auto>
	 */
	@Programmatic
	public List<Auto> listaAutos() {
		return allMatches(QueryDefault.create(Auto.class, "listadoAutosActivos"));
	}
	
	/**
	 * Consulta si existen alquileres en la fecha estipulada.
	 * Retorna una lista de vehiculos con el estado de alquiler que tiene en ese momento especifico.
	 * 
	 * @param fecha
	 * @param patente
	 * @return AutoPorFecha
	 */
	@Hidden
	public  AutoPorFecha existeAlquiler(final LocalDate fecha,
			final String patente) {
		return uniqueMatch(AutoPorFecha.class, new Filter<AutoPorFecha>() {
			@Override
			public boolean accept(AutoPorFecha auto) {
				return auto.getFecha().equals(fecha)
						&& auto.getPatente().equals(patente);
			}
		});
	}
	/**
	 * Se retorna una lista de los vehiculos alquilados.
	 * 
	 * @param patente
	 * @return List<AutoPorFecha>
	 */
	@Hidden
	public List<AutoPorFecha> autosAlquilados(final String patente) {
		return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>() {
			@Override
			public boolean accept(final AutoPorFecha auto) {
				return auto.getPatente().contains(patente);
			}
		});
	}
	
	
	/**
	 * Lista de disponibilidad
	 * @return List<Disponible>
	 */
	@Programmatic
    public List<Disponible> listaDisponibilidad(){
    	return allMatches(QueryDefault.create(Disponible.class, "Disponibles"));
    }
	
	/**
	 * Elimina la lista de disponibilidad.
	 */
	@Programmatic
	 public void eliminarDisponibilidad(){
			List<Disponible> lista=listaDisponibilidad();
			for(Disponible d:lista){
				remove(d);
			}		
	}
}
