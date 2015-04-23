package org.metaworks;


public class Remover {
	
	public static final Remover ModalWindow = new Remover(new org.metaworks.widget.ModalWindow()); 
//	public static final Remover Popup = new Remover(new org.metaworks.widget.Popup());
		
	Object target;
		public Object getTarget() {
			return target;
		}	
		public void setTarget(Object target) {
			this.target = target;
		}

	boolean match;
		public boolean isMatch() {
			return match;
		}
		public void setMatch(boolean match) {
			this.match = match;
		}
		
	
	public Remover(Object target){
		this(target, false);
	}
	
	public Remover(Object target, boolean match){
		setMatch(match);
		setTarget(target); //TODO: need remove detail properties except the key value or clone a new key object containing the only key parts for network optimization
	}

}
