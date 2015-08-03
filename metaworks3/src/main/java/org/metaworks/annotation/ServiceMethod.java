package org.metaworks.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.metaworks.ServiceMethodContext;
import org.metaworks.dao.IDAO;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ServiceMethod {

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


	String when() 				default IDAO.WHEN_EVER;
	String where() 				default IDAO.WHERE_EVER;
	String how() 				default IDAO.HOW_EVER;
	String target() 			default ServiceMethodContext.TARGET_AUTO;
	
	boolean callByContent() 	default false;
	boolean needToConfirm() 	default false;	//shows 'Are you sure to do this....?'
	boolean clientSide() 		default false;	//
	
	String[] payload() default {};
	String[] except() default {};

	String[] bindingFor()		default {"@this"};
	String[] eventBinding() 	default {};
	String[] keyBinding() 		default {};
	String mouseBinding() 		default "";
	
	boolean validate()			default false;	
	boolean inContextMenu() 	default false;
	
	String[] loader() 			default {};
	
	String[] cacheClasses() 	default {};
	
	boolean loadOnce() 			default false;
	boolean childrenLoader() 	default false;
	boolean constructor()		default false;
	boolean bindingHidden() 	default false;
}
