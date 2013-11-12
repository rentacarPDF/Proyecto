package fixture;



import marca.Marca;
import marca.MarcaServicio;
import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.annotation.Named;


import cliente.Cliente;
import cliente.Cliente.TipoId;
import cliente.ClienteServicio;

import adicional.Adicional;
import adicional.AdicionalServicio;

import categoria.Categoria;
import categoria.Categoria.Caja;
import categoria.Categoria.Traccion;
import categoria.CategoriaServicio;
 
/**
* Enables fixtures to be installed from the application.
*/
@Named("Fixtures")
public class PreCargarServicioFixture extends AbstractService{
	private final String user;
		public PreCargarServicioFixture() {
	        this(null);
	    }
	    public PreCargarServicioFixture(final String ownedBy) {
	        this.user = ownedBy;
	    }
	    public void install() {
	    	final String ownedBy = this.user != null? this.user : getContainer().getUser().getName();
  	        installFor(ownedBy);	        
	        getContainer().flush();
	    }	
	    //{{installFor 
	    private void installFor(final String user) {
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
	    	createCategoriaForUser("A", 2, 2, Caja.AUTOMATICA, Traccion.CUATROx2, "20");
	    	createCategoriaForUser("B", 4, 4, Caja.MANUAL, Traccion.CUATROx2, "50");
	    	createCategoriaForUser("C", 6, 4, Caja.MANUAL, Traccion.CUATROx4, "100");
	    	getContainer().informUser("Se han instalado correctamente las Categorias");
	    	//Se instalan Adicionales
	    	createAdicionalForUser("Silla de Bebe","Silla de bebé para mayor seguridad en un viaje. Para menores de 2 años","30");
	    	createAdicionalForUser("Porta Sky","Porta Sky para transportar tus equipamiento para esquiar con mayor seguridad en un viaje.","70");
	    	createAdicionalForUser("Conductor Adicional","En el caso de que haya más de un conductor, debe abonarse un seguro adicional","80");
	    	createAdicionalForUser("Seguro Internacional","En el caso de que se quiera salir del país, debe abonar un seguro adicional con cobertura solo en paises limítrofes","120");
	    	getContainer().informUser("Se han instalado correctamente los Adicionales");
	    	getContainer().flush();
	    }
	    //}}
	    /////////////////////////////////////
	    // Helpers
	    ////////////////////////////////////////

	    //Metodo que instancia el Servicio de Marca y se crean los objetos.
	    private Marca createMarcaForUser(final String marca){
	    	return marcaServicio.cargarMarca(marca);
	    }
	    //Metodo que instancia el Servicio de Categoria y se crean los objetos.
	    private Categoria createCategoriaForUser(final String categoria,final int cantPuert,final int cantPlaz,final Caja caja,final Traccion traccion,final String precio){
	    	
	    	return categoriaServicio.cargarCategoria(categoria, cantPuert, cantPlaz, caja, traccion, precio);
	    }
	    //Metodo que instancia el Servicio de Adicional y se crean los objetos.
	    private Adicional createAdicionalForUser(final String nombre, final String descrip, final String precio){
	    	return adicionalServicio.cargar(nombre, descrip, precio);
	    }
	    //Metodo que instancia el Servicio de Cliente y se crean los objetos.
	    @SuppressWarnings("unused")
		private Cliente createClienteForUser(final String nombre, final String apellido, final TipoId tipo, final String numeroId, final int numeroTel, final String mail){
	    	return clienteServicio.cargarCliente(nombre, apellido, tipo, numeroId, numeroTel, mail);
	    }
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

}