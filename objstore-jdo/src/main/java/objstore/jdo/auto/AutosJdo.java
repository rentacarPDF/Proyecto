package objstore.jdo.auto;

import autos.AutoServicio;
import autos.Auto;


import org.apache.isis.applib.query.QueryDefault;
import java.util.List;


public class AutosJdo extends AutoServicio{
	@Override
    public List<Auto> listadoAutosActivos() {
        return allMatches(
                new QueryDefault<Auto>(Auto.class, 
                        "listado_autos"));
    }
	
	
	
	public List<Auto> buscoPatente(String patente){
			
		return allMatches(
                new QueryDefault<Auto>(Auto.class, 
                        "findAutos"));
	}
	
	
//	@Override
//    public boolean buscoPatente(String patente) {
//		List<Auto> lista= allMatches(
//                new QueryDefault<Auto>(Auto.class, 
//                        "findAutos",patente));
//		
//		return (lista.size() > 0) ? false : true;
//    }
	
	
	
}
