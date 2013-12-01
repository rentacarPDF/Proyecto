package categoria;

import java.util.List;


import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;

import autos.Auto;

import com.google.common.base.Objects;
import categoria.Categoria.Caja;
import categoria.Categoria.Traccion;


@Named("Categoria")
public class CategoriaServicio extends AbstractFactoryAndRepository {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "categoria";
	}	
	
	/**
	 * Metodo mediante el cual se realiza la carga de la Categoria.
	 * 
	 * @param categoria
	 * @param cantPuert
	 * @param cantPlaz
	 * @param caja
	 * @param traccion
	 * @param precio
	 * 
	 * @return Categoria
	 */
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
			@Named("Precio de la categoria")String precio)
	{   final String usuario = currentUserName();
		final boolean activo= true;
		return laCategoria(categoria.toUpperCase(),cantPuert,cantPlaz,caja,traccion,precio,usuario,activo);
	}
	/**
	 * Metodo que setea las diferentes propiedades de la Categoria y lo persiste.
	 * Corrobora que la categoria ingresada no exista ya en el sistema.
	 * 
	 * @param cat
	 * @param cantPuert
	 * @param cantPlaz
	 * @param caja
	 * @param traccion
	 * @param precio
	 * @param userName
	 * @param activo
	 * 
	 * @return Categoria
	 */
	@Hidden
	public Categoria laCategoria(
		final String cat,
		final int cantPuert,
		final int cantPlaz,
		final Caja caja,
		final Traccion traccion,
		final String precio,
		final String userName,
		boolean activo)
		{
		final List<Categoria> mismoNombre= allMatches(Categoria.class, new Filter<Categoria>(){
			@Override
			public boolean accept(final Categoria categoria){
				return Objects.equal(categoria.getNombre(), cat);
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
			categoria.setUsuario(userName);
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
	
	/**
	 * Se retorna un listado de Categorias activas
	 * @return List<Categoria>
	 */
    @ActionSemantics(Of.SAFE)
    @MemberOrder(sequence = "2")
    @Named("Listado Categorias")
    public List<Categoria> categoriaActivos() {
        List<Categoria> items = listaCategorias();
        if(items.isEmpty()) {
            getContainer().informUser("No hay categorias activas :-(");
        }
        return items;
    }
    /**
     * Lista de categorias activas.
     * @return
     */
    protected List<Categoria> listaCategorias() {
        return allMatches(Categoria.class, new Filter<Categoria>() {
            @Override
            public boolean accept(final Categoria t) {
                return t.getActivo();
            }
        });
    }
	 /**
	  * Listado de autos filtrado por Categoria
	  * @param lista
	  * @return List<Auto>
	  */
    @MemberOrder(sequence="3") 
	public List<Auto> listadoAutosPorCategoria(final Categoria lista) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(Auto t){
		return  lista.equals(t.getCategoria())&& t.getActivo();
		}
	  });
	}
    /**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link CategoriaServicio#listadoAutosPorCategoria(Categoria)}
     * 
     * @return List<Categoria>
     */
    public List<Categoria> choices0ListadoAutosPorCategoria(){
    	List<Categoria> items = listaCategorias();
    	return items;
    }    
	
    /**
     * Accion de Autocompletado generada por el framework, 
     * retorna una lista de los objetos de la entidad.
     *
     * @param cat
     * 
     * @return List<Categoria>
     */
	@Hidden    
	public List<Categoria> autoComplete(final String cat) {
		return allMatches(Categoria.class, new Filter<Categoria>() {
		@Override
		public boolean accept(final Categoria t) {		
		return  t.getNombre().contains(cat) && t.getActivo(); 
		}
	  });				
	}
	
	/**
	 * Helpers
	 * 
	 * Retorna un boolean que determina 
	 * si el usuario que se le está pasando por parametro es el mismo.
	 * 
	 * @param categoria
	 * @return boolean
	 * 
	 */
	protected boolean usuarioCurrentUser(final Categoria categoria) {
	    return Objects.equal(categoria.getUsuario(), currentUserName());
	}
	/**
	 * Helpers
	 * 
	 * Retorna el usuario.
	 * 
	 * @return String
	 */
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}	
}