package org.metaworks.widget;

public class Accordion {

	private String title;
	private Object content;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
	public Accordion(){}
	
	public Accordion(String title){
		this.title = title;
	}
	
	public Accordion(String title,Object content) {
		this.title = title;
		this.content = content;
	}
	
}
