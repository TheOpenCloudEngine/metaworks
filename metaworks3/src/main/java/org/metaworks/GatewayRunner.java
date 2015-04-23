package org.metaworks;

import org.metaworks.annotation.Face;
import org.metaworks.annotation.Id;
import org.metaworks.annotation.ServiceMethod;

public class GatewayRunner {

	public GatewayRunner(){
	}

	String fullClassName;
		@Id
		public String getFullClassName() {
			return fullClassName;
		}
		public void setFullClassName(String className) {
			this.fullClassName = className;
		}
		
	Object runningObject;	
		public Object getRunningObject() {
			return runningObject;
		}
		public void setRunningObject(Object runningObject) {
			this.runningObject = runningObject;
		}
		
	@ServiceMethod(callByContent=true)
	@Face(displayName="Run (Normal)")
	public void run() throws Exception{
		Object o = Thread.currentThread().getContextClassLoader().loadClass(getFullClassName()).newInstance();

		setRunningObject(o);
	}
}
