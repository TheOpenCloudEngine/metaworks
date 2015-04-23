package org.metaworks.viewer;

import java.util.*;

public interface Viewer{
	public void initialize(Hashtable attr);
	public void setFace(String face);
	public String getFace();
}