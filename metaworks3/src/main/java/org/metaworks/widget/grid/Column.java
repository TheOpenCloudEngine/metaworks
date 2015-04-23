package org.metaworks.widget.grid;

public class Column{

	String name;
		
		public String getName() {
			return name;
		}
	
		public void setName(String name) {
			this.name = name;
		}
		
	String index;
	
		public String getIndex() {
			return index;
		}
	
		public void setIndex(String index) {
			this.index = index;
		}

	int width;
	
		public int getWidth() {
			return width;
		}
	
		public void setWidth(int width) {
			this.width = width;
		}

	String sorttype;
	
		public String getSorttype() {
			return sorttype;
		}
	
		public void setSorttype(String sorttype) {
			this.sorttype = sorttype;
		}

	boolean editable;
	
		public boolean isEditable() {
			return editable;
		}
	
		public void setEditable(boolean editable) {
			this.editable = editable;
		}

	boolean sortable;
	
		public boolean isSortable() {
			return sortable;
		}
	
		public void setSortable(boolean sortable) {
			this.sortable = sortable;
		}
}