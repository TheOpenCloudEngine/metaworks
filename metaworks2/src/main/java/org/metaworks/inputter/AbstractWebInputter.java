package org.metaworks.inputter;

import org.metaworks.inputter.Inputter;
import org.metaworks.*;
import java.util.*;

abstract public class AbstractWebInputter extends AbstractInputter{

/// 나중에 inputter 에서 제거바람
	public void setValue(Object obj){}
	public Object getValue(){return null;}
	public java.awt.Component getComponent(){return null;}
	
	protected Hashtable attributes = new Hashtable();
//

////////////////// catridges /////////////////////

	
	public Object getValue(String httpVal){
		return httpVal;
	}

	abstract public String getInputterHTML();
	
	public Properties getScriptHTMLSet(){
		return null;
	}
	
}
