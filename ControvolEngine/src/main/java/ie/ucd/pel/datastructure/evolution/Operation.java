package ie.ucd.pel.datastructure.evolution;

import ie.ucd.pel.datastructure.MEntity;

public abstract class Operation implements Comparable<Operation> {
	
	protected static final String SEP = "::";
	
	protected MEntity entity;
	protected String version1;
	protected String version2;

	public Operation(MEntity entity, String version2, String version1){
		this.entity = entity;
		this.version1 = version1;
		this.version2 = version2;
	}
	
	public MEntity getEntity() {
		return entity;
	}
	public void setEntity(MEntity entity) {
		this.entity = entity;
	}	
	
	public String getVersion1() {
		return version1;
	}
	
	public void setVersion1(String version1) {
		this.version1 = version1;
	}
	
	public String getVersion2() {
		return version2;
	}
	
	public void setVersion2(String version2) {
		this.version2 = version2;
	}
	
	// FIXME Very dirty to do it this way! 
	public int compareTo(Operation o){
		return this.toString().compareTo(o.toString());
	}
	
}
