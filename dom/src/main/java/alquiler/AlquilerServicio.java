package alquiler;


import java.util.List;
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
import disponibles.DisponibleServicio;


@Named("Alquileres")
public class AlquilerServicio extends AbstractFactoryAndRepository{
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "alquiler";
	}
	/**
	 * Metodo que permite comenzar el proceso de Alquiler, 
	 * partiendo de la definicion del Cliente
	 * @see disponibles.Disponible
	 * @see AlquilerServicio#listaAutosReservados()
	 * 
	 * @param cliente
	 * @return Alquiler
	 */
    @Named("Alquilar")
    @MemberOrder(sequence="1")
    public Alquiler reservar(
            @Named("Cliente") Cliente cliente ) {
    		final String usuario = currentUserName();
            Alquiler alquiler = newTransientInstance(Alquiler.class);
            //Mail mail=new Mail();
            persistIfNotAlready(alquiler);
            List<Disponible> disponibilidad = listaAutosReservados();
            return crear(alquiler,disponibilidad,cliente,usuario);
    }
    /**
     * 
     * Metodo que setea todas las propiedades que tiene la entidad Alquiler
     * creado en el metodo {@link AlquilerServicio#reservar}.
     * 
     * @param alquiler
     * @param disponibilidad
     * @param cliente
     * @param userName
     * 
     * @return Alquiler
     */
    @Hidden
    private Alquiler crear(
    		final Alquiler alquiler,
    		final List<Disponible> disponibilidad,
    		final Cliente cliente,	
    		final String userName
    		//final Mail mail
    		){
    		if(disponibilidad.size()>0){
    			alquiler.setClienteId(cliente);
    			alquiler.setFecha(LocalDate.now().toDate());
    			alquiler.setNombreCliente(cliente.getNombre());
    			alquiler.setApellidoCliente(cliente.getApellido());
    			alquiler.setEstado(EstadoAlquiler.RESERVADO);
    			alquiler.setUsuario(userName);
    			
    			
    			for (Disponible disp:disponibilidad){
    				if (disp.isEstaSeleccionada()){
    					AutoPorFecha autoF=newTransientInstance(AutoPorFecha.class);
    					if ( servDisp.existeAlquiler(disp.getFecha(), disp.getPatente()) == null){ 
    					autoF.setFecha(disp.getFecha());
    					autoF.setCategoria(disp.getCategoria());
    					autoF.setPatente(disp.getPatente());
    					autoF.setAlquiler(alquiler);
    					autoF.setModeloAuto(disp.getModeloAuto());
    					
    					alquiler.addToAutos(autoF);    					
    					persistIfNotAlready(autoF);
    					}
    				}  
    				//getContainer().removeIfNotAlready(disp);
    				
    			}
    		//mail.enviaMails(alquiler.getApellidoCliente(),alquiler.getNombreCliente(),listaReservas, alquiler.getPrecioAlquiler(),cliente.getEmail());	
    		}
    	return alquiler;
	}

    @Hidden
    public Alquiler reservar2(Alquiler alquiler, Cliente cliente ) {    		
    		final String usuario = currentUserName();
            List<Disponible> disponibilidad = listaAutosReservados();
            return crear(alquiler,disponibilidad,cliente,usuario);
    }
    
    /**
     * Choices provisto por el Framework
     * que habilita una serie de opciones para un metodo.
     * Choices para el metodo {@link AlquilerServicio#reservar(Cliente)}
     * 
     * @return List<Cliente>
     */
	@ActionSemantics(Of.SAFE)
	public List<Cliente> choices0Reservar() {
		List<Cliente> items = listaClientes();
		if (items.isEmpty()) {
			getContainer().informUser("No hay clientes activos");
		}
		return items;
	}
	/**
	 * Metodo que retorna una lista de Clientes activos 
	 * 
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
	/**
	 * Metodo que retorna una lista de Autos Reservados.
	 * 
	 * @see disponibles.Disponible
	 * @return List<Disponible>
	 */
	@Hidden
    public List<Disponible> listaAutosReservados() {         
        return allMatches(QueryDefault.create(Disponible.class, "Disponibles"));
    } 

	/**
	 * Metodo que retorna una lista de Alquileres
	 * @return List<Alquiler>
	 */
    @Named("Listado Alquileres")
    @MemberOrder(sequence="2")
    public List<Alquiler> listaAlquileres() {
            return allMatches(QueryDefault.create(Alquiler.class, "traerAlquileres"));
    }
    @Named("Buscar Alquiler")
    @MemberOrder(sequence="3")
    @Hidden
    public Alquiler buscarAlquiler(final Alquiler alquiler) {
        return uniqueMatch(Alquiler.class,new Filter<Alquiler>() {
        	public boolean accept(final Alquiler a){
        		return a.equals(alquiler);
        	}
		});
    }
    
    public List<Alquiler> choices0BuscarAlquiler(){
    	return listaAlquileres();
    }
    /**
	 * Helpers
	 * 
	 * Retorna un boolean que determina 
	 * si el usuario que se le est&aacute; pasando por parametro es el mismo.
	 * 
	 * @param Alquiler
	 * @return boolean
	 * 
	 */
	protected boolean usuarioCurrentUser(final Alquiler t) {
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
	private DisponibleServicio servDisp;
	/**
	 * Se inyecta el servicio disponible.
	 * 
	 * @param serv
	 */
	public void injectDisponiblesServicio(final DisponibleServicio serv) {
		this.servDisp = serv;
	}
}
