package org.metaworks;

public class ToNext {
	
	Object target;

		public Object getTarget() {
			return target;
		}
	
		public void setTarget(Object target) {
			this.target = target;
		}
		
	Object previous;
		public Object getPrevious() {
			return previous;
		}
		public void setPrevious(Object previous) {
			this.previous = previous;
		}

	public ToNext(Object previous, Object target){
		setPrevious(previous);
		setTarget(target); //TODO: need remove detail properties except the key value or clone a new key object containing the only key parts for network optimization
	}
}
