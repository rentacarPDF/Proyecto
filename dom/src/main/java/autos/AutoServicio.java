package autos;

import java.util.Arrays;
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

import com.danhaywood.isis.wicket.wickedcharts.applib.WickedChart;
import com.google.common.base.Objects;
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

import autos.Auto;
import autos.Auto.Seguro;
import autos.Auto.TipoCombustible;
import marca.Marca;
 

@Named("Flota")
public class AutoServicio extends AbstractFactoryAndRepository {

	public String iconName(){
		return "auto";
	}
	
	// {{ 
	@MemberOrder(sequence = "1") 
	@Named("Cargar Auto")
	public Auto cargarAuto(
		@RegEx(validation="[A-Za-z]{3}\\d{3}")
		@Named("Patente") String patente,
		@Named("Marca") Marca marca,
		@RegEx(validation = "\\w[@&:\\-\\,\\.\\+ \\w]*")
		@Named("Modelo") String modelo,
		@RegEx(validation = "[0-9]+")
		@Named("Año") int ano,
		@Named("Categoria") Categoria categ,
		@RegEx(validation="[A-Za-z]+")
		@Named("Color") String color,
		@RegEx(validation = "[0-9]+")
		@Named("Kilometraje") int kms,
		@RegEx(validation = "[0-9]+")
		@Named("Capacidad Baul (lt)") int baul,
		@Named("Tipo de Combustible") TipoCombustible combustible,
		@Named("Fecha de Compra") Date fechaCompra,
		@Named("Compañía de Seguro")Seguro seguro) { 
		final boolean activo=true;
		final String ownedBy = currentUserName();
		return elAuto(patente.toUpperCase(),marca,modelo.toUpperCase(),ano,categ,color.toUpperCase(),kms,baul,combustible,fechaCompra,seguro,activo, ownedBy);
	}
	// }}
	// {{
	@Hidden // for use by fixtures
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
			auto.setOwnedBy(userName);
			persistIfNotAlready(auto);
		}
		else {
			auto = null;		 
			getContainer().warnUser("EN EL SISTEMA YA SE ENCUENTRA UN AUTO CON ESTE DOMINIO");
		}		
		return auto;
    }
	// }}
	//{{Validacion de ingreso de fecha de compra
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
	//}}
	//{{chises Cargar auto
	public List<Marca> choices1CargarAuto(){
		List<Marca> items = listaMarcasActivas();		
		return items;
	}
	public List<Categoria> choices4CargarAuto(){
		List<Categoria> items = listaCategoriasActivas();		
		return items;
	}
	//}}
	//{{Listado de Marcas Activas
    protected List<Marca> listaMarcasActivas() {
        return allMatches(Marca.class, new Filter<Marca>() {
            @Override
            public boolean accept(final Marca t) {            	          	
                return t.getActivo();            	          	
            }
        });
    }
    // }}
    //{{Listado de categorias Activas
	protected List<Categoria> listaCategoriasActivas() {
        return allMatches(Categoria.class, new Filter<Categoria>() {
            @Override
            public boolean accept(final Categoria t) {            	          	
                return t.getActivo();            	          	
            }
        });
    }
    // }}	
	// {{ Listado de Autos Activos
    @ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "2") 
    @Named("Listado Autos Activos")
    public List<Auto> listadoAutosActivos(){
    	return allMatches(QueryDefault.create(Auto.class,"listadoAutosActivos"));
    }    
   	// }}	
    // {{  AutoComplite
	@Hidden    
	public List<Auto> autoComplete(final String auto) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(final Auto t) {		
			return t.getPatente().contains(auto) && t.getActivo();
		}
	  });				
	}
	// }}	
	/*public WickedChart createChart() {
        Options options = new Options();

        options
            .setChartOptions(new ChartOptions()
                .setType(SeriesType.COLUMN));

        options
            .setTitle(new Title("My very own chart."));

        options
            .setxAxis(new Axis()
                .setCategories(Arrays
                    .asList(new String[] { "Jan", "Feb", "Mar", "Apr", "May",
                        "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" })));

        options
            .setyAxis(new Axis()
                .setTitle(new Title("Autos x Fecha")));

        options
            .setLegend(new Legend()
                .setLayout(LegendLayout.VERTICAL)
                .setAlign(HorizontalAlignment.RIGHT)
                .setVerticalAlign(VerticalAlignment.TOP)
                .setX(-10)
                .setY(100)
                .setBorderWidth(0));

        options
            .addSeries(new SimpleSeries()
                .setName("Tokyo")
                .setData(
                    Arrays
                        .asList(new Number[] { 7.0, 6.9, 9.5, 14.5, 18.2, 21.5,
                            25.2, 26.5, 23.3, 18.3, 13.9, 9.6 })));

        options
            .addSeries(new SimpleSeries()
                .setName("New York")
                .setData(
                    Arrays
                        .asList(new Number[] { -0.2, 0.8, 5.7, 11.3, 17.0, 22.0,
                            24.8, 24.1, 20.1, 14.1, 8.6, 2.5 })));
        
        return new WickedChart(options);
    }*/
    // {{ Helpers
	protected boolean ownedByCurrentUser(final Auto t) {
	    return Objects.equal(t.getOwnedBy(), currentUserName());
	}
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}
	// }}		
}