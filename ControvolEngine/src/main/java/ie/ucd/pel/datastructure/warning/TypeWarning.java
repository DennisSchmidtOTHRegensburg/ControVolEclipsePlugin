package ie.ucd.pel.datastructure.warning;

import ie.ucd.pel.datastructure.evolution.RetypeAttribute;

public abstract class TypeWarning extends Warning<RetypeAttribute> {

	public TypeWarning(RetypeAttribute op) {
		super(op);
	}
	
}
