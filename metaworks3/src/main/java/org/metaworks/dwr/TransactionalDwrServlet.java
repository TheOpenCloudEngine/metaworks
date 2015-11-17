package org.metaworks.dwr;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.extend.Replies;
import org.directwebremoting.extend.Reply;
import org.directwebremoting.servlet.DwrServlet;
import org.metaworks.MetaworksException;
import org.metaworks.dao.ConnectionFactory;
import org.metaworks.dao.JDBCConnectionFactory;
import org.metaworks.dao.TransactionContext;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class TransactionalDwrServlet extends DwrServlet{


	public static final String PATH_METAWORKS = "metaworks";

	public static boolean useSpring = false;
	
	public static ConnectionFactory connectionFactory;
	
	protected XStream xstream = new XStream(/*new DomDriver()*/);


	
	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		// TODO Auto-generated method stub
		super.init(servletConfig);
		
		if("true".equals(servletConfig.getInitParameter("useSpring"))){
			useSpring = true;
		}

		String connectionString = getServletConfig().getInitParameter("connectionString");
		
		if(connectionString!=null){
			String driverClass = getServletConfig().getInitParameter("driverClass");
			String userId = getServletConfig().getInitParameter("userId");
			String password = getServletConfig().getInitParameter("password");
			
			JDBCConnectionFactory cf = new JDBCConnectionFactory();
			cf.setConnectionString(connectionString);
			cf.setDriverClass(driverClass);
			cf.setUserId(userId);
			cf.setPassword(password);
	
			connectionFactory = cf;
		}

	}




	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		//check if the request is simply requesting resource and handles it.
		
		String pathInfo = request.getPathInfo();
		String requestContextPath = request.getContextPath();

		boolean isAllow = true;
    	
		// forx
		/*
    	if(pathInfo.endsWith(".class") || 
    	   pathInfo.endsWith(".java") ||
    	   pathInfo.endsWith(".org") ||
    	   pathInfo.endsWith(".temp") ||
    	   pathInfo.endsWith(".tmp") ||
    	   pathInfo.endsWith(".sln") ||
    	   pathInfo.endsWith(".zip") ||
    	   pathInfo.endsWith(".egg") ||
    	   pathInfo.endsWith(".alz") ||
    	   pathInfo.endsWith(".gz") ||
    	   pathInfo.endsWith(".tar") ||
    	   pathInfo.endsWith(".dat") ||
    	   pathInfo.endsWith(".list") ||
    	   pathInfo.endsWith(".core") ||
    	   pathInfo.endsWith(".conf") ||
    	   pathInfo.endsWith(".inc") ||
    	   pathInfo.endsWith(".orgin") ||
    	   pathInfo.endsWith(".log") ||
    	   pathInfo.endsWith(".old") ||        	   
    	   pathInfo.endsWith(".bk") || 
    	   pathInfo.endsWith(".bak"))
    		isAllow = false;    	
    	
    	if(!isAllow){
    		response.setStatus(404);
    		
    		return;
    	}
    	*/
    	
    	response.setHeader("X-XSS-Protection", "0"); //X-XSS-Protection
    	
		int wherePrefixStarts = pathInfo.indexOf(PATH_METAWORKS);
        if (wherePrefixStarts != -1)
        {
        	// forx
        	/*
        	if(pathInfo.endsWith("/") || pathInfo.indexOf(".") == -1){
        		response.setStatus(404);
        		
        		return;        		
        	}
        	*/
        		
        	String mimeType = (pathInfo.endsWith(".js") ? "text/javascript":"text/plain");
        	
        	if(pathInfo.endsWith(".png"))
        		mimeType = "image/png";
        	
        	if(pathInfo.endsWith(".css"))
        		mimeType = "text/css";
        	
        	
        	response.setContentType(mimeType + "; charset=UTF-8");
        	response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
        	response.setHeader("Pragma", "no-cache"); //HTTP 1.0
        	response.setDateHeader("Expires", 0); //prevents caching at the proxy server

//            response.setContentType("text/text");
            //response.setDateHeader(HttpConstants.HEADER_LAST_MODIFIED, lastModified);
            //response.setHeader(HttpConstants.HEADER_ETAG, "\"" + lastModified + '\"');
        	
        	pathInfo = pathInfo.substring(wherePrefixStarts + PATH_METAWORKS.length() + 1);
        	
        	InputStream is = null;
        	
        	try{
        		is = Thread.currentThread().getContextClassLoader().getResourceAsStream(pathInfo);
        		
    	        if(is!=null){
	            	String appendix = null;
	            	
	            	if(pathInfo.endsWith("/metaworks.js")){
	            		appendix = ("mw3.setBase('" + request.getContextPath() + "');");
	            	}
	            	
					copyStream(is, response.getOutputStream(), appendix);
    	        }else{
					//response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);

				}
        	}catch(Exception e){
        		e.printStackTrace();
        	}finally{
        		if(is != null)
        			is.close();
        		
				response.flushBuffer();
			    response.getOutputStream().flush();
			    response.getOutputStream().close();		
        	}
        	
        	return;
        }

        
        
		//end check if
		
		
		//TODO: It's debugging option 
		//MetaworksRemoteService.metadataStorage = new Hashtable<String, WebObjectType>();
		
        TransactionContext tx = new TransactionContext(); //once a TransactionContext is created, it would be cached by ThreadLocal.set, so, we need to remove this after the request processing. 
        
		tx.setManagedTransaction(false);
		tx.setAutoCloseConnection(true);
		
		tx.setRequest(request);
		tx.setResponse(response);
		
