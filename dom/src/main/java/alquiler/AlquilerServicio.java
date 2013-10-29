package alquiler;

import java.util.Date;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.filter.Filter;

import categoria.Categoria;
import cliente.Cliente;
import com.google.common.base.Objects;
import alquiler.Alquiler;
import alquiler.Alquiler.TipoPago;
import autos.Auto;
import autos.Auto.Estado;


@Named("Alquiler")
public class AlquilerServicio extends AbstractFactoryAndRepository{
		
	// {{ Carga de Alquiler
	@MemberOrder(sequence = "1")
	public Alquiler CargarAlquiler(	
			@Named("Categoria") Categoria categoria,
			@Named("Auto") Auto auto, 
			@Named("Numero Cuil/Cuit") Cliente cliente,
			@Named("Tipo de Pago") TipoPago tipoPago,
			@Named("Numero de Recibo") int numeroRecibo,
			@Named("Fecha Alquiler") Date fechaAlq,
			@Named("Fecha Devolucion") Date fechaDev) {
		final boolean activo = true;
		final String ownedBy = currentUserName();
		return elAlq(categoria, auto, cliente, tipoPago, numeroRecibo, fechaAlq, fechaDev, activo, ownedBy);

	}
	// }}
	
	// {{
	@Hidden
	// for use by fixtures
	public Alquiler elAlq(final Categoria categoria,
			final Auto auto,
			final Cliente cliente,			
			final TipoPago tipoPago,
			final int numRecibo, 
			final Date fechaAlq,
			final Date fechaDev,
			final boolean activo,
			final String userName) {	
		
			Alquiler alquiler = newTransientInstance(Alquiler.class);		
			
			if (fechaDev.getTime()>fechaAlq.getTime()){		
				
				float dias=calculoDias(fechaAlq, fechaDev);
				float costo=auto.getCategoria().getPrecio();
				float precio=dias*costo;							
				
				alquiler.setCategoria(categoria);
				alquiler.setAuto(auto);
				alquiler.setClienteId(cliente);
				alquiler.setTipoPago(tipoPago);
				alquiler.setPrecioAlquiler(precio);
				alquiler.setNumeroRecibo(numRecibo);
				alquiler.setFechaAlquiler(fechaAlq);
				alquiler.setFechaDevolucion(fechaDev);
				alquiler.setOwnedBy(userName);
				alquiler.setActivo(true);
				auto.setEstado(Estado.ALQUILADO);
				
				persistIfNotAlready(alquiler);					
			}
			else{
				alquiler=null;
				getContainer().warnUser("La fecha de Devolucion tiene que ser mayor a la de Alquiler");
			}
		
		return alquiler;
	}
	// }}	
	public List<Categoria> choices0CargarAlquiler(){
		List<Categoria> items = listaCategorias();
		
		return items;
	}
    protected List<Categoria> listaCategorias() {
        return allMatches(Categoria.class, new Filter<Categoria>() {
            @Override
            public boolean accept(final Categoria t) {            	          	
                return t.getActivo();            	          	
            }
        });
    }
    /*
    public List<Auto> choices1CargarAlquiler(final Categoria categoria){
    	//return autos.busquedaAutosPorCategoria(categoria);
    	List<Auto> items=busquedaAutosPorCategoria(categoria);
    	return items;
    }    
    private AutoServicio autos;
    public void injectAutos(final AutoServicio auto){
    	this.autos=auto;
    }
    
    protected List<Auto> busquedaAutosPorCategoria(@Named("Categoria")final Categoria description) {    	
        return allMatches(Auto.class, new Filter<Auto>() {
            @Override
            public boolean accept(final Auto t) {
                return  t.getCategoria().equals(description) && t.getActivo() && t.getEstado().equals(Estado.LIBRE);  
            }
        });
    }
	*/
	public List<Auto> choices1CargarAlquiler(){
		List<Auto> items = listaAutosLibres();
		
		return items;
	}
    protected List<Auto> listaAutosLibres() {
        return allMatches(Auto.class, new Filter<Auto>() {
            @Override
            public boolean accept(final Auto t) {            	          	
            	return t.getActivo() ;
                //return t.getActivo() && t.getEstado().equals(Estado.LIBRE);            	          	
            }
        });
    }
    // }}
       
    // {{ Calculo de diferencia de dias entre fechas.
	protected int calculoDias(final Date a1, final Date a2){
		long inicio=a1.getTime();
		long fin =a2.getTime();
		long diferencia = fin-inicio;
		long resultado = diferencia/(24*60*60*1000);
		
		return (int) resultado;
	}
	// }}
	
	// {{ Listado de Alquileres Activos
	@ActionSemantics(Of.SAFE)
	@MemberOrder(sequence = "2")
	public List<Alquiler> listadoAlquileresActivos() {
		List<Alquiler> items = listaAlquileresActivos();
		if (items.isEmpty()) {
			getContainer().informUser("No hay alquileres activos ");
		}
		return items;
	}

	protected List<Alquiler> listaAlquileresActivos() {
		return allMatches(Alquiler.class, new Filter<Alquiler>() {
			@Override
			public boolean accept(final Alquiler t) {
				return t.getActivo();
			}
		});
	}
	// }}	
	
