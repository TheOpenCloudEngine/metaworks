package org.metaworks;

public class ToPrev {
	
	Object target;
		public Object getTarget() {
			return target;
		}
	
		public void setTarget(Object target) {
			this.target = target;
		}
		
	Object next;
		public Object getNext() {
			return next;
		}	
		public void setNext(Object next) {
			this.next = next;
		}

	boolean match;
		public boolean isMatch() {
			return match;
		}
	
		public void setMatch(boolean match) {
			this.match = match;
		}

	public ToPrev(Object next, Object target){
		this(next, target, false);
	}

	public ToPrev(Object next, Object target, boolean match){
		setNext(next);
		setTarget(target); //TODO: need remove detail properties except the key value or clone a new key object containing the only key parts for network optimization
	}
}
