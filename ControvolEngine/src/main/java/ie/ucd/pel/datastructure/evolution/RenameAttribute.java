package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

public abstract class RenameAttribute extends AlterAttribute {

	private static final String MSG = "Attribute::Rename::";
	
	public RenameAttribute(MEntity entity, MAttribute attribute, MAttribute legacyAttribute, String version2, String version1){
		super(entity, attribute, legacyAttribute, version2, version1);
	}

	public String toString(){
		return MSG+legacyAttribute.getName()+"->"+attribute.getName()+" [Entity"+Operation.SEP+entity.getEntityName()+"]::" + this.getVersion1() + "-" + this.getVersion2();
	}
	
	abstract public Boolean isCleanRenameAttribute();
	abstract public Boolean isWildRenameAttribute();
}