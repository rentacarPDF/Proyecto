package adicional;

import java.math.BigDecimal;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import com.google.common.base.Objects;

@Named("Adicionales")
public class AdicionalServicio extends AbstractFactoryAndRepository{
	/**
	 * Retorna el nombre del adicional
	 * @return String
	 */
	public String iconName(){
		return "adicionales";
	}
	/**
	 * Se realiza la carga del Adicional
	 * @param nombre
	 * @param descrip
	 * @param precio
	 * 
	 * @return Adicional
	 */
	@MemberOrder(sequence = "1") 
	@Named("Cargar Adicional")
	public Adicional cargar(
			@RegEx(validation="[A-Za-z ]+")
			@Named("Nombre") String nombre,
			@Optional
			@MultiLine
			@MaxLength(100)
			@Named("Descripcion") String descrip,
			@RegEx(validation = "[0-9]+")
			@Named("Precio") BigDecimal precio){
			final boolean activo=true;
			final String usuario = currentUserName();
			return adicional(nombre.toUpperCase(),descrip.toUpperCase(),precio,activo,usuario);
	}
	/**
	 * Se persiste y setea cada una de las propiedades del Adicional
	 * 
	 * @param nombre
	 * @param descrip
	 * @param precio
	 * @param activo
	 * @param userName
	 * 
	 * @return Adicional
	 * 
	 */
	@Hidden
	public Adicional adicional(
			final String nombre,
			final String descrip,
			final BigDecimal precio,
			final boolean activo,
			final String userName) {
			Adicional adic=newTransientInstance(Adicional.class);
			adic.setNombre(nombre);
			adic.setDescripcion(descrip);
			adic.setPrecio(precio);
			adic.setActivo(activo);
			adic.setUsuario(userName);
			persistIfNotAlready(adic);
			return adic;
	}
   /**
    * Lista de adicionales
    * @return List<Adicional>
    */
    public List<Adicional> listaAdicionales() {         
        return allMatches(QueryDefault.create(Adicional.class, "listaAdicionales"));
    } 
    
    /**
     * Accion de Autocompletado generada por el framework, 
     * retorna una lista de los objetos de la entidad.
     * 
     * @param nombre
     * 
     * @return List<adicional.Adicional> 
     */
	@Hidden    
	public List<Adicional> autoComplete(final String nombre) {
		return allMatches(Adicional.class, new Filter<Adicional>() {
		@Override
		public boolean accept(final Adicional t) {		
			return t.getNombre().contains(nombre) && t.getActivo();
		}
	  });				
	}
	
	/**
	 * Helpers
	 * 
	 * Retorna un boolean que determina 
	 * si el usuario que se le est&aacute; pasando por parametro es el mismo.
	 * 
	 * @param t
	 * @return boolean
	 * 
	 */
	protected boolean usuarioCurrentUser(final Adicional t) {
	    return Objects.equal(t.getUsuario(), currentUserName());
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
