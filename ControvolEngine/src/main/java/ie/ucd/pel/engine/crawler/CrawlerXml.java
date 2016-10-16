package ie.ucd.pel.engine.crawler;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class CrawlerXml implements ICrawler {
	
	public CrawlerXml(){}

	public MApplication getApplication(String fileName){
		MApplication app = null;
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(fileName);

		try {
			Document document = (Document) builder.build(xmlFile);
			Element application = document.getRootElement();
			String location = application.getAttributeValue(MApplication.TAG_APPLICATION_LOCATION); 
			String version = application.getAttributeValue(MApplication.TAG_APPLICATION_VERSION); // Might be null
			List<Element> entities = application.getChildren(MApplication.TAG_ENTITY, application.getNamespace());
			app = new MApplication(location, version);
			app.setVersion(version);
			for (Element entity : entities) {
				String entityName = entity.getAttributeValue(MApplication.TAG_ENTITY_NAME);
				String superEntityName = entity.getAttributeValue(MApplication.TAG_ENTITY_SUPERNAME); // Might be null
				String entityLocation = entity.getAttributeValue(MApplication.TAG_ENTITY_LOCATION);
				MEntity mEntity;
				if (superEntityName == null){
					mEntity = new MEntity(entityName, entityLocation);
				} else {
					mEntity = new MEntity(entityName, entityLocation, superEntityName);
				}
				List<Element> attributes = entity.getChildren(MApplication.TAG_ATTRIBUTE, entity.getNamespace()); 
				for (Element attribute : attributes){
					String attributeName = attribute.getAttributeValue(MApplication.TAG_ATTRIBUTE_NAME);
					String attributeType = attribute.getAttributeValue(MApplication.TAG_ATTRIBUTE_TYPE);
					String attributeLocation = attribute.getAttributeValue(MApplication.TAG_ATTRIBUTE_LOCATION);
					String attributeAccess = attribute.getAttributeValue(MApplication.TAG_ATTRIBUTE_ACCESS);
					Integer attributeLine = null;
					String attributeLineAux = attribute.getAttributeValue(MApplication.TAG_ATTRIBUTE_LINE);
					if ((attributeLineAux != null) && (!attributeLineAux.equals(""))){
						attributeLine = Integer.valueOf(attributeLineAux);
					}
					MAttribute mAttribute = new MAttribute(attributeName, attributeType, attributeLocation, attributeLine, attributeAccess); 
					List<Element> attributeFormerNames = attribute.getChildren(MApplication.TAG_FORMERNAME, attribute.getNamespace());
					for (Element attributeFormerName : attributeFormerNames){
						String formerName = attributeFormerName.getValue();
						mAttribute.addFormerName(formerName);
					}
					mEntity.addAttribute(mAttribute);
				}
				app.addEntity(mEntity);
			}
		} catch (IOException io) {
			System.out.println(io.getMessage());
		} catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		}
		return app;
	}

	

}
