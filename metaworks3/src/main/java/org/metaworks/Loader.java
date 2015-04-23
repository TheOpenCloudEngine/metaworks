package org.metaworks;

public class Loader {

	Object target;
		public Object getTarget() {
			return target;
		}
		public void setTarget(Object target) {
			this.target = target;
		}

	String caller;
		public String getCaller() {
			return caller;
		}
		public void setCaller(String caller) {
			this.caller = caller;
		}
		
	public Loader(){

	}
	
	public Loader(Object target, String caller){
		this.setTarget(target);
		this.setCaller(caller);
	}
	
}
