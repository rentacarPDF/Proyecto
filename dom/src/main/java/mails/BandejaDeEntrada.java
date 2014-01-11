package mails;

import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.filter.Filter;
import com.google.common.base.Objects;

@Named("Correo Electronico")
public class BandejaDeEntrada extends AbstractFactoryAndRepository {

	/**
	 * 
	 *  Identificacion del nombre del icono que aparecera en la UI
	 *  @return String
	 */
	public String iconName() {
		return "email";
	}

	/**
	 * 
	 * @return Retorna la lista de correos persistidos
	 */
	@Named("Bandeja de Entrada")
	@MemberOrder(sequence = "1")
	public List<Correo> bde() {

		Recibe recepcion = new Recibe();
		recepcion.setProperties();
		recepcion.accion();

		final List<Correo> listaJavaMail = recepcion.getListaMensajes();

		String mensajeNuevos = listaJavaMail.size() == 1 ? "TIENES UN NUEVO CORREO!"
				: "TIENES " + listaJavaMail.size() + " CORREOS NUEVOS";

		if (listaJavaMail.size() > 0) {

			getContainer().informUser(mensajeNuevos);

			for (Correo mensaje : listaJavaMail) {

				final Correo mensajeTransient = newTransientInstance(Correo.class);
				if(existeMensaje(mensaje.getAsunto())==null){
					mensajeTransient.setEmail(mensaje.getEmail());
					mensajeTransient.setAsunto(mensaje.getAsunto());
					mensajeTransient.setMensaje(mensaje.getMensaje());
					mensajeTransient.setUsuario(usuarioActual());
					mensajeTransient.setFechaActual(mensaje.getFechaActual());
					persistIfNotAlready(mensajeTransient);
				}
			}

		}
		return listaMensajesPersistidos();
	}

	/**
	 * Retorna los emails guardados por el usuario registrado
	 * @return List<Correo>
	 */
	@Programmatic
	public List<Correo> listaMensajesPersistidos() {

		return allMatches(Correo.class, new Filter<Correo>() {
			@Override
			public boolean accept(final Correo mensaje) {
				return Objects.equal(mensaje.getUsuario(), usuarioActual());
			}
		});
	}
	/**
	 * Corrobora si ya esta persistido el correo en nuestra BD
	 * Busqueda.
	 * @param mail
	 * @return Correo
	 */
	@Programmatic
	public Correo existeMensaje(final String mail) {
		return uniqueMatch(Correo.class, new Filter<Correo>() {
			@Override
			public boolean accept(Correo correo) {
				return correo.getAsunto().equals(mail);
			}
		});
	}

	protected boolean creadoPorActualUsuario(final Correo m) {
		return Objects.equal(m.getUsuario(), usuarioActual());
	}

	protected String usuarioActual() {
		return getContainer().getUser().getName();
	}
}
