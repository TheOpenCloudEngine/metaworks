package org.metaworks.widget.grid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;

public class Grid {
	public Grid(){
		cell = new GridCell();
	}
	
	String KeyColumn;
		public String getKeyColumn() {
			return KeyColumn;
		}
		public void setKeyColumn(String keyColumn) {
			KeyColumn = keyColumn;
		}


	String[] columnNames;
		public String[] getColumnNames() {
			return columnNames;
		}		
		public void setColumnNames(String[] columnNames) {
			this.columnNames = columnNames;
		}
		
	
	Map<String, Column> columnModel;
		public Map<String, Column> getColumnModel() {
			return columnModel;
		}	
		public void setColumnModel(Map<String, Column> columnModel) {
			this.columnModel = columnModel;
		}


	Object data;	
		public Object getData() {
			return data;
		}	
		public void setData(Object data) {
			this.data = data;
		}
	
	GridCell cell;
		public GridCell getCell() {
			return cell;
		}		
		public void setCell(GridCell cell) {
			this.cell = cell;
		}
	

	@ServiceMethod
	public void init(){
		// TODO select Data from DB
		
		setColumnNames(new String[]{"Inv No","Date", "Client", "Amount","Tax","Total","Notes"});
	//	setColumnModel("");
		
		ArrayList<Map<String, String>> arrListData = new ArrayList<Map<String, String>>();
		Map column = new HashMap();
		
		column.put("id", "1");
		column.put("invdate", "2007-10-02");
		column.put("name", "11111");
		column.put("note", "11111");
		column.put("tax", "11111");
		column.put("total", "11111");

		arrListData.add(column);
		column = new HashMap();
		column.put("id", "2");
		column.put("invdate", "2007-10-02");
		column.put("name", "22222");
		column.put("note", "22222");
		column.put("tax", "22222");
		column.put("total", "22222");
		arrListData.add(column);

		column = new HashMap();
		column.put("id", "3");
		column.put("invdate", "2007-10-02");
		column.put("name", "33333");
		column.put("note", "33333");
		column.put("tax", "33333");
		column.put("total", "33333");
		arrListData.add(column);

		column = new HashMap();
		column.put("id", "4");
		column.put("invdate", "2007-10-02");
		column.put("name", "4444");
		column.put("note", "44444");
		column.put("tax", "44444");
		column.put("total", "4444");
		arrListData.add(column);
		
		setData(arrListData);
	}
	
	@ServiceMethod(callByContent=true, target=ServiceMethodContext.TARGET_NONE)
	public void changeCell() {
		System.out.println("changeCell1()");
		//System.out.println("row : " + getCell().row + ", col : " + getCell().col + ", content : " + getCell().value);
		
		//ArrayList<Map<String, String>> arrListData = (ArrayList<Map<String, String>>) data;
		// TODO update DB and re select Data from DB and clear Cell Object 
		//getCell().setValue(arrListData.get(getCell().getRow()).put("name", getCell().getValue()));
		
	}
	
}
