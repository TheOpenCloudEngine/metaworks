package org.metaworks.test.parameterizedcall;

import org.metaworks.annotation.Face;

public class SecondPart {
	
	Part part;
	@Face(faceClass=PartSelectBox.class)
		public Part getPart() {
			return part;
		}
	
		public void setPart(Part part) {
			this.part = part;
		}

}
