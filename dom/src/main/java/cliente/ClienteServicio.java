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
import com.google.common.base.Objects;

import cliente.Cliente.TipoId;



@Named("Cliente")
public class ClienteServicio extends AbstractFactoryAndRepository {
	public String iconName(){
		return "cliente";
	}
	// {{ Carga de clientes
	@MemberOrder(sequence = "1")
	public Cliente cargarCliente(
			@RegEx(validation = "(\\w[@&:\\-\\,\\.\\+ \\w])+[A-Za-z]+")
			@Named("Nombre") String nombre,
			@RegEx(validation = "(\\w[@&:\\-\\,\\.\\+ \\w])+[A-Za-z]+")
			@Named("Apellido") String apellido,
			@Named("Tipo de Id Tributaria") TipoId tipo,
			//@RegEx(validation = "([0-9])")
			@Named("Numero") String numeroId,
			@RegEx(validation = "([0-9])")
			@Named("Numero de Telefono") int numeroTel,
			@RegEx(validation = "(\\w+\\.)*\\w+@(\\w+\\.)+[A-Za-z]+")
			@Named("Correo Electrónico") String mail){
		final String ownedBy = currentUserName();
		final boolean activo = true;
		return elCliente(nombre.toUpperCase(), apellido.toUpperCase(), tipo, numeroId, numeroTel, mail, ownedBy, activo);
	}	
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
			cliente.setOwnedBy(userName);
			cliente.setActivo(true);
			persistIfNotAlready(cliente);			
			
		} else {
			cliente = null;
			getContainer().warnUser("YA SE ENCUENTRA EL CLIENTE CARGADO");
			System.out.println("YA SE ENCUENTRA EL CLIENTE CARGADO");
		}
		return cliente;
	} 
	// }}
	// {{ Listado de Clientes Activos
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "2")
	public List<Cliente> listadoClienteActivos() {
		List<Cliente> items = listaClientes();
		if (items.isEmpty()) {
			getContainer().informUser("No hay clientes activos");
		}
		return items;
	}
	protected List<Cliente> listaClientes() {
		return allMatches(Cliente.class, new Filter<Cliente>() {
			@Override
			public boolean accept(final Cliente t) {
				return t.getActivo();
			}
		});
	}
	// }}
	
	// {{
	@Hidden    
	public List<Cliente> autoComplete(final String cliente) {
		return allMatches(Cliente.class, new Filter<Cliente>() {
		@Override
		public boolean accept(final Cliente t) {		
		return t.getNumeroIdent().contains(cliente) && t.getActivo(); 
		}
	  });				
	}
	// }}
	// {{ Helpers
	protected boolean ownedByCurrentUser(final Cliente t) {
		return Objects.equal(t.getOwnedBy(), currentUserName());
	}
	protected String currentUserName() {
		return getContainer().getUser().getName();
	}
	// }}
}