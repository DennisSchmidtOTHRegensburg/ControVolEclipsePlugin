package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

public abstract class AlterAttribute extends OperationAttribute {

	protected MAttribute legacyAttribute;
	
	public AlterAttribute(MEntity entity, MAttribute attribute, MAttribute legacyAttribute, String version2, String version1){
		super(entity, attribute, version2, version1);
		this.legacyAttribute = legacyAttribute;
	}
	
	public MAttribute getLegacyAttribute() {
		return legacyAttribute;
	}

	public void setLegacyAttribute(MAttribute legacyAttribute) {
		this.legacyAttribute = legacyAttribute;
	}
	
}