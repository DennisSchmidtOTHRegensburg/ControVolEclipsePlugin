package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

public class AddAttribute extends OperationAttribute {

	private static final String MSG = "Add"+Operation.SEP;
	
	public AddAttribute(MEntity entity, MAttribute attribute, String version2, String version1){
		super(entity, attribute, version2, version1);
	}

	public String toString(){
		return OperationAttribute.MSG+AddAttribute.MSG+attribute.getName()+"_" + attribute.getType() + "[Entity"+Operation.SEP+entity.getEntityName()+"]::" + this.getVersion1() + "-" + this.getVersion2();
	}
	
}
