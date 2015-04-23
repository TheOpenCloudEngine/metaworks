package org.metaworks.widget;

import javax.persistence.Id;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Hidden;

@Face(ejsPath="dwr/metaworks/CleanObjectFace.ejs")
public class Clipboard {

	String id;
	Object content;
	
	public Clipboard(){
		
	}
	
	public Clipboard(String id){
		this(id, null);
	}
	
	public Clipboard(String id, Object content){
		this.setId(id);
		this.setContent(content);
	}
	
	@Id
	@Hidden
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Hidden
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
}
