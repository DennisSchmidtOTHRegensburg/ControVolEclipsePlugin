package ie.ucd.pel.engine.astvisitor;

import ie.ucd.pel.engine.util.CstJava;
import ie.ucd.pel.engine.util.Util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * This visitor is used to extract a given class's super-class. 
 * @author Thomas
 *
 */
public class ASTVisitorSuperClass extends ASTVisitor {
	 
	protected Map<String, String> imports = new HashMap<String, String>();
	protected String packageName = null;
	protected String className = null;
	protected String superClassName = null;
	
	public String getSuperClassName() {
		return superClassName;
	}

	public ASTVisitorSuperClass(CompilationUnit cu){
		super();
	}

	public boolean visit(PackageDeclaration node){
		this.packageName = node.getName().toString();
		return true;
	}

	// Visit class
	public boolean visit(TypeDeclaration node){
		this.className = node.getName().toString();
		if (node.getSuperclassType() != null){
			Type typeSuperClass = node.getSuperclassType();
			String superClassFullName = getFullName(typeSuperClass.toString());
			if (superClassFullName.startsWith(this.packageName) || imports.containsValue(superClassFullName)){
				this.superClassName = superClassFullName;
			}
			
		}
		return true;
	}

	// Visit import
	public boolean visit(ImportDeclaration node){
		String fullTypeName = node.getName().getFullyQualifiedName();
		// FIXME Dirty code...
		String typeName = fullTypeName.substring(fullTypeName.lastIndexOf(".")+1, fullTypeName.length());
		this.imports.put(typeName, fullTypeName);
		return true;
	} 

	private String getFullName(String typeName){
		String fullTypeName = typeName;
		if (this.imports.containsKey(typeName)){
			fullTypeName = imports.get(typeName);
		} else if ((!Util.isJavaPrimitiveType(typeName)) && (!Util.isJavaPrimitiveTypeObject(typeName))){
			fullTypeName = this.packageName + "." + typeName;
		} else {
			// FIXME The conversion made here could be confusing... (int => java.lang.Integer)
			typeName = Util.primitiveTypeToPrimitiveTypeObject(typeName);
			fullTypeName = CstJava.JAVA_LANG_PACKAGE + typeName;
		}
		return fullTypeName;
	}

}
