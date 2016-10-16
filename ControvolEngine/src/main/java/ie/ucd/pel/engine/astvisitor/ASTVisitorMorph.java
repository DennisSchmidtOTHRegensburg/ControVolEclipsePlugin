package ie.ucd.pel.engine.astvisitor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.engine.util.CstJava;
import ie.ucd.pel.engine.util.CstMorphia;
import ie.ucd.pel.engine.util.Util;
/*
* Extract the attribute regarding Morphia annotations.
*/
public class ASTVisitorMorph implements IASTVisitor{
	@Override
	public boolean visit(FieldDeclaration node, CompilationUnit cu,String packageName,String currentClassName,Map<String, String> imports,Set<MAttribute> attributes) {
		String nodeStr = node.toString();
		String[] legacyNames = {};
		if (nodeStr.startsWith(CstMorphia.ANNOTATION_ALSOLOAD_1)){
			String legacyNamesAux = nodeStr.substring(nodeStr.indexOf("(\"")+2, nodeStr.indexOf("\")")); 
			legacyNames = legacyNamesAux.split(",");
			for (int i = 0 ; i < legacyNames.length ; i++){
				legacyNames[i] = legacyNames[i].replace(" ", "");
			}
		}
		String[] realNames={};
		if (nodeStr.startsWith(CstMorphia.ANNOTATION_PROPERTY_1)){
			String realName=nodeStr.substring(nodeStr.indexOf("(\"")+2, nodeStr.indexOf("\")")); 
			realNames = realName.split(",");
			for (int i = 0 ; i < realNames.length ; i++){
				realNames[i] = realNames[i].replace(" ", "");
			}
		}
		Type typeAux = node.getType();
		String access = getAccess(node);
		int lineNumber = cu.getLineNumber(node.getStartPosition());
		MLocation location = new MLocation(packageName + "." + currentClassName, lineNumber);
		
		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> fragments = node.fragments();
		for (VariableDeclarationFragment fragment : fragments){
			String name = fragment.getName().toString();
			String className = getFullName(typeAux.toString(),packageName,imports);
			for(int i=0;i<realNames.length;i++){
				name=realNames[i];
			}
			MAttribute attribute = new MAttribute(name, className, location, access); 
			Boolean isDeleted = ((nodeStr.startsWith(CstMorphia.ANNOTATION_TRANSIENT_1))||(nodeStr.startsWith(CstMorphia.ANNOTATION_NOTSAVED_1)));
			if(nodeStr.startsWith(CstMorphia.ANNOTATION_TRANSIENT_1)){ 
				attribute.setIsLoaded(false);
			}
			attribute.setIsDeleted(isDeleted);
			for (int i = 0 ; i < legacyNames.length ; i++){
				attribute.addFormerName(legacyNames[i]);
			}
			attributes.add(attribute);
		}
		return false;
	}
	
	private String getAccess(FieldDeclaration node){
		String access = CstJava.ACCESS_PACKAGE;
		List<?> modifiers = node.modifiers();
		for (Object modifierAux : modifiers){
			if (modifierAux instanceof Modifier){
				Modifier modifier = (Modifier) modifierAux;
				if (modifier.isPublic()){
					access = CstJava.ACCESS_PUBLIC;
				} else if (modifier.isProtected()){
					access = CstJava.ACCESS_PROTECTED;
				} else if (modifier.isPrivate()){
					access = CstJava.ACCESS_PRIVATE;
				} 
			}
		}
		return access;
	}

	private String getFullName(String typeName,String packageName,Map<String, String> imports){
		String fullTypeName = typeName;
		if (imports.containsKey(typeName)){
			fullTypeName = imports.get(typeName);
		} else if ((!Util.isJavaPrimitiveType(typeName)) && (!Util.isJavaPrimitiveTypeObject(typeName))){
			fullTypeName = packageName + "." + typeName;
		} else {
			// FIXME The conversion made here could be confusing... (int => java.lang.Integer)
			typeName = Util.primitiveTypeToPrimitiveTypeObject(typeName);
			fullTypeName = CstJava.JAVA_LANG_PACKAGE + typeName;
		}
		return fullTypeName;
	}
}
