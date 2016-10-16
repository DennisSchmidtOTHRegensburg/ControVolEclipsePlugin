package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

public class CleanRenameAttribute extends RenameAttribute {

	private static final String MSG = "Attribute::CleanRename::";
	
	public CleanRenameAttribute(MEntity entity, MAttribute attribute, MAttribute legacyAttribute, String version2, String version1){
		super(entity, attribute, legacyAttribute, version2, version1);
	}

	public String toString(){
		return MSG+legacyAttribute.getName()+"->"+attribute.getName()+" [Entity"+Operation.SEP+entity.getEntityName()+"]::" + this.getVersion1() + "-" + this.getVersion2();
	}
	
	@Override
	public Boolean isCleanRenameAttribute() {
		return true;
	}

	@Override
	public Boolean isWildRenameAttribute() {
		return false;
	}
	
}