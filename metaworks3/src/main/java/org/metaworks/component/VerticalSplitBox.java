package org.metaworks.component;

public class VerticalSplitBox {

	public final static String ALIGN_LEFT			= "left";
	public final static String ALIGN_RIGHT			= "right";
			
	int fixWidth;
		public int getFixWidth() {
			return fixWidth;
		}
		public void setFixWidth(int fixWidth) {
			this.fixWidth = fixWidth;
		}

	String align;
		public String getAlign() {
			return align;
		}
		public void setAlign(String align) {
			this.align = align;
		}
		
	Object left;
		public Object getLeft() {
			return left;
		}
		public void setLeft(Object left) {
			this.left = left;
		}

	Object right;
		public Object getRight() {
			return right;
		}
		public void setRight(Object right) {
			this.right = right;
		}
		
	Splitter splitter;
		public Splitter getSplitter() {
			return splitter;
		}
		public void setSplitter(Splitter splitter) {
			this.splitter = splitter;
		}
		
	public void useSplitter(boolean isUse){
		if(isUse){
			Splitter splitter = new Splitter();
			splitter.setType(Splitter.TYPE_VERTICAL);
			splitter.setAlign(this.getAlign());
			splitter.setSize(5);
			
			this.setSplitter(splitter);
		}else{
			this.setSplitter(null);
		}
	}
	
	public VerticalSplitBox(){
		this.setAlign(ALIGN_LEFT);
	}
}
