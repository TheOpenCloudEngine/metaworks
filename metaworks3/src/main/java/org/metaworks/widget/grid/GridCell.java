package org.metaworks.widget.grid;

public class GridCell {
	String rowId;
		public String getRowId() {
			return rowId;
		}
		public void setRowId(String rowId) {
			this.rowId = rowId;
		}	
	
	String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
	String value;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

	int row;
		public int getRow() {
			return row;
		}
		public void setRow(int row) {
			this.row = row;
		}

	int col;
		public int getCol() {
			return col;
		}
		public void setCol(int col) {
			this.col = col;
		}
}
