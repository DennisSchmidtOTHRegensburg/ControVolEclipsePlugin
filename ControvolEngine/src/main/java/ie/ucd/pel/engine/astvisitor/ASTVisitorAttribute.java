package ie.ucd.pel.engine.astvisitor;

import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.engine.util.CstJava;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

/**
 * This visitor is used to extract the attributes of a given class. 
 * @author Thomas
 * 
 * It uses the delegation-pattern to differ between Objectify and Morphia.
 * @author Dennis
 */
public class ASTVisitorAttribute extends ASTVisitor {

	protected CompilationUnit cu;

	protected Map<String, String> imports = new HashMap<String, String>();
	protected Set<MAttribute> attributes = new TreeSet<MAttribute>();
	protected String packageName = "";
	protected String currentClassName = "";
	private String typeVisitor="";

	public ASTVisitorAttribute(CompilationUnit cu, String srcFolderName,String typeVisitor){
		super();
		this.cu = cu;
		this.typeVisitor=typeVisitor;
	}

	public Set<MAttribute> getAttributes(){
		return this.attributes;
	}

	public boolean visit(PackageDeclaration node){
		this.packageName = node.getName().toString();
		return true;
	}

	// Visit import
	public boolean visit(ImportDeclaration node){
		String fullTypeName = node.getName().getFullyQualifiedName();
		String typeName = fullTypeName.substring(fullTypeName.lastIndexOf(CstJava.DOT)+1, fullTypeName.length());
		this.imports.put(typeName, fullTypeName);
		return true;
	} 

	public boolean visit(TypeDeclaration node){
		this.currentClassName = node.getName().toString();
		return true;
	}

	// Visit attributes
	public boolean visit(FieldDeclaration node) {
		ASTVisitorDelegation delegation=new ASTVisitorDelegation();
		delegation.setTypeVisitor(typeVisitor);
		delegation.setAttributes(attributes);
		delegation.setImports(imports);
		delegation.setCurrentClassName(currentClassName);
		delegation.setPackageName(packageName);
		delegation.setUnit(cu);
		delegation.visit(node);
		attributes=delegation.getAttributes();
		imports=delegation.getImports();
		return false;
	}

}
		
