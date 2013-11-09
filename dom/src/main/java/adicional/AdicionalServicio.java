package adicional;

import java.util.List;


import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;

import autos.Auto;

import com.google.common.base.Objects;


@Named("Adicionales")
public class AdicionalServicio extends AbstractFactoryAndRepository{
			
	@MemberOrder(sequence = "1") 
	@Named("Cargar Adicional")
	public Adicional cargar(			
			@Named("Nombre") String nombre,
			@Optional
			@MultiLine
			@Named("Descripcion") String descrip,
			@Named("Precio") float precio){
			final boolean activo=true;
			final String ownedBy = currentUserName();
			return adicional(nombre,descrip,precio,activo,ownedBy);
	}
	@Hidden
	public Adicional adicional(
			final String nombre,
			final String descrip,
			final float precio,
			final boolean activo,
			final String userName) {
			Adicional adic=newTransientInstance(Adicional.class);
			adic.setNombre(nombre);
			adic.setDescripcion(descrip);
			adic.setPrecio(precio);
			adic.setActivo(activo);
			adic.setOwnedBy(userName);
			persistIfNotAlready(adic);
			return adic;
	}
    // {{Lista de adicionales 
    public List<Adicional> listaAdicionales() {         
        return allMatches(QueryDefault.create(Adicional.class, "listaAdicionales"));
    } 
    // }}
	
	// {{AutoComplete  
	@Hidden    
	public List<Adicional> autoComplete(final String nombre) {
		return allMatches(Adicional.class, new Filter<Adicional>() {
		@Override
		public boolean accept(final Adicional t) {		
			return t.getNombre().contains(nombre) && t.getActivo();
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
