package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

public class DeleteAttribute extends OperationAttribute {

	private static final String MSG = "Attribute::Delete::";
	
	public DeleteAttribute(MEntity entity, MAttribute attribute, String version2, String version1){
		super(entity, attribute, version2, version1);
	}
	
	public String toString(){
		return MSG+attribute.getName()+"_" + attribute.getType() + "[Entity"+Operation.SEP+entity.getEntityName()+"]::" + this.getVersion1() + "-" + this.getVersion2();
	}
	
}
