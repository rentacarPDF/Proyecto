package categoria;

import java.util.List;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;

import com.google.common.base.Objects;


import categoria.Categoria.Caja;
import categoria.Categoria.Traccion;


@Named("Categorias")
public class CategoriaServicio extends AbstractFactoryAndRepository {
	
	/*@MemberOrder(sequence="1")
	public Categoria CargarCategoria(
			@Named("Categoria")String categoria,
			@Named("Cantidad de puertas")int cantPuert,
			@Named("Cantidad de plazas")int cantPlaz,
			@Named("Tipo de caja")Caja caja,
			@Named("Tipo de traccion") Traccion traccion,
			@Named("Precio de la categoria")int precio)
	{   
		final String ownedBy = currentUserName();
		final boolean activo= true;
		return laCategoria(categoria,cantPuert,cantPlaz,caja,traccion,precio,ownedBy,activo);
	}*/
	
	@Hidden
	public Categoria laCategoria(
		String cat,
		int cantPuert,
		int cantPlaz,
		Caja caja,
		Traccion traccion,
		int precio,
		String userName,
		boolean activo)
		{
			final Categoria categoria= newTransientInstance(Categoria.class);
			categoria.setCategoria(cat);
			categoria.setCantPuertas(cantPuert);
			categoria.setCantPlazas(cantPlaz);
			categoria.setCaja(caja);
			categoria.setTraccion(traccion);
			categoria.setPrecio(precio);
			categoria.setOwnedBy(userName);
			categoria.setActivo(true);
			persist(categoria);
			return categoria;
		}
	
	// {{ complete (action)
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    protected List<Categoria> CategoriaActivos() {
        List<Categoria> items = doComplete();
        if(items.isEmpty()) {
            getContainer().informUser("No hay categorias activas :-(");
        }
        return items;
    }

    protected List<Categoria> doComplete() {
        return allMatches(Categoria.class, new Filter<Categoria>() {
            @Override
            public boolean accept(final Categoria t) {
                return t.getActivo();
            }
        });
    }
    // }}	
		
	// {{ helpers
	protected boolean ownedByCurrentUser(final Categoria t) {
	    return Objects.equal(t.getOwnedBy(), currentUserName());
	}
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}
	//}}	
}