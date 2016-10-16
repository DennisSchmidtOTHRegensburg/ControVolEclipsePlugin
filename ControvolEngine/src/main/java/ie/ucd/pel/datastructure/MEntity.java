package ie.ucd.pel.datastructure;

import java.util.Set;
import java.util.TreeSet;

import ie.ucd.pel.engine.util.Util;

public class MEntity implements Comparable<MEntity> {

	protected String entityName;
	protected Set<MAttribute> attributes = new TreeSet<MAttribute>();
	protected String superEntityName;
	protected String location = "";
	protected Boolean isEntity = true; // Used to deal with inheritance
	protected Boolean isMophEntity=true;
	protected Boolean isObjEntity=true;
	

	public MEntity(String entityName, String location){
		this.entityName = entityName;
		this.location = location;
		this.location=Util.completeUnixPath(this.location);
	}

	public MEntity(String entityName, String location, String superEntityName){
		this.entityName = entityName;
		this.location = location;
		this.superEntityName = superEntityName;
		this.location=Util.completeUnixPath(this.location);
	}
	public String getEntityName(){
		return this.entityName;
	}

	public String getSuperEntityName(){
		return this.superEntityName;
	}

	public void setSuperEntity(String superEntityName) {
		this.superEntityName = superEntityName;
	}
	
	public String getLocation(){
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public Set<MAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<MAttribute> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(MAttribute attribute){
		this.attributes.add(attribute);
	}
	
	public void addAttributes(Set<MAttribute> attributes){
		this.attributes.addAll(attributes);
	}
	
	public Boolean isEntity(){
		return isEntity=(isObjEntity||isMophEntity);
	}
	
	public Boolean getIsMophEntity() {
		return isMophEntity;
	}
	
	public Boolean getIsObjEntity() {
		return isObjEntity;
	}
	
	public void setIsObjEntity(Boolean isObjEntity){
		this.isObjEntity=isObjEntity;
	}
	
	public void setIsMophEntity(Boolean isMophEntity){
		this.isMophEntity=isMophEntity;
	}
	
	public Boolean containsAttribute(String attName){
		return (getAttribute(attName) != null);
	}

	public MAttribute getAttribute(String attName){
		MAttribute attDesc = null;
		for (MAttribute attDescAux : this.getAttributes()){
			if (attDescAux.getName().equals(attName)){
				attDesc = attDescAux;
			}
			for (String formerName : attDescAux.getFormerNames()){
				if (formerName.equals(attName)){
					attDesc = attDescAux;
				}
			}
		}
		return attDesc;
	}

	@Override
	public int compareTo(MEntity o) {
		return this.getEntityName().compareTo(o.getEntityName());
	}

	public String toString(){
		String str = this.getEntityName();
		for (MAttribute attDesc : this.getAttributes()){
			str += "\n\t"+attDesc.toString();
		}
		return str;
	}

}
