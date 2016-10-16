package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

public class RetypeAttribute extends AlterAttribute {

	private static final String MSG = "Attribute::Retype::";
	
	public RetypeAttribute(MEntity entity, MAttribute attribute, MAttribute legacyAttribute, String version2, String version1){
		super(entity, attribute, legacyAttribute, version2, version1);
	}
	
	public String toString(){
		return MSG+attribute.getName()+" "+legacyAttribute.getType()+"->"+attribute.getType()+" [Entity"+Operation.SEP+entity.getEntityName()+"]::" + this.getVersion1() + "-" + this.getVersion2();
	}
	
}
