package org.metaworks.inputter;

import java.awt.Component;

import org.metaworks.FieldDescriptor;
import org.metaworks.ui.*;
import org.metaworks.web.HTML;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.text.*;

public class DateInput extends InputterAdapter{

	public DateInput(){
		super();
	}
	
/////////// implemetations //////////////

	public Object getValue(){
		Date srcDate = ((DateButton)getComponent()).getDate();
		
		if(srcDate==null) return null;
		
	/*	Date rtnDate = new Date(srcDate.getTime()){
			public String toString(){
			    SimpleDateFormat displayFormatter = new SimpleDateFormat("yyyy-MM-dd");
				String making = displayFormatter.format(this);
						
				return making;
			}			
		};
		
		return rtnDate;*/
		
		return srcDate;

	}
	
	public void setValue(Object obj){
		if(obj instanceof Date && obj!=null)
			((DateButton)getComponent()).setDate((Date)obj);
	}

	public Component getNewComponent(){
		
		DateButton dateButton = new DateButton();
		
		//dateButton.setDisplayFormat("yyyy-MM-dd");
		
		return dateButton;
	}
	
	public String getViewerHTML(String section, FieldDescriptor fd, Object defaultValue, Map options) {
		
		if(defaultValue!=null){
			Calendar value;

			if(defaultValue instanceof Date){
				value = Calendar.getInstance();
				value.setTime((Date)defaultValue);
			}else value = (Calendar)defaultValue;
			
//			SimpleDateFormat displayFormatter = new SimpleDateFormat("dd-MM-yyyy");
//			String making = displayFormatter.format(value.getTime());
//					
			return value.getTime().toString();
			//return ""+value;
		}
		
		return "-";
	}

	public String getInputterHTML(String section, FieldDescriptor fd, Object defaultValue, Map options){

		int date = 0, month =0, year=0;	
		
		if(defaultValue == null) defaultValue = Calendar.getInstance();
		
		if(defaultValue!=null && defaultValue instanceof Calendar){
			Calendar cal = (Calendar)defaultValue;
			date = cal.get(Calendar.DATE);
			month = cal.get(Calendar.MONTH);
			year = cal.get(Calendar.YEAR);
		}

		StringBuffer html = new StringBuffer();
		
		//day
		html.append("<select");
		html.append(HTML.getAttrHTML("name", "_" + section + "_" + fd.getName() + "_day"));
		html.append(">");
		for(int i=0; i<30; i++){
			if(defaultValue!=null && i==date)
				html.append("<option value=" + i + " selected>" + (i+1));
			else
				html.append("<option value=" + i + ">" + (i+1));
		}
		html.append("</select> - ");

		//month
		html.append("<select");
		html.append(HTML.getAttrHTML("name", "_" + section + "_" + fd.getName() + "_month"));
		html.append(">");
		
		String [] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

		for(int i=0; i<11; i++){
			if(defaultValue!=null && i==month)
				html.append("<option value=" + i + " selected>" + monthNames[i]);
			else
				html.append("<option value=" + i + ">" + monthNames[i]);
		}
		html.append("</select> - ");
		
		//year
		html.append("<input");
		html.append(HTML.getAttrHTML("name", "_" + section + "_" + fd.getName() + "_year"));
		if(defaultValue!=null) html.append(" value=" + year);
		html.append(" size=5>");

		return html.toString();
	}
	
	public Object createValueFromHTTPRequest(Map parameterValues, String section, String fieldName, Object oldValue){
		try{
			String strDay = ((String[])parameterValues.get("_" + section + "_" + fieldName + "_day"))[0];
			String strMonth = ((String[])parameterValues.get("_" + section + "_" + fieldName + "_month"))[0];
			String strYear = ((String[])parameterValues.get("_" + section + "_" + fieldName + "_year"))[0];
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DATE, Integer.parseInt(strDay)+1);
			cal.set(Calendar.MONTH, Integer.parseInt(strMonth));
			cal.set(Calendar.YEAR, Integer.parseInt(strYear));
			
			return cal;
		}catch(Exception e){
			return null;
		}
	}

	
}