//		String defaultDS = getServletConfig().getInitParameter("defaultDS");
//		
//		DataSourceConnectionFactory dscf = new DataSourceConnectionFactory();
//		dscf.setDataSourceJndiName(defaultDS);

		if(connectionFactory!=null)
			tx.setConnectionFactory(connectionFactory);
		

		try{
			
	        //Handles XStream-based RPC 
	        if(pathInfo.startsWith("/xstr-rpc")){
	        	
	        	String className = request.getParameter("className");
	        	String methodName = request.getParameter("methodName");

	        	String objectStr = request.getParameter("object");
	        	String autowiredFieldsStr = request.getParameter("autowiredFields");
	        	
	        	Object object=null;
	        	if(objectStr!=null)
	        		object = fromXML(new ByteArrayInputStream(objectStr.getBytes()));
	        	else
	        		object = Thread.currentThread().getContextClassLoader().loadClass(className).newInstance();
	        		
	        	Map<String, Object> autowiredFields = null;
	        	if(autowiredFieldsStr!=null)
	        		autowiredFields = (Map<String, Object>) fromXML(new ByteArrayInputStream(autowiredFieldsStr.getBytes()));
	        	
	        	MetaworksRemoteService metaworksRemoteService = MetaworksRemoteService.getInstance();
	        	Object rtnValue = metaworksRemoteService.callMetaworksService(className, object, methodName, autowiredFields);
	        	
	        	toXML(rtnValue, response.getOutputStream());
	        	
	        	response.flushBuffer();
	        	
	        }else	if(pathInfo.startsWith("/mw3-rpc")){
            	String className = request.getParameter("className");
            	String objectId = request.getParameter("id");
            	String methodName = request.getParameter("methodName");
        		Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
	        	Object object = c.newInstance();
	        	
	        	MetaworksRemoteService metaworksRemoteService = MetaworksRemoteService.getInstance();
	        	object = metaworksRemoteService.callMetaworksService(className, object, methodName, null);
	        		        	
	        	toXmlByXstream(object, response.getOutputStream());
	        	
	        	response.flushBuffer();
	        }else if(pathInfo.startsWith("/mw3-json-rpc")){
	        	String className = request.getParameter("className");
            	String objectId = request.getParameter("id");
            	String methodName = request.getParameter("methodName");
        		Class c = Thread.currentThread().getContextClassLoader().loadClass(className);
	        	Object object = c.newInstance();
	        	
	        	MetaworksRemoteService metaworksRemoteService = MetaworksRemoteService.getInstance();
	        	object = metaworksRemoteService.callMetaworksService(className, object, methodName, null);
	        	
	        	XStream xstream4JSON = new XStream(new JettisonMappedXmlDriver());
//	            System.out.println("JSON RESULT");
//	            System.out.println(xstream4JSON.toXML(object));
	        	xstream4JSON.toXML(object, response.getOutputStream());	
	        	// http://localhost:8080/uengine-web/dwr/mw3-json-rpc?className=org.uengine.codi.mw3.knowledge.WfNode&methodName=load&id=1
	            response.flushBuffer();
	        }else{

				
				//Handles DWR request
				super.doPost(request, response);
				
				Replies replies = (Replies) tx.getSharedContext("replies");
				
				if(replies!=null)
				for(Reply reply : replies){
					if(reply.getThrowable()!=null){
						throw reply.getThrowable();
					}
				}
	        }
			
	        try{
		        
	        	tx.commit();
	        }catch(Exception exAtCommit){
	        	
	        	//TODO: [before production] replacing the reply (by reply.setThrowable(exAtCommit)) to shout out the right error messages to the client.
	        	Replies replies = (Replies) tx.getSharedContext("replies");
//	        	replies.getReply(0).
	        	replies.addReply(new Reply(replies.getCalls().getCall(0).getCallId(), null, exAtCommit));
	        	
	        	throw exAtCommit;
	        }			
		}catch(Throwable e){
			try {
				tx.rollback();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if(!(e instanceof MetaworksException))				
				e.printStackTrace();
			
			//in DWR, we never stops the application
			//throw new ServletException(e);
			
		}finally{
			try {
				tx.releaseResources();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	protected Object fromXML(InputStream xml){
		return xstream.fromXML(xml);
	}
	
	protected void toXML(Object obj, OutputStream out){
		xstream.toXML(obj, out);
	}

	protected void toXmlByXstream(Object obj, OutputStream out) {
		xstream.autodetectAnnotations(true);
		xstream.toXML(obj, out);
	}
	
	static public void copyStream(InputStream sourceInputStream, OutputStream targetOutputStream, String appendix) throws Exception{
		int length = 1024;
		byte[] bytes = new byte[length]; 
		int c; 
		int total_bytes=0;
			
		while ((c = sourceInputStream.read(bytes)) != -1) { 
				total_bytes +=c; 
				targetOutputStream.write(bytes,0,c); 
		} 
		
		if(appendix!=null){
			targetOutputStream.write(("\n" + appendix).getBytes());
		}
		
		if (sourceInputStream != null) try { sourceInputStream.close(); } catch (Exception e) {}
		if (targetOutputStream != null) try { targetOutputStream.close(); } catch (Exception e) {}
	}
	

}
