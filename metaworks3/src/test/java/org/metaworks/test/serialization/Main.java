package org.metaworks.test.serialization;

import org.metaworks.annotation.ServiceMethod;
import org.metaworks.dwr.SerializationSensitive;
import org.metaworks.test.parameterizedcall.Part;

public class Main implements SerializationSensitive {

	public Main(){
	}
	
	Part part;
		public Part getPart() {
			return part;
		}
		public void setPart(Part part) {
			this.part = part;
		}


	@Override
	public void afterDeserialization() {
		Part part = new Part();
		part.setId("deserialized");

		setPart(part);
	}

	@Override
	public void beforeSerialization() {
		Part part = new Part();
		part.setId("serialized");

		setPart(part);
	}

	@ServiceMethod(callByContent = true)
	public Main doSomething(){
		assert ("deserialized".equals(getPart().getId()));

		return this;
	}
}
