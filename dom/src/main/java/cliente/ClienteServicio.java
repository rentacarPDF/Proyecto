package cliente;

import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.RegEx;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;

import com.google.common.base.Objects;
import cliente.Cliente.TipoId;

@Named("Cliente")
public class ClienteServicio extends AbstractFactoryAndRepository {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "cliente";
	}
	/**
	 * Se realiza la carga de los Clientes, con todos sus atributos.
	 * 
	 * @param nombre
	 * @param apellido
	 * @param tipo
	 * @param numeroId
	 * @param numeroTel
	 * @param mail
	 * 
	 * @return Cliente
	 */
	@MemberOrder(sequence = "1")
	@Named("Cargar Cliente")
	public Cliente cargarCliente(
			@RegEx(validation = "[A-Za-z ]+")
			@Named("Nombre") String nombre,
			@RegEx(validation="[A-Za-z ]+")
			@Named("Apellido") String apellido,
			@Named("Tipo de Id Tributaria") TipoId tipo,
			@RegEx(validation="\\d{2}-\\d{7,8}-\\d{1,2}")
			@Named("Numero") String numeroId,
			@RegEx(validation = "[0-9]+")
			@Named("Numero de Telefono") int numeroTel,
			@RegEx(validation = "(\\w+\\-)*(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+")
			@Named("Correo Electrónico") String mail){
		final String usuario = currentUserName();
		final boolean activo = true;
		return elCliente(nombre.toUpperCase(), apellido.toUpperCase(), tipo, numeroId, numeroTel, mail, usuario, activo);
	}	
	/**
	 * Metodo que setea las diferentes propiedades del Cliente.
	 * Se corrobora que no exista ya un cliente con ese numero de Identificacion Tributaria en nuestro sistema.
	 * Se persiste.
	 * 
	 * @param nombre
	 * @param apellido
	 * @param tipo
	 * @param numeroId
	 * @param numeroTel
	 * @param mail
	 * @param userName
	 * @param activo
	 * @return Cliente
	 */
	@Hidden
	public Cliente elCliente(
			final String nombre, 
			final String apellido, 
			final TipoId tipo,
			final String numeroId, 
			final int numeroTel, 
			final String mail,
			final String userName, 
			final boolean activo) {		
			final List<Cliente> mismoNumDoc = allMatches(Cliente.class,
				new Filter<Cliente>() {
					@Override
					public boolean accept(final Cliente cliente) {
						return Objects.equal(cliente.getNumeroIdent(), numeroId);
					}
				});
			Cliente cliente = newTransientInstance(Cliente.class);
		if (mismoNumDoc.size() == 0) {
			cliente.setNombre(nombre);
			cliente.setApellido(apellido);
			cliente.setTipoId(tipo);
			cliente.setNumeroIdent(numeroId);
			cliente.setNumeroTel(numeroTel);
			cliente.setEmail(mail);
			cliente.setUsuario(userName);
			cliente.setActivo(true);
			persistIfNotAlready(cliente);			
			
		} else {
			cliente = null;
			getContainer().warnUser("YA SE ENCUENTRA EL CLIENTE CARGADO");
			System.out.println("YA SE ENCUENTRA EL CLIENTE CARGADO");
		}
		return cliente;
	} 
	
	/**
	 * Busqueda de Clientes.
	 * Se retorna una lista de Clientes.
	 * 
	 * @param cliente
	 * 
	 * @return List<Cliente>
	 */
	@MemberOrder(sequence = "2")
	@Named("Buscar Cliente")
	public List<Cliente> busquedaCliente(@Named("Apellido del cliente") 
	@RegEx(validation = "[A-Za]+")
	final String clien){
		{
		 final List<Cliente> mismoNumDoc = allMatches(Cliente.class,
					new Filter<Cliente>(){
						@Override
						public boolean accept(final Cliente cliente){
							return Objects.equal(cliente.getApellido(),clien);
						}
					});
			if (mismoNumDoc.size() == 0){
				getContainer().warnUser("NO SE ENCUENTRA EL CLIENTE EN LA BASE DE DATOS");
			}else{
				return listaClientePorApellido(clien);
			}
			return null;
	   }			
	}
	 /**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link ClienteServicio#busquedaCliente(Cliente)}
     * 
     * @return List<Cliente>
	 */
//	public List<Cliente> choices0BusquedaCliente(){
//		return listadoClienteActivos();
//	}
	
	/**
	 * Se retorna un listado de Clientes Activos
	 * 
	 * @return List<Cliente>
	 */
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "3")
	@Named("Listado Clientes")
	public List<Cliente> listadoClienteActivos() {
		List<Cliente> items = listaClientes();
		if (items.isEmpty()) {
			getContainer().informUser("No hay clientes activos");
		}
		return items;
	}
	/**
	 * Lista de clientes activos.
	 * @return List<Cliente>
	 */
	protected List<Cliente> listaClientes() {
		return allMatches(Cliente.class, new Filter<Cliente>() {
			@Override
			public boolean accept(final Cliente t) {
				return t.getActivo();
			}
		});
	}
	@Hidden
	public List<Cliente> listaClientePorApellido(String cliente) {
	            return allMatches(QueryDefault.create(Cliente.class, "traerClientePorApellido","apellido",cliente));
	    }
	/**
     * Accion de Autocompletado generada por el framework, 
     * retorna una lista de los objetos de la entidad.
     *
     * @param cliente
     * 
     * @return List<Cliente>
     */
	@Hidden    
	public List<Cliente> autoComplete(final String cliente) {
		return allMatches(Cliente.class, new Filter<Cliente>() {
		@Override
		public boolean accept(final Cliente t) {		
		return t.getNumeroIdent().contains(cliente) && t.getActivo(); 
		}
	  });				
	}
	/**
	 * Helpers
	 * 
	 * Retorna un boolean que determina 
	 * si el usuario que se le está pasando por parametro es el mismo.
	 * 
	 * @param cliente
	 * @return boolean
	 * 
	 */ 
	protected boolean usuarioCurrentUser(final Cliente cliente) {
		return Objects.equal(cliente.getUsuario(), currentUserName());
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