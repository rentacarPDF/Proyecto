package categoria;

import java.util.List;


import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;

import autos.Auto;

import com.google.common.base.Objects;
import categoria.Categoria.Caja;
import categoria.Categoria.Traccion;


@Named("Categoria")
public class CategoriaServicio extends AbstractFactoryAndRepository {
	public String iconName(){
		return "categoria";
	}	
	// {{ Carga de Categorias
	@MemberOrder(sequence="1")
	public Categoria cargarCategoria(
			@RegEx(validation="[A-Za-z]+")
			@Named("Categoria")String categoria,
			@RegEx(validation = "[0-9]+")
			@Named("Cantidad de puertas")int cantPuert,
			@RegEx(validation = "[0-9]+")
			@Named("Cantidad de plazas")int cantPlaz,
			@Named("Tipo de caja")Caja caja,
			@Named("Tipo de traccion") Traccion traccion,
			@RegEx(validation = "[0-9]+")
			@Named("Precio de la categoria")int precio)
	{   final String ownedBy = currentUserName();
		final boolean activo= true;
		return laCategoria(categoria.toUpperCase(),cantPuert,cantPlaz,caja,traccion,precio,ownedBy,activo);
	}
	@Hidden
	public Categoria laCategoria(
		final String cat,
		final int cantPuert,
		final int cantPlaz,
		final Caja caja,
		final Traccion traccion,
		final int precio,
		final String userName,
		boolean activo)
		{
		final List<Categoria> mismoNombre= allMatches(Categoria.class, new Filter<Categoria>(){
			@Override
			public boolean accept(final Categoria categoria){
				return java.util.Objects.equals(categoria.getNombre(), cat);
			}
		});
		Categoria categoria= newTransientInstance(Categoria.class);
		if(mismoNombre.size()==0){
			
		
			categoria.setNombre(cat);
			categoria.setCantPuertas(cantPuert);
			categoria.setCantPlazas(cantPlaz);
			categoria.setCaja(caja);
			categoria.setTraccion(traccion);
			categoria.setPrecio(precio);
			categoria.setOwnedBy(userName);
			categoria.setActivo(true);
			persist(categoria);
		}
		else
		{
			categoria=null;
			getContainer().warnUser("EN EL SISTEMA YA SE ENCUENTA UNA CATEGORIA CON ESE NOMBRE");
			
		}
		return categoria;
		}
	// }}
	// {{ Listado de Categorias Activas
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    @NotInServiceMenu
    public List<Categoria> CategoriaActivos() {
        List<Categoria> items = listaCategorias();
        if(items.isEmpty()) {
            getContainer().informUser("No hay categorias activas :-(");
        }
        return items;
    }
    protected List<Categoria> listaCategorias() {
        return allMatches(Categoria.class, new Filter<Categoria>() {
            @Override
            public boolean accept(final Categoria t) {
                return t.getActivo();
            }
        });
    }
    // }}	
    // {{ Listado de Autos filtrado por Categoria
    @MemberOrder(sequence="3") 
	public List<Auto> listadoAutosPorCategoria(final Categoria lista) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(Auto t){
		return  lista.equals(t.getCategoria())&& t.getActivo();
		}
	  });
	}
    public List<Categoria> choices0ListadoAutosPorCategoria(){
    	List<Categoria> items = listaCategorias();
    	return items;
    }    
	// }} 
    // {{AutoComplete 
	@Hidden    
	public List<Categoria> autoComplete(final String cat) {
		return allMatches(Categoria.class, new Filter<Categoria>() {
		@Override
		public boolean accept(final Categoria t) {		
		return  t.getNombre().contains(cat) && t.getActivo(); 
		}
	  });				
	}
	// }}    
	// {{ Helpers
	protected boolean ownedByCurrentUser(final Categoria t) {
	    return Objects.equal(t.getOwnedBy(), currentUserName());
	}
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}
	//}}	
}