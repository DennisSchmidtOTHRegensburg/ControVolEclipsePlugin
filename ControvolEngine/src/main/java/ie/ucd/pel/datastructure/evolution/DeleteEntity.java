package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MEntity;

public class DeleteEntity extends OperationEntity {

	protected static final String MSG = "Delete"+Operation.SEP;
	
	public DeleteEntity(MEntity entity, String version2, String version1){
		super(entity, version2, version1);
	}
	
	public String toString(){
		return OperationEntity.MSG+DeleteEntity.MSG+entity.getEntityName() + "::" + this.getVersion1() + "-" + this.getVersion2();
	}
	
}
