package org.metaworks;

public class ToAppend {
	
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

	boolean match;
		public boolean isMatch() {
			return match;
		}
		public void setMatch(boolean match) {
			this.match = match;
		}

	boolean toFront;
		public boolean isToFront() {
			return toFront;
		}
		public void setToFront(boolean toFront) {
			this.toFront = toFront;
		}


	public ToAppend(Object parent, Object target){
		this(parent, target, false);
	}
	
	public ToAppend(Object parent, Object target, boolean match){
		setParent(parent);
		setTarget(target); //TODO: need remove detail properties except the key value or clone a new key object containing the only key parts for network optimization
		setMatch(match);
	}

}
