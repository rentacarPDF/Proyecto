package marca;

 
import java.util.List;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.AbstractFactoryAndRepository;

import com.google.common.base.Objects;

import autos.Auto;



@Named("Marca")
public class MarcaServicio extends AbstractFactoryAndRepository {
	public String iconName(){
		return "marca";
	}
	//{{ Carga de Marcas
	@MemberOrder(sequence = "1")
	public Marca cargarMarca(
			@RegEx(validation = "[A-Za-z]+")
			@Named("Marca") String marca) { 
		final boolean activo=true;
		final String ownedBy = currentUserName();
		return laMarca(marca.toUpperCase(), activo, ownedBy); 
	}
	// }}
	// {{	
	@Hidden // for use by fixtures
	public Marca laMarca(
		final String marca,
		final boolean activo,
		final String userName) {
		final List<Marca> mismaCategoria= allMatches(Marca.class,new Filter<Marca>(){
			@Override
			public boolean accept(final Marca marc){
				return Objects.equal(marc.getNombre(), marca);
			}
		});
		Marca aux = newTransientInstance(Marca.class);
		if(mismaCategoria.size()==0)
		{
		aux.setNombre(marca);
		aux.setActivo(activo);
		aux.setOwnedBy(userName);
		persist(aux);
		}
		else{
			aux = null;
			getContainer().warnUser("EN EL SISTEMA YA SE ENCUENTRA UNA MARCA CON ESE NOMBRE");
		}
		return aux;
	}
	// }}	
	// {{ Listado de Marcas 
	@MemberOrder(sequence = "2") 
	public List<Marca> listadoMarcas() {
	     return allMatches(Marca.class, new Filter<Marca>() {
	     @Override
	     public boolean accept(final Marca t) {
	     return t.getActivo();
	     }
	   });
	}
	// }}
	// {{ Listado de Autos filtrado por Marcas	
	@MemberOrder(sequence = "3")
	public List<Auto> listadoAutosPorMarca(final Marca lista) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(Auto t){
		return  lista.equals(t.getMarca())&& t.getActivo();
		}
	  });
	}
	public List<Marca> choices0ListadoAutosPorMarca(){
		List<Marca> items=listadoMarcas();
		return items;
	}
	// }}
	// {{ 
	@Hidden    
	public List<Marca> autoComplete(final String marcas) {
		return allMatches(Marca.class, new Filter<Marca>() {
		@Override
		public boolean accept(final Marca t) {		
		return t.getActivo() && t.getNombre().contains(marcas) ; 
		}
	  });				
	}
	// }}
	// {{ Helpers
	protected boolean ownedByCurrentUser(final Marca t)	{
		return Objects.equal(t.getOwnedBy(), currentUserName()); 
	}
	protected String currentUserName(){
		return getContainer().getUser().getName(); 
	}
	// }}	
}