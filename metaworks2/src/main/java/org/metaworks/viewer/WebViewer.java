package org.metaworks.viewer;

import java.util.*;
import org.metaworks.*;

public interface WebViewer extends Viewer{

	public String getViewerHTML(Instance data, String colName);
	
}