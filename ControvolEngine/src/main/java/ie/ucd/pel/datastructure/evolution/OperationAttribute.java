package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

public abstract class OperationAttribute extends Operation {

	protected static final String MSG = "Attribute"+Operation.SEP;
	
	protected MAttribute attribute;
	
	public OperationAttribute(MEntity entity, MAttribute attribute, String version2, String version1){
		super(entity, version2, version1);
		this.attribute = attribute;
	}

	public MAttribute getAttribute() {
		return attribute;
	}

	public void setAttribute(MAttribute attribute) {
		this.attribute = attribute;
	}
	
}
