/*
 * Created on 2004. 11. 3.
 */
package org.metaworks.dao;

import java.io.Serializable;
import java.util.Date;

import org.metaworks.ContextAware;
import org.metaworks.MetaworksContext;
import org.metaworks.ServiceMethodContext;
import org.metaworks.annotation.ServiceMethod;

/**
 * @author Jinyoung Jang
 */
public interface IDAO extends Serializable, ContextAware {
	
	

	public final static String HOW_NORMAL = MetaworksContext.HOW_NORMAL;
	public final static String HOW_STANDALONE = MetaworksContext.HOW_STANDALONE;
	public final static String HOW_IN_LIST = MetaworksContext.HOW_IN_LIST;
	public final static String HOW_MINIMISED = MetaworksContext.HOW_MINIMISED;
	public final static String HOW_EVER = MetaworksContext.HOW_EVER;
	
	public final static String WHEN_VIEW = MetaworksContext.WHEN_VIEW;
	public final static String WHEN_EDIT = MetaworksContext.WHEN_EDIT;
	public final static String WHEN_NEW = MetaworksContext.WHEN_NEW;
	public final static String WHEN_EVER = MetaworksContext.WHEN_EVER;
	
	public final static String WHERE_PC = MetaworksContext.WHERE_PC;
	public final static String WHERE_MOBILE = MetaworksContext.WHERE_MOBILE;
	public final static String WHERE_EVER = MetaworksContext.WHERE_EVER;

	public final static String TARGET_AUTO = ServiceMethodContext.TARGET_AUTO;
	public final static String TARGET_SELF = ServiceMethodContext.TARGET_SELF;
	public final static String TARGET_APPEND = ServiceMethodContext.TARGET_APPEND;
	public final static String TARGET_POPUP = ServiceMethodContext.TARGET_POPUP;
	public final static String TARGET_TOP = ServiceMethodContext.TARGET_TOP;
	
	
//	public int getRow() throws Exception;
//	public boolean absolute(int row) throws Exception;
	
	public void select() throws Exception;
	public int insert() throws Exception;
	public int update() throws Exception;
	public int update(String stmt) throws Exception;
	public int call() throws Exception;
	public void addBatch() throws Exception;
	public int[] updateBatch() throws Exception;
	
	public void beforeFirst() throws Exception;
	public boolean previous() throws Exception;
	public boolean next() throws Exception;
	public boolean first() throws Exception;
	public void afterLast() throws Exception;
	public boolean last() throws Exception;
	public boolean absolute(int pos) throws Exception;
	
	public int size();
	
//	public void setStatement(String stmt) throws Exception;
	
	public Object get(String key) throws Exception;
	public Object set(String key, Object value) throws Exception;
	
	public String getString(String key) throws Exception; 
	public Integer getInt(String key) throws Exception;
	public Long getLong(String key) throws Exception;
	public Boolean getBoolean(String key) throws Exception;
	public Date getDate(String key) throws Exception;
	
	public AbstractGenericDAO getImplementationObject();
	public void releaseResource() throws Exception;
	
	
	public void moveToInsertRow() throws Exception;
	
	//public void toArrayList() throws Exception;

}
