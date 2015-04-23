package org.metaworks.widget;

import org.metaworks.annotation.Id;


public class IFrame {
	
	public IFrame(){}
	
	public IFrame(String url){
		setSrc(url);
	}
	
	String id;
		@Id
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}

	String src;
		public String getSrc() {
			return src;
		}
		public void setSrc(String src) {
			this.src = src;
		}
		
	String width;
		public String getWidth() {
			return width;
		}
		public void setWidth(String width) {
			this.width = width;
		}

	String height;
		public String getHeight() {
			return height;
		}
		public void setHeight(String height) {
			this.height = height;
		}
}
