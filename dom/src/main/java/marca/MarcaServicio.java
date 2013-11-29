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
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "marca";
	}
	/**
	 * Se carga la Marca del vehiculo.
	 * 
	 * @param marca
	 * 
	 * @return Marca
	 */
	@MemberOrder(sequence = "1")
	public Marca cargarMarca(
			@RegEx(validation = "[A-Za-z]+")
			@Named("Marca") String marca) { 
		final boolean activo=true;
		final String ownedBy = currentUserName();
		return laMarca(marca.toUpperCase(), activo, ownedBy); 
	}
	
	/**
	 * Metodo que setea todas las propiedades de la Marca del vehiculo
	 * y lo persiste.
	 * Corrobora que no exista una igual en el sistema.
	 * 
	 * @param marca
	 * @param activo
	 * @param userName
	 * 
	 * @return Marca
	 */
	@Hidden
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
	
	/**
	 * Retorna un listado de Marcas activas.
	 * 
	 * @return List<Marca>
	 */
	@MemberOrder(sequence = "2") 
	public List<Marca> listadoMarcas() {
	     return allMatches(Marca.class, new Filter<Marca>() {
	     @Override
	     public boolean accept(final Marca t) {
	     return t.getActivo();
	     }
	   });
	}
	/**
	 * Retorna un listado de vehiculos filtrados por Marca.
	 * @param marca
	 * @return List<Auto>
	 */
	@MemberOrder(sequence = "3")
	public List<Auto> listadoAutosPorMarca(final Marca marca) {
		return allMatches(Auto.class, new Filter<Auto>() {
		@Override
		public boolean accept(Auto t){
		return  marca.equals(t.getMarca())&& t.getActivo();
		}
	  });
	}
	/**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link MarcaServicio#listadoAutosPorMarca(Marca)}
     * 
     * @return List<Marca>
     */
	public List<Marca> choices0ListadoAutosPorMarca(){
		List<Marca> items=listadoMarcas();
		return items;
	}
	/**
     * Accion de Autocompletado generada por el framework, 
     * retorna una lista de los objetos de la entidad.
     *
     * @param marcas
     * 
     * @return List<Marca>
     */
	@Hidden    
	public List<Marca> autoComplete(final String marcas) {
		return allMatches(Marca.class, new Filter<Marca>() {
		@Override
		public boolean accept(final Marca t) {		
		return t.getActivo() && t.getNombre().contains(marcas) ; 
		}
	  });				
	}
	/**
	 * Helpers
	 * 
	 * Retorna un boolean que determina 
	 * si el usuario que se le está pasando por parametro es el mismo.
	 * 
	 * @param marca
	 * @return boolean
	 * 
	 */
	protected boolean ownedByCurrentUser(final Marca marca)	{
		return Objects.equal(marca.getOwnedBy(), currentUserName()); 
	}
	/**
	 * Helpers
	 * 
	 * Retorna el usuario.
	 * 
	 * @return String
	 */
	protected String currentUserName(){
		return getContainer().getUser().getName(); 
	}
}