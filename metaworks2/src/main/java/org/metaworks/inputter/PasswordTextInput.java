package org.metaworks.inputter;

//import com.ugsolutions.util.Painter;
//import com.pongsor.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.util.*;

import com.pongsor.util.*;

import org.metaworks.component.MetaWorksComponentCenter;
import org.metaworks.web.HTML;
import org.metaworks.web.WebUtil;
import org.metaworks.FieldDescriptor;

public class PasswordTextInput extends InputterAdapter{
	
	public PasswordTextInput(){
		super();
	}

	
/////////// implementations //////////////


	public Object getValue(){
		JPasswordField textField = (JPasswordField)getValueComponent();
		
		if(textField.getText().equals("")) return null;
		
		return textField.getText();
	}
	
	public void setValue(Object obj){
		
		//System.out.println("==> " + obj);
		
		if(obj !=null){
			String txt = obj.toString();
			((JPasswordField)getValueComponent()).setText(txt);
		}
		else
			((JPasswordField)getValueComponent()).setText("");
	}

	public Component getNewComponent(){
		if(isMandatory()){
			return new JPasswordField(20){
				public void paint(Graphics g){
                			super.paint(g);
//                			Painter.paintIsRequired(this, g);
            			}
        		};
        	}
		else
			return new JPasswordField(20);
	}

//////////// overrides //////////////

	public void initialize(Hashtable attrs){		
		super.initialize(attrs);
		
		//we don't guarrantee the value component is surely a JTextField if overriden
		Component c = getValueComponent();
		if(!(c instanceof JPasswordField)) return;
		
		final JPasswordField textField = (JPasswordField)c; 
		Document doc = textField.getDocument();		
		doc.addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e){ 			
				int iSize = getSizeInt();
				if(iSize < 1) return;
				Document d=e.getDocument();
				                	
				try{
					int docSize = 0;
					String s2 = d.getText(0, (docSize = d.getLength()));
					int currSize = s2.getBytes().length;
        		
					if(currSize > iSize){
						final String [] out = ByteStringTokenizer.getToken(s2, iSize);
						SwingUtilities.invokeLater(new Runnable() { 
							public void run() { 
								textField.setText(out[0]); 
							} 
						}); 
					}
				}catch(Exception ex){
					System.out.println("TextInput..changedUpdate: "+ ex.getMessage());
					//ex.printStackTrace();
				}
			}
			public void insertUpdate(DocumentEvent e){
					changedUpdate(e);  
			}
			public void removeUpdate(DocumentEvent e){
				changedUpdate(e);
			}
		});
	}

////////////////////// for web ///////////////////////

	public String getInputterHTML(String section, FieldDescriptor fd, Object defaultValue, Map options){
		StringBuffer html = new StringBuffer();
		
		html.append("<input"); 
		html.append(HTML.getAttrHTML("name", "_" + section + "_" + fd.getName()));
		html.append(HTML.getAttrHTML("size", fd.getAttribute("size")));
		
		if(defaultValue!=null)
			html.append(HTML.getAttrHTML("value", defaultValue));
		else
			html.append(HTML.getAttrHTML("value", fd.getAttribute("default")));

		html.append(">");
		
		return html.toString();
	}
	
	public Object createValueFromHTTPRequest(Map parameterValues, String section, String fieldName, Object oldValue){
		return createStringFromHTTPRequest(parameterValues, section, fieldName);
	}
	
	public static String createStringFromHTTPRequest(Map parameterValues, String section, String fieldName){
		try{
			String value = ((String[])parameterValues.get("_" + section + "_" + fieldName))[0];
			
			if(WebUtil.webStringDecoder!=null && !parameterValues.containsKey("ignoreDecoder"))
				value = WebUtil.webStringDecoder.decode(value);
				            	
			if(value.trim().length()==0) value=null;
			
			return value;
		}catch(Exception e){
			return null;
		}
	}


	public boolean isEligibleType(Class type) {
		return type == String.class;
	}
}
