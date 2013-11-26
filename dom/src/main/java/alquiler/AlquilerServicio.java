package alquiler;

import java.util.List;

import mails.Mail;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.filter.Filter;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import com.google.common.base.Objects;

import alquiler.Alquiler.EstadoAlquiler;
import cliente.Cliente;
import disponibles.AutoPorFecha;
import disponibles.Disponible;

@Named("Alquileres")
public class AlquilerServicio extends AbstractFactoryAndRepository{

	public String iconName(){
		return "alquiler";
	}
    @Named("Alquilar")
    @MemberOrder(sequence="1")
    public Alquiler reservar(
            @Named("Cliente") Cliente cliente ) {
    		final String ownedBy = currentUserName();
            Alquiler alquiler = newTransientInstance(Alquiler.class);
            Mail mail=new Mail();
            persistIfNotAlready(alquiler);
            List<Disponible> disponibilidad = listaAutosReservados();
            return crear(alquiler,disponibilidad,cliente,ownedBy,mail);
    }
    @Hidden
    private Alquiler crear(
    		final Alquiler alquiler,
    		final List<Disponible> disponibilidad,
    		final Cliente cliente,	
    		final String userName,
    		final Mail mail
    		){
    		if(disponibilidad.size()>0){
    			alquiler.setClienteId(cliente);
    			alquiler.setFecha(LocalDate.now().toDate());
    			alquiler.setNombreCliente(cliente.getNombre());
    			alquiler.setApellidoCliente(cliente.getApellido());
    			alquiler.setEstado(EstadoAlquiler.RESERVADO);
    			alquiler.setOwnedBy(userName);
    			for (Disponible disp:disponibilidad){
    				if (disp.isEstaSeleccionada()){
    					AutoPorFecha autoF=newTransientInstance(AutoPorFecha.class);
    					autoF.setFecha(disp.getFecha());
    					autoF.setCategoria(disp.getCategoria());
    					autoF.setPatente(disp.getPatente());
    					autoF.setAlquiler(alquiler);
    					autoF.setModeloAuto(disp.getModeloAuto());
    					alquiler.addToAutos(autoF);    					
    					persistIfNotAlready(autoF);    					
    				}  
    				getContainer().removeIfNotAlready(disp);
    				
    			}
    		mail.enviaMails(alquiler.getApellidoCliente(),alquiler.getNombreCliente(),alquiler.getAutos().toString(),alquiler.getPrecioTotal(),cliente.getEmail());	
    		}
    	return alquiler;
	}
    // }}
    
	// {{ Listado de Clientes Activos
	@ActionSemantics(Of.SAFE)
	public List<Cliente> choices0Reservar() {
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
    // {{ Listado de autos reservados
	@Hidden
    public List<Disponible> listaAutosReservados() {         
        return allMatches(QueryDefault.create(Disponible.class, "Disponibles"));
    } 
    // }}
    // {{
    @Named("Listado Alquileres")
    @MemberOrder(sequence="2")
    public List<Alquiler> listaAlquileres() {
            return allMatches(QueryDefault.create(Alquiler.class, "traerAlquileres"));
    }
    // }}
		
	// {{ Helpers
	protected boolean ownedByCurrentUser(final Alquiler t) {
	    return Objects.equal(t.getOwnedBy(), currentUserName());
	}
	protected String currentUserName() {
	    return getContainer().getUser().getName();
	}
	// }}	
}
