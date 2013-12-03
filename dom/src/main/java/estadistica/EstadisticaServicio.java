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
import estadistica.Estadistica.Mes;

@Named("Estadisticas")
public class EstadisticaServicio extends AbstractFactoryAndRepository {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "estadistica";
	}	
	
   /**
    * Metodo que trae estadisticas por periodo de alquileres,
    * puede seleccionarse por mes, por categoria, o ambas
    * en un a&ntilde;o determinado.
    * Se calculan los totales de alquileres ya sea por mes o por categoria.
    * 
    * Se retorna una lista de estadisticas.
    * 
    * @param ano
    * @param mes
    * @param categoria
    * 
    * @return List<Estadistica>
    */
   @Hidden
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
    /**
     * Metodo que realiza la estadistica anual por vehiculo en un a&ntilde;o determinado.
     * Se determina la cantidad de alquileres por mes. 
     * 
     * @param ano
     * @return List<Estadistica>
     */
	@Hidden
	public List<Estadistica> estadisticaAnual(String ano) {
		
		List<Estadistica> listaEstadistica = new ArrayList<Estadistica>();		
		
		List<Estadistica> listaEst = traerEstadisticas();
        for(Estadistica h : listaEst) {
            remove(h);
        }        
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
		return listaEstadistica;
	}
	/**
	 * Metodo que retorna la diferencia entre el dia desde y el dia hasta,
	 * para realizar calculos.
	 * 
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
	 * Retorna un listado de vehiculos activos.
	 * @return List<Auto>
	 */
	@Programmatic
	public List<Auto> listaAutos() {
		return allMatches(QueryDefault.create(Auto.class, "listadoAutosActivos"));
	}
	/**
	 * Retorna un listado de estadisticas.
	 * @return List<Estadistica>
	 */
	@Programmatic
	public List<Estadistica> traerEstadisticas() {
		return allMatches(QueryDefault.create(Estadistica.class, "listarEstadisticas"));
	}
 
	/**
	 * Retorna un listado de estadisticas por un vehiculo determinado por la patente.
	 * @param patente
	 * @return List<Estadistica>
	 */
	@Programmatic
	public List<Estadistica> listaEstadisticaPorAuto(String patente){
		return allMatches(QueryDefault.create(Estadistica.class, "listaEstadisticasPorAuto","auto",patente));	
	}
	
    /**
     * Retorna un listado de vehiculos filtrado por la Categoria.
     * @param categoria
     * @return List<Auto>
     */
	@Programmatic
	public List<Auto> listaAutosCate(final Categoria categoria) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(Auto t){
		return  categoria.equals(t.getCategoria())&& t.getActivo();
		}
	  });
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
	public AutoPorFecha existeAlquiler(final LocalDate fecha,
			final String patente) {
		return uniqueMatch(AutoPorFecha.class, new Filter<AutoPorFecha>() {
			@Override
			public boolean accept(AutoPorFecha auto) {
				return auto.getFecha().equals(fecha)
						&& auto.getPatente().equals(patente) && 
						auto.getAlquiler().getEstado()==EstadoAlquiler.FINALIZADO;
			}
		});
	}
	/**
	 * Metodo que dependiendo de la seleccion del usuario en {@link EstadisticaServicio#estadistica} 
	 * devuelve la fecha de inicio con los parametros que brinda el usuario.
	 * 
	 * Se genera la fecha en LocalDate, ya que es pasado como String y una Enumeracion
	 * 
	 * @param mes
	 * @param ano
	 * @return LocalDate
	 */
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
	/**
	 * Metodo que dependiendo de la seleccion del usuario en {@link EstadisticaServicio#estadistica(String, Mes, Categoria)}
	 * devuelve la fecha de fin con los parametros que brinda el usuario.
	 * 
	 * Se genera la fecha en LocalDate, ya que es pasado como String y una Enumeracion
	 * 
	 * @param mes
	 * @param ano
	 * @return LocalDate
	 */
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
	/**
	 * Metodo que dependiendo de la seleccion del usuario en {@link EstadisticaServicio#estadisticaAnual(String)}
	 * devuelve el mes con los parametros que brinda el usuario.
	 * 
	 * @param fecha
	 * @param ano
	 * @return LocalDate
	 */
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
	/**
	 * Metodo que devuelve un grafico de WickedCharts 
	 * para representar la consulta de alquileres por periodo.
	 * 
	 * @param ano
	 * @param mes
	 * @param categoria
	 * 
	 * @return WickedChart
	 */
    @Named("Consulta Por Periodo")
    @MemberOrder(sequence="1") 
    public WickedChart crearGraficoPeriodo(
			@Named("Año") String ano,
			@Optional
			@Named("Mes") Mes mes,
			@Optional
			@Named("Categoria") Categoria categoria) {
    	
    	estadistica(ano, mes, categoria);       
    
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
	        if(categoria==null){        
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
      /**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link EstadisticaServicio#crearGraficoPeriodo}
     * 
     * Retorna una lista de a&ntilde;os.
     * 
     * @return List<String>
     */
    public List<String> choices0CrearGraficoPeriodo(){
    	Calendar cal= Calendar.getInstance();
    	int anoActual= cal.get(Calendar.YEAR);    	
    	List<String> lista = new ArrayList<String>();
    	
    	for(int a=0; a<=10; a++)	{ 
    		int calculo= anoActual-a;
    		lista.add(Integer.toString(calculo));
    	}
    	return lista;
    }
    /**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link EstadisticaServicio#crearGraficoPeriodo}
     * 
     * Retorna una lista de Categorias activas.
     * 
     * @return List<Categoria>
     */
	public List<Categoria> choices2CrearGraficoPeriodo(){
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
  
   /**
   * Metodo que devuelve un grafico de WickedCharts 
   * para representar la consulta anual de alquileres.
   * 
   * @param ano
   * 
   * @return WickedChart
   */
    @Named("Consulta Anual")
    @MemberOrder(sequence="2") 
    public WickedChart crearGraficoAnual(
			@Named("Año") String ano) {
    	
    	estadisticaAnual(ano);
    	
        Options options = new Options();

        options
            .setChartOptions(new ChartOptions()
                .setType(SeriesType.LINE));

        options
            .setTitle(new Title("ESTADISTICAS AÑO "+ano));

        options
            .setyAxis(new Axis()
                .setTitle(new Title("CANTIDAD DE ALQUILERES AÑO "+ano)));

        options
            .setLegend(new Legend()
                .setLayout(LegendLayout.VERTICAL)
                .setAlign(HorizontalAlignment.RIGHT)
                .setVerticalAlign(VerticalAlignment.TOP)
                .setX(-10)
                .setY(100)
                .setBorderWidth(0));
		
        
        List<Auto> listaAutos=listaAutos();
        List<Estadistica> listaEst=null;
        
	        if(ano!=null){		        	
		        for(Auto a : listaAutos) {   
		        	listaEst = listaEstadisticaPorAuto(a.getPatente());
			        options
			        .addSeries(new SimpleSeries()
			            .setName(a.getPatente())
			            .setData(
			                Arrays
			                    .asList(new Number[] {listaEst.get(0).getCantAlq(),listaEst.get(1).getCantAlq(),listaEst.get(2).getCantAlq(),listaEst.get(3).getCantAlq(),listaEst.get(4).getCantAlq(),listaEst.get(5).getCantAlq(),listaEst.get(6).getCantAlq(),listaEst.get(7).getCantAlq(),listaEst.get(8).getCantAlq(),listaEst.get(9).getCantAlq(),listaEst.get(10).getCantAlq(),listaEst.get(11).getCantAlq()})));
				        options
				            .setxAxis(new Axis()            	
				                .setCategories(Arrays
				                    .asList(new String[] {"ENERO","FEBRERO","MARZO","ABRIL","MAYO","JUNIO","JULIO","AGOSTO","SEPTIEMBRE","OCTUBRE","NOVIEMBRE","DICIEMBRE"})));	        	     	        
				}
	        }            
        
        return new WickedChart(options);
    }    
    
    /**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link EstadisticaServicio#crearGraficoAnual}
     * 
     * Retorna una lista de a&ntilde;os.
     * 
     * @return List<String>
     */
    public List<String> choices0CrearGraficoAnual(){
    	Calendar cal= Calendar.getInstance();
    	int anoActual= cal.get(Calendar.YEAR);    	
    	List<String> lista = new ArrayList<String>();
    	
    	for(int a=0; a<=10; a++)	{ 
    		int calculo= anoActual-a;
    		lista.add(Integer.toString(calculo));
    	}
    	return lista;
    }    
}
