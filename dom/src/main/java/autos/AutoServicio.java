package autos;

import java.util.Date;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;

import categoria.Categoria;
import com.google.common.base.Objects;
import autos.Auto;
import autos.Auto.Seguro;
import autos.Auto.TipoCombustible;
import marca.Marca;
 

@Named("Flota")
public class AutoServicio extends AbstractFactoryAndRepository {
	// {{ 
	@MemberOrder(sequence = "1") 
	@Named("Cargar Auto")
	public Auto cargarAuto(			
		@Named("Patente") String patente,
		@Named("Marca") Marca marca, 
		@Named("Modelo") String modelo, 
		@Named("Año") int ano,
		@Named("Categoria") Categoria categ,
		@Named("Color") String color,
		@Named("Kilometraje") int kms,
		@Named("Capacidad Baul (lt)") int baul,
		@Named("Tipo de Combustible") TipoCombustible combustible,
		@Named("Fecha de Compra") Date fechaCompra,
		@Named("Compañía de Seguro")Seguro seguro) { 
		final boolean activo=true;
		final String ownedBy = currentUserName();
		return elAuto(patente,marca,modelo,ano,categ,color,kms,baul,combustible,fechaCompra,seguro,activo, ownedBy);
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
			getContainer().warnUser("YA SE ENCUENTRA ESTE AUTO");
		}		
		return auto;
    }
	// }}
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
   	// {{ 	
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
    // {{ Helpers
	protected boolean ownedByCurrentUser(final Auto t) {
	    return Objects.equal(t.getOwnedBy(), currentUserName());
	}
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}
	// }}		
}