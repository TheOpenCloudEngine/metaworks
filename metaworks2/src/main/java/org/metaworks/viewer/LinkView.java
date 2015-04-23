package org.metaworks.viewer;

import java.util.*;
import org.metaworks.*;

public class LinkView extends AbstractWebViewer{

	public LinkView(String action){
		if(action!=null)
			attributes.put("action", action);
	}
	
	public LinkView(){
		this(null);
	}
	
	
/////////////////// implements ///////////////////

	public String getViewerHTML(Instance rec, String colName){
		return "<a href='"+getAttribute("action")+"?" +rec.getKeyQueryString()+ "'>"+rec.get(colName)+"</a>";
	}

}