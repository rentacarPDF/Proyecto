package historiales;

import java.util.List;


import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;

import categoria.Categoria;
import cliente.Cliente;

import alquiler.Alquiler.EstadoAlquiler;
import autos.Auto;
import disponibles.AutoPorFecha;

@Named("Historial")
public class HistorialServicio extends AbstractFactoryAndRepository {
	public String iconName(){
		return "historial";
	}
	//Historial de autos alquilados
	//{{
	@MemberOrder(sequence = "1")
	 public List<AutoPorFecha> historialPorAuto(final Auto patente) 
	 { return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>(){ 
		 @Override 
		 public boolean accept(final AutoPorFecha auto) 
		 { 
			 return auto.getPatente().contains(patente.getPatente()) && auto.getAlquiler().getEstado()==EstadoAlquiler.CERRADO; 
		 }
		});
	 }
	//}}
	//{{Historial de alquileres por cliente
	public List<AutoPorFecha> historialPorCliente(final Cliente cliente) 
	 { return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>(){ 
		 @Override 
		 public boolean accept(final AutoPorFecha auto) 
		 {  return auto.getAlquiler().getClienteId().equals(cliente) && auto.getAlquiler().getEstado()==EstadoAlquiler.CERRADO;  
		 }
		});
	 }
	//}}
	public List<AutoPorFecha> historialPorCategoria(final Categoria categoria) 
	 { return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>(){ 
		 @Override 
		 public boolean accept(final AutoPorFecha auto) 
		 {  return auto.getCategoria().equals(categoria) && auto.getAlquiler().getEstado()==EstadoAlquiler.CERRADO;  
		 }
		});
	 }
}
