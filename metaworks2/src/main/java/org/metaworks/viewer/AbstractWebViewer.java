package org.metaworks.viewer;

import java.util.*;
import org.metaworks.*;

abstract public class AbstractWebViewer implements WebViewer{
	Hashtable attributes = new Hashtable();

	public void initialize(Hashtable attr){
		attributes = attr;		
	}
	
/////////////////// catridge ///////////////////

	abstract public String getViewerHTML(Instance data, String colName);
	
/////////////////// appended services ///////////////////

	public Object getAttribute(String attrName){
		return attributes.get(attrName);
	}
	
	String face;

		public String getFace() {
			return face;
		}
	
		public void setFace(String face) {
			this.face = face;
		}
	
}