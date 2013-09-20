package localidad;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;


@Named("Localidad")
public class LocalidadServicio extends AbstractFactoryAndRepository {
	
	@MemberOrder(sequence = "1") // Carga de localidad	
	public Localidad CargaDeLocalidad(@Named("Localidad") String localidad) { 
		final boolean activo=true;
		final String ownedBy = currentUserName();
		return laLocalidad(localidad,activo, ownedBy); 
	}
	@Hidden // for use by fixtures
	public  Localidad laLocalidad(
		String localidad,
		boolean activo,
		String userName) 
	{
	final Localidad aux = newTransientInstance(Localidad.class);
		aux.setNombreLocalidad(localidad);
		aux.setActivo(activo);
		aux.setOwnedBy(userName);
	   persist(aux);
		return aux;
	}
		
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "2") // Listado de Localidades
	public List<Localidad> ListaDeLocalidades() {
	        List<Localidad> items = doComplete();
	        if(items.isEmpty()) {
	            getContainer().informUser("No hay Ciudades activos :-(");
	        }
	        return items;
	}

	protected List<Localidad> doComplete() {
	        return allMatches(Localidad.class, new Filter<Localidad>() {
	            @Override
	            public boolean accept(final Localidad t) {
	                return  t.getActivo();
	            }
	        });
	}
	
	// {{ Helpers
	protected String currentUserName(){
		return getContainer().getUser().getName(); 
	}
	protected boolean ownedByCurrentUser(final Localidad t) {
	    return Objects.equal(t.getOwnedBy(), currentUserName());
	}
		
}
