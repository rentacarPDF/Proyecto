package historiales;

import java.util.List;


import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;

import alquiler.Alquiler.EstadoAlquiler;
import autos.Auto;
import disponibles.AutoPorFecha;

@Named("Historial")
public class HistorialServicio extends AbstractFactoryAndRepository {
	//Historial de autos alquilados
	@MemberOrder(sequence = "1")
	 public List<AutoPorFecha> historialPorAuto(final Auto patente) 
	 { return allMatches(AutoPorFecha.class, new Filter<AutoPorFecha>(){ 
		 @Override 
		 public boolean accept(final AutoPorFecha auto) 
		 { 
			 return auto.getPatente().contains(patente.getPatente()) && auto.getAlquiler().getEstado()==EstadoAlquiler.FINALIZADO; //return auto.getPatente().equals(patente); } }); } 
		 }
		});
	 }
}
