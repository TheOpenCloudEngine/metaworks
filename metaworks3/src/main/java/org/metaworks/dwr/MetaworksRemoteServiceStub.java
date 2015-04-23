package org.metaworks.dwr;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.metaworks.WebObjectType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.thoughtworks.xstream.XStream;

public class MetaworksRemoteServiceStub /*extends MetaworksRemoteService*/{
	
	static XStream xstream = new XStream(/*new DomDriver()*/);
	HttpClient httpClient;
	String contextRoot;

	protected static MetaworksRemoteServiceStub instance;
	public static MetaworksRemoteServiceStub getInstance(String host, String port, String contextRoot) { 

		if(instance==null){
			instance = new MetaworksRemoteServiceStub(host, port, contextRoot);
		}
		
		return instance;
	}
	
	protected MetaworksRemoteServiceStub(String host, String port, String contextRoot){
		httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHostConfiguration().setHost(host, Integer.parseInt(port), "http");
		httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		
		this.contextRoot = contextRoot;
	}
	
	
	public void clearMetaworksType(String className) throws Exception {
		
System.out.println("Not implemented.");
		
	}


	public WebObjectType getMetaworksType(String className) throws Exception {
System.out.println("Not implemented.");
		
		return null; 
	}
	
    public Map<String, String> callMetaworksService(String objectTypeName, Map<String, String> FieldValueMap, String methodName) throws Exception{

		StringBuffer objectStr = new StringBuffer();
		objectStr.append("<")
		.append(objectTypeName)
		.append("/>");
		
		for(String key : FieldValueMap.keySet()){
			objectStr.append("  <").append(key).append(">").append(FieldValueMap.get(key)).append("</").append(key).append(">");
		}

		objectStr.append("</")
		.append(objectTypeName)
		.append(">");

		
		InputStream is =  callMetaworksServiceImpl(objectTypeName, objectStr.toString(), methodName, (String)null);
		
	    Document doc = null;
	      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	      DocumentBuilder builder = dbf.newDocumentBuilder();
	      doc = builder.parse(is);
	     

	    Map returnValueMap = new HashMap<String, String>();
	      
	    NodeList nodeList = doc.getChildNodes();
	    
	    for(int i=0; i<nodeList.getLength(); i++){
	    	Node node = nodeList.item(i);
	    	returnValueMap.put(node.getNodeName(), node.getNodeValue());
	    }
	    
	    return returnValueMap;
	}

		
    public Object callMetaworksService(String objectTypeName, Object object, String methodName, Map<String, Object> autowiredFields) throws Exception{

		String objectStr = xstream.toXML(object);
		String autowiredFieldsStr = xstream.toXML(autowiredFields);
	
		InputStream is = callMetaworksServiceImpl(objectTypeName, objectStr, methodName, autowiredFieldsStr);
		
		object = xstream.fromXML(is);
    	
		return object;

	}

    protected InputStream callMetaworksServiceImpl(String objectTypeName, String objectStr, String methodName, String autowiredFieldsStr) throws Exception{

		Map<String, String> formValueMap = new HashMap<String, String>();
		formValueMap.put("className", objectTypeName);
		formValueMap.put("methodName", methodName);
		formValueMap.put("object", objectStr);
		formValueMap.put("autowiredFields", autowiredFieldsStr);
		
		PostMethod method = 
				new PostMethod(contextRoot + "/dwr/xstr-rpc/callMetaworksService");

		method.setRequestBody(createParameter(formValueMap));
		method.setRequestHeader("Content-Type", PostMethod.FORM_URL_ENCODED_CONTENT_TYPE 
				+ "; charset=" + "UTF-8");

		httpClient.executeMethod(method);
		InputStream is = method.getResponseBodyAsStream();

		return is;
	}


	private NameValuePair[] createParameter(Map map) throws Exception {
//		System.out.println("map : " + map);
		NameValuePair[] nvPair = new NameValuePair[map.size()];
		Iterator iter = map.keySet().iterator();
		for(int i=0; iter.hasNext(); i++) {
			String key = (String)iter.next();
			String value = (String)map.get(key);
			if ( value != null ) value = fromEncode(value);
			nvPair[i] = new NameValuePair(key, value);
		}
		return nvPair;
	}
	
	private static String fromEncode(String str) {
        if (str == null)
            return null;
        try {
			return new String(str.getBytes("UTF-8"), "8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
    }	
	
}
