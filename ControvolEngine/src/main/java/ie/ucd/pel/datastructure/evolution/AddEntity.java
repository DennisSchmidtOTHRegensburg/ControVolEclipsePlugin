package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MEntity;

public class AddEntity extends OperationEntity {

	private static final String MSG = "Add"+Operation.SEP;
	
	public AddEntity(MEntity entity, String version2, String version1){
		super(entity, version2, version1);
	}
	
	public String toString(){
		return OperationEntity.MSG+AddEntity.MSG+entity.getEntityName() + "::" + this.getVersion1() + "-" + this.getVersion2();
	}
	
}
