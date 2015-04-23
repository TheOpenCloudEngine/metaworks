package org.metaworks;

public class ToOpener {
	
	Object target;

		public Object getTarget() {
			return target;
		}
	
		public void setTarget(Object target) {
			this.target = target;
		}

		
	public ToOpener(Object target){
		setTarget(target); //TODO: need remove detail properties except the key value or clone a new key object containing the only key parts for network optimization
	}
}
