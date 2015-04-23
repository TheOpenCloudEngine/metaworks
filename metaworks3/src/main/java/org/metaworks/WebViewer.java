package org.metaworks;

import java.util.Hashtable;

import org.metaworks.viewer.Viewer;

public class WebViewer implements Viewer{

	@Override
	public void initialize(Hashtable arg0) {
		// TODO Auto-generated method stub
		
	}
	
	String face;

		public String getFace() {
			return face;
		}
	
		public void setFace(String faceName) {
			this.face = faceName;
		}

}
