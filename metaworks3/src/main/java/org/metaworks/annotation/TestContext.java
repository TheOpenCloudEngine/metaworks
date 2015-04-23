package org.metaworks.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TestContext {
	
	public String[] value;
		public String[] getValue() {
			return value;
		}
		public void setValue(String[] value) {
			this.value = value;
		}
		
	public String[] next;
		public String[] getNext() {
			return next;
		}
		public void setNext(String[] next) {
			this.next = next;
		}
				
	public String scenario;
		public String getScenario() {
			return scenario;
		}
		public void setScenario(String testName) {
			this.scenario = testName;
		}
		
	public String[] instruction;
		public String[] getInstruction() {
			return instruction;
		}
		public void setInstruction(String[] instruction) {
			this.instruction = instruction;
		}
		
	boolean starter;
		public boolean isStarter() {
			return starter;
		}
		public void setStarter(boolean starter) {
			this.starter = starter;
		}
		
}
