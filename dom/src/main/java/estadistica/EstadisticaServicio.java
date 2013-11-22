package estadistica;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import com.danhaywood.isis.wicket.wickedcharts.applib.WickedChart;
import com.googlecode.wickedcharts.highcharts.options.Axis;
import com.googlecode.wickedcharts.highcharts.options.ChartOptions;
import com.googlecode.wickedcharts.highcharts.options.HorizontalAlignment;
import com.googlecode.wickedcharts.highcharts.options.Legend;
import com.googlecode.wickedcharts.highcharts.options.LegendLayout;
import com.googlecode.wickedcharts.highcharts.options.Options;
import com.googlecode.wickedcharts.highcharts.options.SeriesType;
import com.googlecode.wickedcharts.highcharts.options.Title;
import com.googlecode.wickedcharts.highcharts.options.VerticalAlignment;
import com.googlecode.wickedcharts.highcharts.options.series.SimpleSeries;

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
	
	@Named("Consulta Por Periodo")
    @MemberOrder(sequence="1") 
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
			
			if (categoria==null){
				for (Auto auto : autos){				
					Estadistica est = newTransientInstance(Estadistica.class);
					for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
						int suma=0;				
							if(auto.getPatente()!=est.getPatente()){
								est.setPatente(auto.getPatente());
								est.setModeloAuto(auto.getModelo());
								est.setCategoria(auto.getCategoria());
								est.setSeleccionCategoria(false);
								est.setMes(mes.toString());
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
			}else{ // (categoria!=null)
				List<Auto> autosPorCate = listaAutosCate(categoria);
				Estadistica est = newTransientInstance(Estadistica.class);
				for (Auto auto : autosPorCate){
					for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
						int suma=0;				
						est.setCategoria(categoria);	
						est.setSeleccionCategoria(true);
						est.setMes(mes.toString());
						if (existeAlquiler(fechaAux, auto.getPatente()) != null) {
							suma=est.getCantAlq();
							suma=suma+1;
							est.setCantAlq(suma);					
						}		
						fechaAux = fechaInicio.plusDays(i + 1);						
					}					
				}
				persistIfNotAlready(est);
				listaEstadistica.add(est);
			}
		}else{ // (mes==null)
			LocalDate fechaInicio = new LocalDate(ano+"-01-01"); 
			LocalDate hastaAux = new LocalDate(ano+"-12-31");
			LocalDate fechaAux=fechaInicio;
			
			if (categoria==null){
				for (Auto auto : autos){				
					Estadistica est = newTransientInstance(Estadistica.class);
					for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
						int suma=0;				
							if(auto.getPatente()!=est.getPatente()){
								est.setPatente(auto.getPatente());
								est.setModeloAuto(auto.getModelo());
								est.setCategoria(auto.getCategoria());
								est.setSeleccionCategoria(false);
								est.setMes("ENERO-DICIEMBRE");
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
			}else{ // (categoria!=null)
				List<Auto> autosPorCate = listaAutosCate(categoria);
				Estadistica est = newTransientInstance(Estadistica.class);
				for (Auto auto : autosPorCate){
					for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
						int suma=0;				
						est.setCategoria(categoria);	
						est.setSeleccionCategoria(true);
						est.setMes("ENERO-DICIEMBRE");
						if (existeAlquiler(fechaAux, auto.getPatente()) != null) {
							suma=est.getCantAlq();
							suma=suma+1;
							est.setCantAlq(suma);					
						}		
						fechaAux = fechaInicio.plusDays(i + 1);						
					}					
				}
				persistIfNotAlready(est);
				listaEstadistica.add(est);
			}
		}		
		return listaEstadistica;		
	}
	// {{ Choices de Año
    public List<String> choices0Estadistica(){
    	Calendar cal= Calendar.getInstance();
    	int anoActual= cal.get(Calendar.YEAR);    	
    	List<String> lista = new ArrayList<String>();
    	
    	for(int a=0; a<=10; a++)	{ 
    		int calculo= anoActual-a;
    		lista.add(Integer.toString(calculo));
    	}
    	return lista;
    }
	// {{ Choices de Categoria
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
 
	@Named("Consulta Anual")
    @MemberOrder(sequence="2") 
	public List<Estadistica> estadisticaAnual(
			@Named("Año") String ano,
			@Optional
			@Named("Categoria") Categoria categoria) {
		
		List<Estadistica> listaEstadistica = new ArrayList<Estadistica>();		
		
		List<Estadistica> listaEst = traerEstadisticas();
        for(Estadistica h : listaEst) {
            remove(h);
        }
        
		if (categoria==null){
			final List<Auto> autos = listaAutos();			
			
			for (Auto auto : autos){						
				
				for (int a = 1; a<=12; a++){
					Estadistica est = newTransientInstance(Estadistica.class);
					LocalDate fechaInicio = new LocalDate(ano+"-"+a+"-01"); 
					LocalDate hastaAux = verFechaFinal(fechaInicio, ano);
					LocalDate fechaAux=fechaInicio;
									
					for (int i = 0; i <= calculoDias(fechaInicio, hastaAux); i++) {				
						int suma=0;				
						if(auto.getPatente()!=est.getPatente()){
							est.setPatente(auto.getPatente());
							est.setModeloAuto(auto.getModelo());
							est.setCategoria(auto.getCategoria());
							est.setSeleccionCategoria(false);							
						}
						if (existeAlquiler(fechaAux, est.getPatente()) != null) {
							suma=est.getCantAlq();
							suma=suma+1;
							est.setCantAlq(suma);					
						}		
						fechaAux = fechaInicio.plusDays(i + 1);						
					}
					est.setMes(Integer.toString(fechaInicio.getMonthOfYear()));
					persistIfNotAlready(est);
					listaEstadistica.add(est);	
				}			
			}
		}else{ // (categoria!=null)
			
		}	
		return listaEstadistica;
	}
	
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
	
    // {{ Listado de Autos filtrado por Categoria
	@Programmatic
	public List<Auto> listaAutosCate(final Categoria lista) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(Auto t){
		return  lista.equals(t.getCategoria())&& t.getActivo();
		}
	  });
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
	protected LocalDate verFechaFinal(final LocalDate fecha, String ano){
		LocalDate fechaAux = null;

		switch(fecha.getMonthOfYear()){ 
		case 1:fechaAux=new LocalDate(ano+"-01-31");break;
		case 2:fechaAux=new LocalDate(ano+"-02-28");break;
		case 3:fechaAux=new LocalDate(ano+"-03-31");break;
		case 4:fechaAux=new LocalDate(ano+"-04-30");break;
		case 5:fechaAux=new LocalDate(ano+"-05-31");break;
		case 6:fechaAux=new LocalDate(ano+"-06-30");break;
		case 7:fechaAux=new LocalDate(ano+"-07-31");break;
		case 8:fechaAux=new LocalDate(ano+"-08-30");break;
		case 9:fechaAux=new LocalDate(ano+"-09-30");break;
		case 10:fechaAux=new LocalDate(ano+"-10-31");break;
		case 11:fechaAux=new LocalDate(ano+"-11-30");break;
		case 12:fechaAux=new LocalDate(ano+"-12-31");break;
		}
		return fechaAux;
	}	
	
    @Named("Crear Grafico")
    @MemberOrder(sequence="3") 
    public WickedChart createChart() {
        Options options = new Options();

        options
            .setChartOptions(new ChartOptions()
                .setType(SeriesType.COLUMN));

        options
            .setTitle(new Title("ESTADISTICAS"));

        options
            .setyAxis(new Axis()
                .setTitle(new Title("CANTIDAD DE ALQUILERES")));

        options
            .setLegend(new Legend()
                .setLayout(LegendLayout.VERTICAL)
                .setAlign(HorizontalAlignment.RIGHT)
                .setVerticalAlign(VerticalAlignment.TOP)
                .setX(-10)
                .setY(100)
                .setBorderWidth(0));
		
        List<Estadistica> listaEst = traerEstadisticas();
        for(Estadistica h : listaEst) {
	        if(h.getSeleccionCategoria()==false){        
	        options
	        .addSeries(new SimpleSeries()
	            .setName(h.getPatente())
	            .setData(
	                Arrays
	                    .asList(new Number[] { h.getCantAlq() })));
		        options
		            .setxAxis(new Axis()            	
		                .setCategories(Arrays
		                    .asList(new String[] {h.getMes() })));	        	
	        }else{ // (h.getSeleccionCategoria()==true)
                options
                .addSeries(new SimpleSeries()
                    .setName(h.getCategoria().getNombre())
                    .setData(
                        Arrays
                            .asList(new Number[] { h.getCantAlq() })));      	      
       	        options
       	            .setxAxis(new Axis()            	
       	                .setCategories(Arrays
       	                    .asList(new String[] {h.getMes() })));	        	        
	    	}                
        }
        return new WickedChart(options);
    }
    
	
	@SuppressWarnings("unused")
	private DisponibleServicio dispServ;
    @Hidden
    public void inyectarAdicionalServicio(DisponibleServicio dispServ){
    	this.dispServ=dispServ;
    }
}
