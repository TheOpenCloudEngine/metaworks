import org.metaworks.annotation.ServiceMethod;


public class Main {
	
	public Main(){}
	
	@ServiceMethod(needToConfirm=true)
	public Login logout(){
		return new Login();
	}

}
