package estadistica;


import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import categoria.Categoria;

import alquiler.Alquiler.EstadoAlquiler;
import autos.Auto;
import disponibles.AutoPorFecha;
import disponibles.DisponibleServicio;
import estadistica.Estadistica.Mes;

@Named("Estadisticas")
public class EstadisticaServicio extends AbstractFactoryAndRepository {
	
	public String iconName(){
		return "estadistica";
	}	
	
	@Named("Consulta")
	public List<Estadistica> estadistica(
			@Named("Año") String ano,
			@Optional
			@Named("Mes") Mes mes,
			@Optional
			@Named("Categoria") Categoria categoria) {
		
		List<Estadistica> listaEstadistica = new ArrayList<Estadistica>();		
		final List<Auto> autos = listaAutos();
		
		List<Estadistica> listaEst = traerEstadisticas();
        for(Estadistica h : listaEst) {
            remove(h);
        }
		
		if (mes!=null){
			LocalDate fechaInicio = diaInicio(mes, ano);
			LocalDate hastaAux = diaFin(mes, ano);
			LocalDate fechaAux=fechaInicio;
			
			for (Auto auto : autos){				
			Estadistica est = newTransientInstance(Estadistica.class);
			if (categoria==null){
				for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
					int suma=0;				
						if(auto.getPatente()!=est.getPatente()){
							est.setPatente(auto.getPatente());
							est.setModeloAuto(auto.getModelo());
							est.setCategoria(auto.getCategoria());
						}
						if (existeAlquiler(fechaAux, est.getPatente()) != null) {
							suma=est.getCantAlq();
							suma=suma+1;
							est.setCantAlq(suma);					
						}		
						fechaAux = fechaInicio.plusDays(i + 1);						
				}
				persistIfNotAlready(est);
				listaEstadistica.add(est);
			}else{
				if (categoria!=null & auto.getCategoria()==categoria){
					for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
						int suma=0;				
							if(auto.getPatente()!=est.getPatente()){
								est.setPatente(auto.getPatente());
								est.setModeloAuto(auto.getModelo());
								est.setCategoria(auto.getCategoria());
							}
							if (existeAlquiler(fechaAux, est.getPatente()) != null) {
								suma=est.getCantAlq();
								suma=suma+1;
								est.setCantAlq(suma);					
							}		
							fechaAux = fechaInicio.plusDays(i + 1);						
					}
					persistIfNotAlready(est);
					listaEstadistica.add(est);
				}
			}
			}		
		}else{
			LocalDate fechaInicio = new LocalDate(ano+"-01-01"); 
			LocalDate hastaAux = new LocalDate(ano+"-12-31");
			LocalDate fechaAux=fechaInicio;
			
			System.out.println("Fecha Anual inicio : " + fechaInicio);
			System.out.println("Fecha Anual fin: "+ hastaAux);
			
			for (Auto auto : autos){				
				Estadistica est = newTransientInstance(Estadistica.class);
				if (categoria==null){
					for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
						int suma=0;				
							if(auto.getPatente()!=est.getPatente()){
								est.setPatente(auto.getPatente());
								est.setModeloAuto(auto.getModelo());
								est.setCategoria(auto.getCategoria());
							}
							if (existeAlquiler(fechaAux, est.getPatente()) != null) {
								suma=est.getCantAlq();
								suma=suma+1;
								est.setCantAlq(suma);					
							}		
							fechaAux = fechaInicio.plusDays(i + 1);						
					}
					persistIfNotAlready(est);
					listaEstadistica.add(est);
				}else{
					if (categoria!=null & auto.getCategoria()==categoria){
						for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
							int suma=0;				
								if(auto.getPatente()!=est.getPatente()){
									est.setPatente(auto.getPatente());
									est.setModeloAuto(auto.getModelo());
									est.setCategoria(auto.getCategoria());
								}
								if (existeAlquiler(fechaAux, est.getPatente()) != null) {
									suma=est.getCantAlq();
									suma=suma+1;
									est.setCantAlq(suma);					
								}		
								fechaAux = fechaInicio.plusDays(i + 1);						
						}
						persistIfNotAlready(est);
						listaEstadistica.add(est);
					}
				}
			}		
		}return listaEstadistica;		
	}
	public List<Categoria> choices2Estadistica(){
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
	
	//{{ Listado de autos
	@Programmatic
	public List<Auto> listaAutos() {
		return allMatches(QueryDefault.create(Auto.class, "listadoAutosActivos"));
	}
	// }}
	
	//{{ Listado de estadisticas 
	@Programmatic
	public List<Estadistica> traerEstadisticas() {
		return allMatches(QueryDefault.create(Estadistica.class, "listarEstadisticas"));
	}
	// }}
	
	// {{ Existencia de alquileres
	@Hidden
	public AutoPorFecha existeAlquiler(final LocalDate fecha,
			final String patente) {
		return uniqueMatch(AutoPorFecha.class, new Filter<AutoPorFecha>() {
			@Override
			public boolean accept(AutoPorFecha auto) {
				return auto.getFecha().equals(fecha)
						&& auto.getPatente().equals(patente) && 
						auto.getAlquiler().getEstado()==EstadoAlquiler.CERRADO;
			}
		});
	}
	// }}
	
	protected LocalDate diaInicio(final Mes mes, final String ano){
		LocalDate fechaAux=null;
		
		switch(mes){ 
		case ENERO:fechaAux=new LocalDate(ano+"-01-01");break;
		case FEBRERO:fechaAux=new LocalDate(ano+"-02-01");break;
		case MARZO:fechaAux=new LocalDate(ano+"-03-01");break;
		case ABRIL:fechaAux=new LocalDate(ano+"-04-01");break;
		case MAYO:fechaAux=new LocalDate(ano+"-05-01");break;
		case JUNIO:fechaAux=new LocalDate(ano+"-06-01");break;
		case JULIO:fechaAux=new LocalDate(ano+"-07-01");break;
		case AGOSTO:fechaAux=new LocalDate(ano+"-08-01");break;
		case SEPTIEMBRE:fechaAux=new LocalDate(ano+"-09-01");break;
		case OCTUBRE:fechaAux=new LocalDate(ano+"-10-01");break;
		case NOVIEMBRE:fechaAux=new LocalDate(ano+"-11-01");break;
		case DICIEMBRE:fechaAux=new LocalDate(ano+"-12-01");break;
		}
		return fechaAux;
	}
	protected LocalDate diaFin(final Mes mes, final String ano){
		LocalDate fechaAux=null;
		
		switch(mes){ 
		case ENERO:fechaAux=new LocalDate(ano+"-01-31");break;
		case FEBRERO:fechaAux=new LocalDate(ano+"-02-28");break;
		case MARZO:fechaAux=new LocalDate(ano+"-03-31");break;
		case ABRIL:fechaAux=new LocalDate(ano+"-04-30");break;
		case MAYO:fechaAux=new LocalDate(ano+"-05-31");break;
		case JUNIO:fechaAux=new LocalDate(ano+"-06-30");break;
		case JULIO:fechaAux=new LocalDate(ano+"-07-31");break;
		case AGOSTO:fechaAux=new LocalDate(ano+"-08-30");break;
		case SEPTIEMBRE:fechaAux=new LocalDate(ano+"-09-30");break;
		case OCTUBRE:fechaAux=new LocalDate(ano+"-10-31");break;
		case NOVIEMBRE:fechaAux=new LocalDate(ano+"-11-30");break;
		case DICIEMBRE:fechaAux=new LocalDate(ano+"-12-31");break;
		}
		return fechaAux;
	}	
	
	@SuppressWarnings("unused")
	private DisponibleServicio dispServ;
    @Hidden
    public void inyectarAdicionalServicio(DisponibleServicio dispServ){
    	this.dispServ=dispServ;
    }
}
