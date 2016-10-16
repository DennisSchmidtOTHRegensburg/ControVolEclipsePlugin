package ie.ucd.pel.datastructure;

import ie.ucd.pel.engine.util.CstJava;

import java.util.ArrayList;
import java.util.List;

public class MAttribute implements Comparable<MAttribute> {

	protected String name;	
	protected List<String> formerNames = new ArrayList<String>();
	protected String type;
	protected MLocation location;
	protected String access;
	protected Boolean isDeleted = false; // true if the attribute is preceded by @Ignore or @IgnoreSave or @Transient or @NotSaved
	protected Boolean isLoaded  = true;  // false if the attribute is preceded by @Ignore or @IgnoreLoad or @Transient

	public MAttribute(String name, String type, String className){
		this.name = name;
		this.type = type;
		this.location = new MLocation(className);
		this.access = CstJava.ACCESS_PACKAGE;
	}
	
	public MAttribute(String name, String type, String className, Integer line){
		this.name = name;
		this.type = type;
		this.location = new MLocation(className, line);
		this.access = CstJava.ACCESS_PACKAGE;
	}
	
	public MAttribute(String name, String type, MLocation location){
		this.name = name;
		this.type = type;
		this.location = location;
		this.access = CstJava.ACCESS_PACKAGE;
	}
	
	public MAttribute(String name, String type, String className, String access){
		this.name = name;
		this.type = type;
		this.location = new MLocation(className);
		this.access = access;
	}
	
	public MAttribute(String name, String type, String className, Integer line, String access){
		this.name = name;
		this.type = type;
		this.location = new MLocation(className, line);
		this.access = access;
	}
	
	public MAttribute(String name, String type, MLocation location, String access){
		this.name = name;
		this.type = type;
		this.location = location;
		this.access = access;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFormerNames() {
		return formerNames;
	}

	public void setFormerNames(List<String> formerNames) {
		this.formerNames = formerNames;
	}
	
	public void addFormerName(String aFormerName){
		if (!this.formerNames.contains(aFormerName)){
			this.formerNames.add(aFormerName);
		}
	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public MLocation getLocation() {
		return this.location;
	}
	
	public Boolean isDeleted(){
		return this.isDeleted;
	}
	
	public void setIsDeleted(Boolean isDeleted){
		this.isDeleted = isDeleted;
	}
	
	public Boolean isLoaded(){
		return this.isLoaded;
	}
	
	public void setIsLoaded(Boolean isLoaded){
		this.isLoaded = isLoaded;
	}
	
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public String toString(){
		String str = this.getType()+" "+this.name;
		if (formerNames.size() > 0){
			str += " "+formerNames.toString();
		}
		str += " (" + this.getLocation().getClassName();
		if (this.getLocation().getLineNumber() != null){
			str += " " + this.getLocation().getLineNumber();
		}
		str += ")";
		return str;
	}

	@Override
	public int compareTo(MAttribute o) {
		return this.getName().compareTo(o.getName());
	}

}
