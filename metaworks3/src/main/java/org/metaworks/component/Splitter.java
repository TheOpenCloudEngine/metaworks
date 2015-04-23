package org.metaworks.component;

public class Splitter {

	public final static String TYPE_HORIZONTAL			= "horizontal";
	public final static String TYPE_VERTICAL			= "vertical";
	
	public final static String ALIGN_LEFT				= "left";
	public final static String ALIGN_RIGHT				= "right";
	public final static String ALIGN_TOP				= "top";
	public final static String ALIGN_BOTTOM				= "bottom";
	
	String type;
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		
	String align;
		public String getAlign() {
			return align;
		}
		public void setAlign(String align) {
			this.align = align;
		}

	int size;
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		
	int min;
		public int getMin() {
			return min;
		}
		public void setMin(int min) {
			this.min = min;
		}

	int max;
		public int getMax() {
			return max;
		}
		public void setMax(int max) {
			this.max = max;
		}

	public Splitter() {
		this.setType(TYPE_HORIZONTAL);
		this.setAlign(ALIGN_LEFT);
		
		this.setSize(5);		
	}
	
}
