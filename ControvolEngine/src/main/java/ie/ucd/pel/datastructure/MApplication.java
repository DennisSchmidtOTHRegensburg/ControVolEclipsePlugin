package ie.ucd.pel.datastructure;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.TreeSet;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class MApplication implements Comparable<MApplication> {

	//***************
	//** CONSTANTS **
	//***************

	// Application
	public static final String TAG_APPLICATION = "application";
	public static final String TAG_APPLICATION_VERSION = "version";
	public static final String TAG_APPLICATION_LOCATION = "location";
	
	// Entity
	public static final String TAG_ENTITY = "entity";
	public static final String TAG_ENTITY_NAME = "name";
	public static final String TAG_ENTITY_SUPERNAME = "superName";
	public static final String TAG_ENTITY_LOCATION = "location";
	// Attribute
	public static final String TAG_ATTRIBUTE = "attribute";
	public static final String TAG_ATTRIBUTE_NAME = "name";
	public static final String TAG_ATTRIBUTE_ACCESS = "access";
	public static final String TAG_ATTRIBUTE_TYPE = "type";
	public static final String TAG_ATTRIBUTE_LOCATION = "location";
	public static final String TAG_ATTRIBUTE_LINE = "line";
	// Former name (for attributes)
	public static final String TAG_FORMERNAME = "legacyName";
	

	//****************
	//** ATTRIBUTES **
	//****************

	protected String location;
	protected String srcFolder;
	protected String version = null;
	protected Set<MEntity> entities = new TreeSet<MEntity>();

	//*************
	//** METHODS **
	//*************

	public MApplication(String location){
		this.location = location;
	}
	
	public MApplication(String location, String version){
		this.location = location;
		this.version = version;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<MEntity> getEntities() {
		return entities;
	}

	public void setEntities(Set<MEntity> entityDescs) {
		this.entities = entityDescs;
	}

	public void addEntity(MEntity entityDesc){
		if (entityDesc != null){
			this.entities.add(entityDesc);
		}
	}

	public Boolean containsEntity(String entityName){
		return (getEntity(entityName) != null);
	}

	public MEntity getEntity(String entityName){
		MEntity desc = null;
		for (MEntity entityDesc : this.entities){
			if (entityDesc.getEntityName().equals(entityName)){ 
				desc = entityDesc;
			}
		}
		return desc;
	}
	
	public Boolean removeEntity(String entityName){
		Set<MEntity> entities = new TreeSet<MEntity>();
		Integer sizeA = this.entities.size();
		for (MEntity entity : this.entities){
			if (!entity.getEntityName().equals(entityName)){
				entities.add(entity);
			}
		}
		this.entities = entities;
		Integer sizeB = this.entities.size();
		return (sizeA != sizeB);
	}

	public String toString(){
		String str = this.location+" (version "+this.version+")\n";
		for (MEntity entityDesc : entities){
			str += entityDesc.toString()+"\n";
		}
		return str;
	}



	public void toXml(OutputStream os){
		Document doc = this.getXml();
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
		try {
			xmlOutputter.output(doc, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Document getXml(){
		Document doc = new Document();
		Element application = new Element(TAG_APPLICATION);
		application.setAttribute(TAG_APPLICATION_LOCATION, this.getLocation());
		if (this.getVersion() != null){
			application.setAttribute(TAG_APPLICATION_VERSION, this.getVersion());
		}
		doc.setRootElement(application); 
		for (MEntity mEntity : this.getEntities()){
			Element entity = mEntity2element(mEntity);
			doc.getRootElement().addContent(entity);
		}
		return doc;
	}

	private Element mEntity2element(MEntity mEntity){
		Element entity = new Element(TAG_ENTITY);
		entity.setAttribute(TAG_ENTITY_NAME, mEntity.getEntityName());
		entity.setAttribute(TAG_ENTITY_LOCATION, mEntity.getLocation());
		if (mEntity.getSuperEntityName() != null){
			entity.setAttribute(TAG_ENTITY_SUPERNAME, mEntity.getSuperEntityName());
		}
		for (MAttribute mAttribute : mEntity.getAttributes()){
			if (!mAttribute.isDeleted){
				entity.addContent(mAttribute2Element(mAttribute));
			}
		}
		return entity;
	}

	private Element mAttribute2Element(MAttribute mAttribute) {
		Element attribute = new Element(TAG_ATTRIBUTE);
		attribute.setAttribute(TAG_ATTRIBUTE_NAME, mAttribute.getName());
		attribute.setAttribute(TAG_ATTRIBUTE_TYPE, mAttribute.getType());
		attribute.setAttribute(TAG_ATTRIBUTE_ACCESS, mAttribute.getAccess());
		if (mAttribute.getLocation() != null){
			if ((mAttribute.getLocation().getClassName() != null) && (!mAttribute.getLocation().getClassName().equals(""))){
				attribute.setAttribute(TAG_ATTRIBUTE_LOCATION, mAttribute.getLocation().getClassName());
			}
			if (mAttribute.getLocation().getLineNumber() != null){
				attribute.setAttribute(TAG_ATTRIBUTE_LINE, mAttribute.getLocation().getLineNumber().toString());
			}
		}
		for (String formerName : mAttribute.getFormerNames()){
			Element eltFormerName = new Element(TAG_FORMERNAME);
			eltFormerName.addContent(formerName);
			attribute.addContent(eltFormerName);
		}
		return attribute;
	}

	@Override
	public int compareTo(MApplication o) {
		return this.getVersion().compareTo(o.getVersion());
	}

}
