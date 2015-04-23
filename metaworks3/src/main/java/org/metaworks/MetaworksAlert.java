package org.metaworks;

public class MetaworksAlert {
	
	String message;
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
	public MetaworksAlert(String message){
		this.message = message;
	}
}
