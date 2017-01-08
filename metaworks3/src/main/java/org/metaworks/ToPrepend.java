package org.metaworks;

public class ToPrepend extends ToAppend{

	public ToPrepend(Object parent, Object target) {
		super(parent, target);

		setToFront(true);
	}

	public ToPrepend(Object parent, Object target, boolean match) {
		super(parent, target, match);

		setToFront(true);
	}
}
