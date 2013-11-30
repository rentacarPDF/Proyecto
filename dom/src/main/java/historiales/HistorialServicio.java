package historiales;

import java.util.Collections;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;
import categoria.Categoria;
import cliente.Cliente;
import alquiler.Alquiler.EstadoAlquiler;
import autos.Auto;
import disponibles.AutoPorFecha;

/**
 * Clase que representa un Servicio de Historiales de Alquileres con distintos filtros.
 *
 */
@Named("Historial")
public class HistorialServicio extends AbstractFactoryAndRepository {
	/**
	 * Identificacion del nombre del icono que aparecera en la UI
	 * @return String
	 */
	public String iconName(){
		return "historial";
	}
	/**
	 * Se retorna un historial de alquileres de un vehiculo en especifico.
	 * 
	 * @param patente
	 * 
	 * @return List<AutoPorFecha>
	 */
	@Hidden
	 public List<AutoPorFecha> historialPorAutoMetodo(final Auto patente) 
	 { 
		return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>(){ 
		 @Override 
		 public boolean accept(final AutoPorFecha auto) 
		 { return auto.getPatente().contains(patente.getPatente()) && auto.getAlquiler().getEstado()==EstadoAlquiler.CERRADO; 
		 }
		});
	 }
	/**
	 * 
	 * Se retorna un historial de vehiculos alquilados por un Cliente en especifico.
	 * @param cliente
	 * @return List<AutoPorFecha>
	 */
	@Hidden
	public List<AutoPorFecha> historialPorClienteMetodo(final Cliente cliente) 
	 { return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>(){ 
		 @Override 
		 public boolean accept(final AutoPorFecha auto) 
		 {  return auto.getAlquiler().getClienteId().equals(cliente) && auto.getAlquiler().getEstado()==EstadoAlquiler.CERRADO;  
		 }
		});
	 }
	/**
	 * Se retorna un historial de vehiculos alquilados por una Categoria en especifica.
	 * @param categoria
	 * @return List<AutoPorFecha>
	 */
	@Hidden
	public List<AutoPorFecha> historialPorCategoriaMetodo(final Categoria categoria) 
	 { return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>(){ 
		 @Override 
		 public boolean accept(final AutoPorFecha auto) 
		 {  return auto.getCategoria().equals(categoria) && auto.getAlquiler().getEstado()==EstadoAlquiler.CERRADO;  
		 }
		});
	 }
	/**
	 * Se retorna un historial de vehiculos alquilados por una Categoria en especifica ordenada por fecha ascendetemente.
	 * @param categoria
	 * @return List<AutoPorFecha>
	 */
	@SuppressWarnings("unchecked")
	public List<AutoPorFecha> historialPorCategoria(final Categoria categoria) 
	 { List<AutoPorFecha> items = historialPorCategoriaMetodo(categoria);
	     Collections.sort(items,new AutoPorFecha());
	     return items;
	  
	 }
	/**
	 * Se retorna un historial de alquileres de un vehiculo en especifico ordenado por fecha ascendentemente.
	 * 
	 * @param patente
	 * 
	 * @return List<AutoPorFecha>
	 */
    
	@SuppressWarnings("unchecked")
	public List<AutoPorFecha> historialPorAuto(final Auto patente ) 
	 { List<AutoPorFecha> items = historialPorAutoMetodo(patente);
	     Collections.sort(items,new AutoPorFecha());
	     return items;
	  
	 }
	/**
	 * 
	 * Se retorna un historial de vehiculos alquilados por un Cliente en especifico ordenado por fecha ascendentemente.
	 * @param cliente
	 * @return List<AutoPorFecha>
	 */
	@SuppressWarnings("unchecked")
	public List<AutoPorFecha> historialPorCliente(final Cliente cliente ) 
	 { List<AutoPorFecha> items = historialPorClienteMetodo(cliente);
	     Collections.sort(items,new AutoPorFecha());
	     return items;
	  
	 }
}
