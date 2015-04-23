package org.metaworks;

public class ToPrepend {
	
	Object target;

		public Object getTarget() {
			return target;
		}
	
		public void setTarget(Object target) {
			this.target = target;
		}
		
	Object parent;
		public Object getParent() {
			return parent;
		}	
		public void setParent(Object parent) {
			this.parent = parent;
		}

	public ToPrepend(Object parent, Object target){
		setParent(parent);
		setTarget(target); //TODO: need remove detail properties except the key value or clone a new key object containing the only key parts for network optimization
	}
}
