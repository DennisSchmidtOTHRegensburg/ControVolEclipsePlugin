package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MEntity;

public abstract class OperationEntity extends Operation {

	protected static final String MSG = "Entity" + Operation.SEP;
	
	public OperationEntity(MEntity entity, String version2, String version1) {
		super(entity, version2, version1);
	}

}
