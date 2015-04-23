package org.metaworks;

public class MetaworksContext {
		
	public final static String HOW_NORMAL = "normal";
	public final static String HOW_STANDALONE = "standalone";
	public final static String HOW_IN_LIST = "inList";
	public final static String HOW_MINIMISED = "minimised";
	public final static String HOW_EVER = "however";
	
	public final static String WHEN_VIEW = "view";
	public final static String WHEN_EDIT = "edit";
	public final static String WHEN_NEW = "new";
	public final static String WHEN_READONLY = "readonly";
	public final static String WHEN_EVER = "whenever";
	
	public final static String WHERE_DIALOG = "dialog";
	public final static String WHERE_PC = "pc";
	public final static String WHERE_MOBILE = "mobile";
	public final static String WHERE_EVER = "wherever";
	
	public final static String MEDIA_PC = "pc";
	public final static String MEDIA_MOBILE = "mobile";
	public final static String MEDIA_EVER = "wherever";

	String when;
	String where;
	String how;
	String media;
	
	
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getHow() {
		return how;
	}
	public void setHow(String how) {
		this.how = how;
	}
	
}
