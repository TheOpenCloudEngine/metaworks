package org.metaworks.annotation;

public class ValidatorContext {

	public final static String VALIDATE_NOTNULL = "notnull";
	public final static String VALIDATE_NULL = "null";
	public final static String VALIDATE_MAXBYTE = "maxbyte";
	public final static String VALIDATE_MAX = "maxlength";
	public final static String VALIDATE_MIN = "minlength";
	public final static String VALIDATE_NUMBERZERO = "numberzero";
	public final static String VALIDATE_REGULAREXPRESSION = "regularexpression";
	public final static String VALIDATE_CONDITION = "condition";
	public final static String VALIDATE_ASSERTTRUE = "asserttrue";
	public final static String VALIDATE_ASSERTFALSE = "assertfalse";
	
	public String name;
		public String getName() {
			return this.name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
	public String message;		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
	
	public String condition;
		public String getCondition() {
			return condition;
		}
		public void setCondition(String condition) {
			this.condition = condition;
		}

	public String availableUnder;
		public String getAvailableUnder() {
			return availableUnder;
		}
		public void setAvailableUnder(String availableUnder) {
			this.availableUnder = availableUnder;
		}

	public String[] events;
		public String[] getEvents() {
			return events;
		}
		public void setEvents(String[] events) {
			this.events = events;
		}
		
	public String[] options;
		public String[] getOptions() {
			return this.options;
		}
		public void setOptions(String[] options) {
			this.options = options;
		}	
}