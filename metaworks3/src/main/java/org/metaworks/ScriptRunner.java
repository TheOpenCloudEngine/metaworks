package org.metaworks;

public class ScriptRunner {

	String script;
		public String getScript() {
			return script;
		}
		public void setScript(String script) {
			this.script = script;
		}

	public ScriptRunner(){
		
	}
	
	public ScriptRunner(String script){
		this.setScript(script);
	}
}
