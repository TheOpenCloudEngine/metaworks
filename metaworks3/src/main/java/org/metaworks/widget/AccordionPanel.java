package org.metaworks.widget;

import java.util.ArrayList;
import java.util.List;

public class AccordionPanel {

	private List accordions;

	public List getAccordions() {
		return accordions;
	}

	public void setAccordions(List accordions) {
		this.accordions = accordions;
	}
	
	public AccordionPanel() {
		this.accordions = new ArrayList();
	}
	
	public void add(Accordion accordion) {
		this.accordions.add(accordion);
	}
	
	public void remove(Accordion accordion) {
		this.accordions.remove(accordion);
	}
	
}
