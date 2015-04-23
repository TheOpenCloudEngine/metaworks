package org.metaworks.web;

import org.metaworks.*;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

abstract public class MetaWorksServlet extends HttpServlet{

	Instance metaRecord;
	Instance parameterRecord;
	Hashtable contextHashtable = new Hashtable();

   ///////////////////// catridges ///////////////////////
   
	abstract public String getTitle();

	public String getPageHeader(){
		return "<html>";
	}
	public String getPageFooter(){
		return "</html>";
	}


   ///////////////////// doSQL base Services ///////////////////
   
	protected void service(HttpServletRequest req, HttpServletResponse resp)
                throws ServletException,
                       java.io.IOException
	{
		contextHashtable.put("servlet.request", req);
		contextHashtable.put("servlet.response", resp);
		
		createParameterRecord(req);
		printHeader(resp);
        	super.service(req, resp);
        	
        	resp.getWriter().println(getPageFooter());
	}

   	public void printHeader(HttpServletResponse response)
   		throws ServletException,
                       java.io.IOException
 	{
   	        response.setContentType("text/html;  charset=EUC-KR");

	        PrintWriter out = response.getWriter();
	        out.println("<html>");
        	out.println("<body>");
	        out.println("<head>");

        	out.println("<title>" + getTitle() + "</title>");
	        out.println("</head>");
        	out.println("<body bgcolor=\"white\">");
        
        	out.println(getTitle());
        	
        	out.println(getPageHeader());
	}

   public void createParameterRecord(HttpServletRequest request){
   
	parameterRecord = new Instance(new Type("Parameters"));
	metaRecord = new Instance(new Type("Meta Parameters"));
	Instance puttingRecord = null;
	
        Enumeration e = request.getParameterNames();

        while (e.hasMoreElements()) {
            String name = (String)e.nextElement();
            String value = request.getParameter(name);


            
            // if meta-variables
            if(name.startsWith("m_")){
            	puttingRecord = metaRecord;
            	name=name.substring(2, name.length());
            }
            else
            	puttingRecord = parameterRecord;

            int pos = -1;
            String prefix = null;
            if((pos = name.lastIndexOf("_"))> 0){//(puttingRecord == metaRecord ? 3:0)){
            	prefix = name.substring(0, pos);
            	name = name.substring(pos+1, name.length());
            }
            
//System.out.println("prefix="+prefix+" name is "+name +" pos is "+pos);       
            	
            if(prefix != null){
            	Object obj=null;
            	if((obj = puttingRecord.getFieldValueObject(prefix)) instanceof Instance)
            		puttingRecord = (Instance)obj;
            	else{
            		Type tableInstance = new Type(prefix);
//System.out.println("here?");            		
            		Instance childRec = new Instance(tableInstance);
//puttingRecord.printProperties();
            		
            		puttingRecord.getType().setFieldDescriptor(new FieldDescriptor(prefix));
            		
            		puttingRecord.put(prefix, childRec);
            		puttingRecord = childRec;
            	}
            }
            
            puttingRecord.getType().setFieldDescriptor(new FieldDescriptor(name));
            
            try{
            	value = new String(value.getBytes("8859_1"), "KSC5601");
            	
            	if(value.trim().length()==0) value=null;
System.out.println("converted val : "+value);
            }catch(Exception exc){}
            
            puttingRecord.put(name, value);
            
//            puttingRecord.printProperties();
//            metaRecord.printProperties();
            
        }
   }
   
   public Instance getParameterRecord(){
   	return parameterRecord;
   }
   public Instance getMetaRecord(){
   	return metaRecord;
   }
   public Hashtable getContextHashtable(){
   	return contextHashtable;
   }


 

} 
            
