package fixture;

import java.sql.Date;

import marca.Marca;
import marca.MarcaServicio;
import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.objectstore.jdo.applib.service.support.IsisJdoSupport;

import cliente.Cliente;
import cliente.Cliente.TipoId;
import cliente.ClienteServicio;

import adicional.Adicional;
import adicional.AdicionalServicio;
import autos.Auto;
import autos.Auto.Seguro;
import autos.Auto.TipoCombustible;
import autos.AutoServicio;

import categoria.Categoria;
import categoria.Categoria.Caja;
import categoria.Categoria.Traccion;
import categoria.CategoriaServicio;
 
/**
* Enables fixtures to be installed from the application.
*/
@Named("Fixtures")
public class PreCargarServicioFixture extends AbstractService
{

		private final String user;

	    public PreCargarServicioFixture() {
	        this(null);
	    }
	    
	    public PreCargarServicioFixture(String ownedBy) {
	        this.user = ownedBy;
	    }
	
	    public void install() {

	        final String ownedBy = this.user != null? this.user : getContainer().getUser().getName();
  	       //isisJdoSupport.executeUpdate("delete from \"ToDoItem\" where \"ownedBy\" = '" + ownedBy + "'");
	        installFor(ownedBy);	        
	        getContainer().flush();
	    }	    

	    private void installFor(String user) {
	    	
	    	//Se instalan Marcas
	    	createMarcaForUser("FORD");
	    	createMarcaForUser("FIAT");
	    	createMarcaForUser("VOLKSWAGEN");
	    	createMarcaForUser("PEUGEOT");
	    	createMarcaForUser("NISSAN");
	    	createMarcaForUser("RENAULT");
	    	createMarcaForUser("TOYOTA");
	    	createMarcaForUser("CITROEN");
	    	
	    	getContainer().informUser("Se han instalado correctamente las Marcas");
	    	
	    	//Se instalan Categorias
	    	createCategoriaForUser("A", 2, 2, Caja.AUTOMATICA, Traccion.Cuatrox2, 20);
	    	createCategoriaForUser("B", 4, 4, Caja.MANUAL, Traccion.Cuatrox2, 50);
	    	createCategoriaForUser("C", 6, 4, Caja.MANUAL, Traccion.Cuatrox4, 100);
	    	
	    	getContainer().informUser("Se han instalado correctamente las Categorias");
	    	
	    	//Se instalan Adicionales
	    	createAdicionalForUser("Silla de Bebe","Silla de bebé para mayor seguridad en un viaje. Para menores de 2 años",30);
	    	createAdicionalForUser("Porta Sky","Porta Sky para transportar tus equipamiento para esquiar con mayor seguridad en un viaje.",70);
	    	createAdicionalForUser("Conductor Adicional","En el caso de que haya más de un conductor, debe abonarse un seguro adicional",80);
	    	createAdicionalForUser("Seguro Internacional","En el caso de que se quiera salir del país, debe abonar un seguro adicional con cobertura solo en paises limítrofes",120);
	    	
	    	getContainer().informUser("Se han instalado correctamente los Adicionales");
	    	
	    	//Se instalan Clientes
	    	//createClienteForUser("Juan","Perez",TipoId.CUIL,"27-3456788-1",4456789,"jperez@hotmail.com");
	    	//createClienteForUser("Maria","Del Barrio",TipoId.CUIL,"27-11234567-2",4934567,"mariadb@gmail.com");
	    	
	    	//getContainer().informUser("Se han instalado Clientes correctamente");
	    	
	    	//Se instalan Autos
	    	
	    	/* 
	    	 * createAutoForUser("AAA",
	    	 */
	    	
	        getContainer().flush();
	    }

	    /////////////////////////////////////
	    // Helpers
	    ////////////////////////////////////////

	    //Metodo que instancia el Servicio de Marca y se crean los objetos.
	    private Marca createMarcaForUser(final String marca){
	    	return marcaServicio.CargarMarca(marca);
	    }
	    //Metodo que instancia el Servicio de Categoria y se crean los objetos.
	    private Categoria createCategoriaForUser(final String categoria, int cantPuert, int cantPlaz, Caja caja, Traccion traccion, int precio){
	    	
	    	return categoriaServicio.CargarCategoria(categoria, cantPuert, cantPlaz, caja, traccion, precio);
	    }
	    //Metodo que instancia el Servicio de Adicional y se crean los objetos.
	    private Adicional createAdicionalForUser(String nombre, String descrip, float precio){
	    	return adicionalServicio.cargar(nombre, descrip, precio);
	    }
	    //Metodo que instancia el Servicio de Cliente y se crean los objetos.
	    private Cliente createClienteForUser(String nombre, String apellido, TipoId tipo, String numeroId, int numeroTel, String mail){
	    	return clienteServicio.CargarCliente(nombre, apellido, tipo, numeroId, numeroTel, mail);
	    }
	  //Metodo que instancia el Servicio del Auto y se crean los objetos.
	  
	    /* private Auto createAutoForUser(String patente, Marca marca, String modelo, int ano, Categoria categ, String color, int kms, int baul, TipoCombustible combustible, Date fechaCompra, Seguro seguro){
	    	return autoServicio.CargarAuto(patente, marca, modelo, ano, categ, color, kms, baul, combustible, fechaCompra, seguro);
	    }
	    */
	    // /////////////////////////////////////
	    // Servicios inyectados
	    ////////////////////////////////////////
	    
	    //Injected: MarcaServicio
	    private MarcaServicio marcaServicio=new MarcaServicio();
	    public void setMarcaServicio(final MarcaServicio marcaServicio) {
	        this.marcaServicio = marcaServicio;
	    }
	    
	    //Injected: CategoriaServicio
	    private CategoriaServicio categoriaServicio=new CategoriaServicio();
	    public void setCategoriaServicio(final CategoriaServicio categoriaServicio){
	    	this.categoriaServicio=categoriaServicio;
	    }
	    
	    //Injected: AdicionalServicio
	    private AdicionalServicio adicionalServicio=new AdicionalServicio();
	    public void setAdicionalServicio(final AdicionalServicio adicionalServicio){
	    	this.adicionalServicio=adicionalServicio;
	    }
	    
	  //Injected: ClienteServicio
	    private ClienteServicio clienteServicio=new ClienteServicio();
	    public void setAdicionalServicio(final ClienteServicio clienteServicio){
	    	this.clienteServicio=clienteServicio;
	    }
	    
	   /*
	  //Injected: AutoServicio
	    private AutoServicio autoServicio=new AutoServicio();
	    public void setAdicionalServicio(final AutoServicio autoServicio){
	    	this.autoServicio=autoServicio;
	    }
	    */


}