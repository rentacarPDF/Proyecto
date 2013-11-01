package alquiler;

import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import com.google.common.base.Objects;

import alquiler.Alquiler.EstadoAlquiler;
import cliente.Cliente;
import disponibles.AutoPorFecha;
import disponibles.Disponible;

@Named("Alquileres")
public class AlquilerServicio extends AbstractFactoryAndRepository{

    @Named("Alquilar")
    @MemberOrder(sequence="1")
    public Alquiler reservar(
                    @Named("Cliente") Cliente cliente
                    ) {
            
            Alquiler alquiler = newTransientInstance(Alquiler.class);
            persistIfNotAlready(alquiler);
            
            List<Disponible> disponibilidad = listaAutosReservados();
            
            return crear(alquiler,disponibilidad,cliente);
    }
    
    private Alquiler crear(
    		final Alquiler alquiler,
    		final List<Disponible> disponibilidad,
    		final Cliente cliente		
    		){
    		if(disponibilidad.size()>0){
    			alquiler.setClienteId(cliente);
    			alquiler.setFecha(LocalDate.now().toDate());
    			alquiler.setNombreCliente(cliente.getNombre());
    			alquiler.setApellidoCliente(cliente.getApellido());
    			alquiler.setEstado(EstadoAlquiler.RESERVADO);
    			
    			
    			for (Disponible disp:disponibilidad){
    				if (disp.estaSeleccionada()){
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
    			
    			
    		}
    	return alquiler;
	}
    // }}
    
    // {{ 
    private List<Disponible> listaAutosReservados() {         
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

