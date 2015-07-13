import org.metaworks.annotation.ServiceMethod;


public class Login {

	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	String message;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	String password;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		
	@ServiceMethod(callByContent=true)
	public Object login(){
		
		if(getName().equals(getPassword()))
		
			return new Main();
		
		else{

			setMessage("암호가 틀렸습니다.");

			return this;
//			throw new RuntimeException("암호가 틀렸");
		}
	}
		
	

}
