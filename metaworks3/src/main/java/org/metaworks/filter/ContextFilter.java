package org.metaworks.filter;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;


public class ContextFilter extends Filter implements ContextAware {

	String[] compareContextList;
	MetaworksContext metaworksContext;

	public ContextFilter(String[] compareContextList, MetaworksContext metaworksContext){
		this.compareContextList = compareContextList;
		this.metaworksContext = metaworksContext;
	}
	
	public ContextFilter(String[] compareContextList, Object object){
		this.compareContextList = compareContextList;
		
		if(object instanceof ContextAware)
			this.metaworksContext = ((ContextAware)object).getMetaworksContext();
	}
	
	public String[] getCompareContextList() {
		return compareContextList;
	}
	public void setCompareContextList(String[] compareContextList) {
		this.compareContextList = compareContextList;
	}

	public MetaworksContext getMetaworksContext() {
		return metaworksContext;
	}
	public void setMetaworksContext(MetaworksContext metaworksContext) {
		this.metaworksContext = metaworksContext;
	}
}