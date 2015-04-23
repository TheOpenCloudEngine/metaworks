package org.metaworks.web;

import java.text.*;

public class HTML{
	
	public static String getAttrHTML(String attrName, Object val){
		if(val!=null)
			return " "+attrName+"='"+ val +"'";
		
		return "";
	}
	
	public static String getTableHeaderHTML(){
		return 
			"<table><table width=3000><td width=3000>&nbsp;</td></table>"+
			"<table border=0 cellspacing=0 cellpadding =1 width><td bgcolor=black><table border=0 cellpadding =5 cellspacing=1>";
	}

	public static String getTableFooterHTML(){
		return "</td></table></td></table>";
	}

	public static String getTableHeaderCellHTML(String text){
		return "<th bgcolor=blue><font color=white size=-1>"+ text +"</th>";
	}

	public static String getTableCellHTML(Object data){	
		boolean bInt = false;
		
		if(data instanceof Integer){
			try{
				data = NumberFormat.getInstance().format(((Integer)data).intValue());
			}catch(Exception e){}
			
			bInt =true;
		}
		
		if(data == null) data = getSpaceHTML();
		
			
		return "<td bgcolor=white"+ (bInt ? " align=right":"") +"><font size=-1>"+ data +"</td>";
	}

	public static String getTableLineHTML(){
		return "<tr>\n";
	}

	public static String getSpaceHTML(){
		return "&nbsp;";
	}
		
}
