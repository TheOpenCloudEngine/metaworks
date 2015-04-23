package org.metaworks;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class ServiceMethodContext{
	

	public static final String WIRE_PARAM_CLS = "wireParamCls:";

	public final static String TARGET_AUTO 		= "auto"; 
	public final static String TARGET_SELF 		= "self"; 
	public final static String TARGET_APPEND 	= "append"; 
	public final static String TARGET_PREPEND 	= "prepend"; 
	public final static String TARGET_STICK 	= "stick";
	public final static String TARGET_POPUP 	= "popup";
	public final static String TARGET_POPUP_OVER_POPUP 	= "popupOverPopup";
//	public final static String TARGET_WINDOW 	= "window";
	public final static String TARGET_NONE 		= "none";
	public final static String TARGET_TOP 		= "top";
	public final static String TARGET_OPENER 	= "opener";
	
	public final static String MOUSEBINDING_LEFTCLICK 	= "left";
	public final static String MOUSEBINDING_RIGHTCLICK 	= "right";
	public final static String MOUSEBINDING_ONOVER 		= "over";
	public final static String MOUSEBINDING_ONOUT 		= "out";
	
	public boolean clientSide;
		public boolean isClientSide() {
			return clientSide;
		}
		public void setClientSide(boolean clientSide) {
			this.clientSide = clientSide;
		}

	public boolean needToConfirm;
		public boolean isNeedToConfirm() {
			return needToConfirm;
		}
		public void setNeedToConfirm(boolean needToConfirm) {
			this.needToConfirm = needToConfirm;
		}

	public boolean callByContent;
		public boolean isCallByContent() {
			return callByContent;
		}
		public void setCallByContent(boolean callByContent) {
			this.callByContent = callByContent;
		}

	String methodName;
		public String getMethodName() {
			return methodName;
		}
		public void setMethodName(String methodName) {
			this.methodName = methodName;
		}
		
	String when;
		public String getWhen() {
			return when;
		}
		public void setWhen(String when) {
			this.when = when;
		}

	String where;
		public String getWhere() {
			return where;
		}
		public void setWhere(String where) {
			this.where = where;
		}
		
	String how;
		public String getHow() {
			return how;
		}
		public void setHow(String how) {
			this.how = how;
		}

	String target;
		public String getTarget() {
			return target;
		}
		public void setTarget(String target) {
			this.target = target;
		}
	
	String displayName;
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		
	String rtnCls;
		
		public String getRtnCls() {
			return rtnCls;
		}
		public void setRtnCls(String rtnCls) {
			this.rtnCls = rtnCls;
		}

	String[] loader;	
		public String[] getLoader() {
			return loader;
		}
		public void setLoader(String[] loader) {
			this.loader = loader;
		}
		
	String[] cacheCls;
	
		public String[] getCacheCls() {
			return cacheCls;
		}
		public void setCacheCls(String[] cacheCls) {
			this.cacheCls = cacheCls;
		}
		
	boolean loadOnce;
		public boolean isLoadOnce() {
			return loadOnce;
		}
		public void setLoadOnce(boolean loadOnce) {
			this.loadOnce = loadOnce;
		}

	boolean childrenGetter;
		public boolean isChildrenGetter() {
			return childrenGetter;
		}
		public void setChildrenGetter(boolean childrenGetter) {
			this.childrenGetter = childrenGetter;
		}

	boolean nameGetter;
		public boolean isNameGetter() {
			return nameGetter;
		}
		public void setNameGetter(boolean nameGetter) {
			this.nameGetter = nameGetter;
		}
		
	
	Map<String, Object> attributes;
		public Map<String, Object> getAttributes() {
			return attributes;
		}
		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}

	Map<String, String> except;
		public Map<String, String> getExcept() {
			return except;
		}
		public void setExcept(Map<String, String> except) {
			this.except = except;
		}

	Map<String, String> payload;
		public Map<String, String> getPayload() {
			return payload;
		}
		public void setPayload(Map<String, String> payload) {
			this.payload = payload;
		}

	List<String> bindingFor;
		public List<String> getBindingFor() {
			return bindingFor;
		}
		public void setBindingFor(List<String> bindingFor) {
			this.bindingFor = bindingFor;
		}

	List<String> eventBinding;
		public List<String> getEventBinding() {
			return eventBinding;
		}
		public void setEventBinding(List<String> eventBinding) {
			this.eventBinding = eventBinding;
		}

	List<String> keyBinding;
		public List<String> getKeyBinding() {
			return keyBinding;
		}
		public void setKeyBinding(List<String> keyMapping) {
			this.keyBinding = keyMapping;
		}
		
	boolean callByRightClick;
		public boolean isCallByRightClick() {
			return callByRightClick;
		}
		public void setCallByRightClick(boolean callByRightClick) {
			this.callByRightClick = callByRightClick;
		}

	String mouseBinding;
		public String getMouseBinding() {
			return mouseBinding;
		}
		public void setMouseBinding(String mouseBinding) {
			this.mouseBinding = mouseBinding;
		}
		
	boolean inContextMenu;
		public boolean isInContextMenu() {
			return inContextMenu;
		}
		public void setInContextMenu(boolean inContextMenu) {
			this.inContextMenu = inContextMenu;
		}
		
	boolean validate;
		public boolean isValidate() {
			return validate;
		}
		public void setValidate(boolean validate) {
			this.validate = validate;
		}
		
	boolean childrenLoader;
		public boolean isChildrenLoader() {
			return childrenLoader;
		}
		public void setChildrenLoader(boolean childrenLoader) {
			this.childrenLoader = childrenLoader;
		}
		
	boolean bindingHidden;
		public boolean isBindingHidden() {
			return bindingHidden;
		}
		public void setBindingHidden(boolean bindingHidden) {
			this.bindingHidden = bindingHidden;
		}
		
	boolean constructor;
		public boolean isConstructor() {
			return constructor;
		}
		public void setConstructor(boolean constructor) {
			this.constructor = constructor;
		}
		
	String group;
		public String getGroup() {
			return group;
		}
		public void setGroup(String group) {
			this.group = group;
		}
		
	Map<String, String> autowiredParams;
		public Map<String, String> getAutowiredParams() {
			return autowiredParams;
		}
		public void setAutowiredParams(Map<String, String> autowiredParams) {
			this.autowiredParams = autowiredParams;
		}

	transient Method method;
		public Method _getMethod() {  // The underbar (_) prevent the BeanSerializer serialize this property.
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		
	transient Map<String, Integer> payloadParameterIndexes;
		public Map<String, Integer> _getPayloadParameterIndexes() {  // The underbar (_) prevent the BeanSerializer serialize this property.
			return payloadParameterIndexes;
		}
		public void setPayloadParameterIndexes(
				Map<String, Integer> payloadParameterIndexes) {
			this.payloadParameterIndexes = payloadParameterIndexes;
		}
		
	
				
	 
	
		
}
