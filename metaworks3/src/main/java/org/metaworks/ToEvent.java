package org.metaworks;

import org.metaworks.filter.Filter;

public class ToEvent {
	
	Object target;
		public Object getTarget() {
			return target;
		}
		public void setTarget(Object target) {
			this.target = target;
		}
	
	String event;
		public String getEvent() {
			return event;
		}
		public void setEvent(String event) {
			this.event = event;
		}
	
	boolean match;
		public boolean isMatch() {
			return match;
		}
		public void setMatch(boolean match) {
			this.match = match;
		}
		
	Object data;
		public Object getData() {
			return data;
		}
		public void setData(Object data) {
			this.data = data;
		}
	
	Filter filter;
		public Filter getFilter() {
			return filter;
		}
		public void setFilter(Filter filter) {
			this.filter = filter;
		}
		
	public ToEvent(Object target, String event){
		this(target, event, null, false);
	}
	
	public ToEvent(Object target, String event, Object data, boolean match){
		this.setTarget(target);
		this.setEvent(event);
		this.setData(data);
		this.setMatch(match);
	}
	
	public ToEvent(Object target, String event, Object data){
		this(target, event, data, false);
	}
	
	public ToEvent(Object target, String event, Filter filter){
		this(target, event, null, false);
		this.setFilter(filter);
	}
}