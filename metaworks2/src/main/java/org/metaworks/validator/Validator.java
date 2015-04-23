package org.metaworks.validator;

import org.metaworks.Instance;

public interface Validator extends java.io.Serializable{

	public String validate(Object data, Instance instance);
		
}