	// {{ Listado de Alquileres entre Fechas
	@MemberOrder(sequence = "3")
	public List<Alquiler> listadoAlquileresEntreFechas(@Named("Fecha Inicio") final Date fecha1, @Named("Fecha Fin") final Date fecha2 ) {
		return allMatches(Alquiler.class, new Filter<Alquiler>() {
		@Override
		public boolean accept(final Alquiler t) {		
			if (fecha1.getTime()<=t.getFechaAlquiler().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()<=t.getFechaDevolucion().getTime()){
				getContainer().informUser("Fuera del rango pero existen alquileres");
				return t.getAuto().getActivo(); 
			}
			if (fecha1.getTime()<=t.getFechaAlquiler().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()>=t.getFechaDevolucion().getTime()){
				getContainer().informUser("Fuera del rango pero existen alquileres");
				return t.getAuto().getActivo(); 
			}
			if (fecha1.getTime()<=t.getFechaAlquiler().getTime() && fecha2.getTime()<t.getFechaAlquiler().getTime()){
				getContainer().informUser("Fuera del rango");
				return false; 
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha1.getTime()<t.getFechaDevolucion().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()<=t.getFechaDevolucion().getTime()){
				getContainer().informUser("Fuera del rango pero existen alquileres");
				return t.getAuto().getActivo(); 
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha1.getTime()<t.getFechaDevolucion().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()>=t.getFechaDevolucion().getTime()){
				getContainer().informUser("Fuera del rango pero existen alquileres");
				return t.getAuto().getActivo(); 
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()<t.getFechaAlquiler().getTime() ){
				getContainer().informUser("Fuera del rango");
				return false; 
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha1.getTime()>t.getFechaDevolucion().getTime() ){
				getContainer().informUser("Fuera del rango");
				return false; 
			}
			if (fecha1.getTime()<t.getFechaAlquiler().getTime() && fecha2.getTime()>=t.getFechaAlquiler().getTime()){
				getContainer().informUser("La seleccion termina cuando empieza un alquiler");
				return t.getAuto().getActivo(); 
			}
			if (fecha1.getTime()<=t.getFechaDevolucion().getTime() && fecha2.getTime()>t.getFechaDevolucion().getTime()){
				getContainer().informUser("La seleccion empieza cuando termina un alquiler");
				return t.getAuto().getActivo(); 
			}
			else return false;
		}
	  });				
	}
	// }}
	
	// {{ Listado de Alquileres filtrado por Auto
	@MemberOrder(sequence = "4")    
	public List<Alquiler> listadoAlquileresPorAuto(final Auto lista,@Named("Fecha Inicio")final Date fecha1,@Named("Fecha Fin") final Date fecha2) {
		return allMatches(Alquiler.class, new Filter<Alquiler>() {
		@Override
		public boolean accept(Alquiler t){
			if (fecha1.getTime()<=t.getFechaAlquiler().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()<=t.getFechaDevolucion().getTime()){
				getContainer().informUser("Fuera del rango pero existen alquileres");
				return lista.equals(t.getAuto())&& t.getActivo();
			}
			if (fecha1.getTime()<=t.getFechaAlquiler().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()>=t.getFechaDevolucion().getTime()){
				
				return lista.equals(t.getAuto())&& t.getActivo(); 
			}
			if (fecha1.getTime()<=t.getFechaAlquiler().getTime() && fecha2.getTime()<t.getFechaAlquiler().getTime()){
				getContainer().informUser("Fuera del rango");
				return false; 
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha1.getTime()<t.getFechaDevolucion().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()<=t.getFechaDevolucion().getTime()){
				getContainer().informUser("Fuera del rango pero existen alquileres");
				return lista.equals(t.getAuto())&& t.getActivo(); 
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha1.getTime()<t.getFechaDevolucion().getTime() && fecha2.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()>=t.getFechaDevolucion().getTime()){
				getContainer().informUser("Fuera del rango pero existen alquileres");
				return lista.equals(t.getAuto())&& t.getActivo();
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha2.getTime()<t.getFechaAlquiler().getTime() ){
				getContainer().informUser("Fuera del rango");
				return false; 
			}
			if (fecha1.getTime()>t.getFechaAlquiler().getTime() && fecha1.getTime()>t.getFechaDevolucion().getTime() ){
				getContainer().informUser("Fuera del rango");
				return false; 
			}
			if (fecha1.getTime()<t.getFechaAlquiler().getTime() && fecha2.getTime()>=t.getFechaAlquiler().getTime()){
				getContainer().informUser("La seleccion termina cuando empieza un alquiler");
				return lista.equals(t.getAuto())&& t.getActivo();
			}
			if (fecha1.getTime()<=t.getFechaDevolucion().getTime() && fecha2.getTime()>t.getFechaDevolucion().getTime()){
				getContainer().informUser("La seleccion empieza cuando termina un alquiler");
				return lista.equals(t.getAuto())&& t.getActivo();
			}
			else return false;
			
		}
	  });
	}		
	public List<Auto> choices0ListadoAlquileresPorAuto(){
		List<Auto> items = listaAutosActivos();
		
		return items;
	}
    protected List<Auto> listaAutosActivos() {
        return allMatches(Auto.class, new Filter<Auto>() {
            @Override
            public boolean accept(final Auto t) {            	          	
                return t.getActivo();            	          	
            }
        });
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

