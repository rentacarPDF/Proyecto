package mails;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Immutable;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ObjectType;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
@ObjectType("CORREOEMPRESA")
@AutoComplete(repository = CorreoServicio.class, action = "autoComplete")
@Immutable
public class CorreoEmpresa {

	public String title(){
			return getCorreo();
	}
	
	public String iconName(){
		return "mail";
	}
	private String correo;
	@MemberOrder(sequence="1")
	public String getCorreo(){
		return correo;
	}
	public void setCorreo(String correo){
		this.correo=correo;
	}
	
	private String pass;
	@MemberOrder(sequence="2")
	public String getPass(){
		return pass;
	}
	public void setPass(String pass){
		this.pass=pass;
	}
	
}
