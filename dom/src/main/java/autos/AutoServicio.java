package autos;

import java.awt.Container;
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
import org.apache.isis.applib.util.TitleBuffer;


import com.google.common.base.Objects;


import autos.Auto;
import autos.Auto.Estado;
import autos.Auto.Seguro;
import autos.Auto.TipoCombustible;
import marca.Marca;
 

@Named("Flota")
public class AutoServicio extends AbstractFactoryAndRepository {
	
	// {{ 
	@MemberOrder(sequence = "1") // Carga de Autos
	public Auto CargarAuto(
			
		@Named("Patente") String patente,
		@Named("Marca") Marca marca, 
		@Named("Modelo") String modelo, 
		@Named("Año") int ano,
		@Named("Color") String color,
		@Named("Kilometraje") int kms,
		@Named("Capacidad Baul (lt)") int baul,
		@Named("Tipo de Combustible") TipoCombustible combustible,
		@Named("Estado de Alquiler") Estado estado,
		@Named("Fecha de Compra") Date fechaCompra,
		@Named("Compañía de Seguro")Seguro seguro) { 
		final boolean activo=true;
		final String ownedBy = currentUserName();
		return elAuto(patente,marca,modelo,ano,color,kms,baul,combustible,estado,fechaCompra,seguro,activo, ownedBy);
			     
	}
	// }}
	

	
	
	
	
	// {{
	@Hidden // for use by fixtures
	public Auto elAuto(
		final String patente,
		final Marca marca, 
		final String modelo,
		final int ano,
		final String color,
		final int kms, 
		final int baul,
		final TipoCombustible combustible,
		final Estado estado,
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
			auto.setColor(color);
			auto.setKilometraje(kms);
			auto.setCapacidadBaul(baul);
			auto.setTipoCombustible(combustible);
			auto.setEstado(estado);
			auto.setFechaCompra(fechaCompra);
			auto.setSeguro(seguro);
			auto.setActivo(activo);
			auto.setOwnedBy(userName);
 
			persistIfNotAlready(auto);
		
		}
		else {
			auto = null;
//			 
				getContainer().warnUser("YA SE ENCUENTRA ESTE AUTO");
				System.out.println("YA SE ENCUENTRA ESTE AUTO");
		}
		
		return auto;
    }
 
	
	// {{ complete (action)
    @ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "2") // Listado de Autos
    public List<Auto> AutosActivos() {
        List<Auto> items = doComplete();
        if(items.isEmpty()) {
            getContainer().informUser("No hay autos activos :-(");
        }
        return items;
    }

    protected List<Auto> doComplete() {
        return allMatches(Auto.class, new Filter<Auto>() {
            @Override
            public boolean accept(final Auto t) {
                return t.getActivo() && t.getMarca().getActivo();
            }
        });
    }
    // }}	
	
    // }}
    @MemberOrder(sequence = "3") // Busqueda de Autos
    public List<Auto> busquedaAutos(@Named("Patente")final String description) {    	
        return allMatches(Auto.class, new Filter<Auto>() {
            @Override
            public boolean accept(final Auto t) {
                return  t.getPatente().contains(description) && t.getActivo();  
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