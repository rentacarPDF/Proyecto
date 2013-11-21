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
import autos.Auto;

@Named("Disponibles")
public class DisponibleServicio extends AbstractFactoryAndRepository {
	public String iconName(){
		return "disponible";
	}
	@MemberOrder(sequence = "1")
	@Named("Entre fechas por Categoria")
	public List<Disponible> entreFechas(
			@Named("Fecha de alquiler:") LocalDate fechaAlq,
			@Optional
			@Named("Fecha de devolución:") LocalDate fechaDev,
			@Named("Categoria") Categoria categoria) {
		List<Disponible> listaAutosDisponibles = new ArrayList<Disponible>();
		final List<Auto> autos = listaAutos();
		LocalDate fechaAux = fechaAlq;
		LocalDate hastaAux = (fechaDev != null) ? fechaDev : fechaAlq;

		for (int i = 0; i <= calculoDias(fechaAlq, hastaAux); i++) {
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

						disp.setFecha(fechaAux);
						persistIfNotAlready(disp);
						listaAutosDisponibles.add(disp);
					}
				} else {
					if (auto.getCategoria().equals(categoria)) {
						disp.setPatente(auto.getPatente());
						disp.setModeloAuto(auto.getModelo());
						disp.setCategoria(auto.getCategoria());

						disp.setFecha(fechaAux);
						persistIfNotAlready(disp);
						listaAutosDisponibles.add(disp);
					}
				}
			}
			fechaAux = fechaAlq.plusDays(i + 1);
		}
		return listaAutosDisponibles;
	}

	// }}
	//{{Categoria
	public List<Categoria> choices2EntreFechas() {
		List<Categoria> items = listaCategoriasActivas();
		return items;
	}

	protected List<Categoria> listaCategoriasActivas() {
		return allMatches(Categoria.class, new Filter<Categoria>() {
			@Override
			public boolean accept(final Categoria t) {
				return t.getActivo();
			}
		});
	}
	// }}
	// {{ Calculo de diferencia de dias entre fechas.
	protected int calculoDias(final LocalDate a1, final LocalDate a2) {
		long inicio = a1.toDate().getTime();
		long fin = a2.toDate().getTime();
		long diferencia = fin - inicio;
		long resultado = diferencia / (24 * 60 * 60 * 1000);
		return (int) resultado;
	}
	// }}
	// {{ Validacion del ingreso de fechas
	public String validateEntreFechas(LocalDate desde, LocalDate hasta,
			Categoria categoria) {
		if (hasta == null) {
			return null;
		} else {	
			LocalDate fechaActual=new LocalDate();
			if (desde.isBefore(fechaActual)) {
				return "La fecha Alquiler no puede ser menor a la fecha de Hoy";
			} else {
				if (hasta.isBefore(desde) || hasta.isEqual(desde)) {
					return "La fecha de Alquiler debe ser menor a la fecha de Devolucion";
				} else {
					return null;
				}
			}
		}
	}
	// }}
	//{{Listado de autos
	@Programmatic
	public List<Auto> listaAutos() {
		return allMatches(QueryDefault.create(Auto.class, "listadoAutosActivos"));
	}
	// }}
	// {{Existencia de alquileres
	private AutoPorFecha existeAlquiler(final LocalDate fecha,
			final String patente) {
		return uniqueMatch(AutoPorFecha.class, new Filter<AutoPorFecha>() {
			@Override
			public boolean accept(AutoPorFecha auto) {
				return auto.getFecha().equals(fecha)
						&& auto.getPatente().equals(patente);
			}
		});
	}

	// }}
	// {{Autos alquilados
	@Hidden
	public List<AutoPorFecha> autosAlquilados(final String patente) {
		return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>() {
			@Override
			public boolean accept(final AutoPorFecha auto) {
				return auto.getPatente().contains(patente);
			}
		});
	}
	// }}
}
