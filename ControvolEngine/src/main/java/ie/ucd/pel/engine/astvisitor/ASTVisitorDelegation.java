package ie.ucd.pel.engine.astvisitor;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import ie.ucd.pel.datastructure.MAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
public class ASTVisitorDelegation {
	
	private String approach;
	private ASTVisitorLookup lookup=new ASTVisitorLookup();
	private IASTVisitor ControVolASTVisitor;
	private CompilationUnit cu;
	private String packageName;
	private String className;
	protected Map<String, String> imports = new HashMap<String, String>();
	protected Set<MAttribute> attributes = new TreeSet<MAttribute>();
	
	public void setTypeVisitor(String approach){
		this.approach=approach;
	}
	
	public void setUnit(CompilationUnit cu){
		this.cu=cu;
	}
	
	public void setPackageName(String packageName){
		this.packageName=packageName;
	}
	
	public void setCurrentClassName(String className){
	this.className=className;
	}
	
	public void setImports(Map<String, String> imports){
		this.imports=imports;
	}
	
	public void setAttributes(Set<MAttribute> attributes){
		this.attributes=attributes;
	}
	
	public Map<String, String> getImports() {
		return imports;
	}
	
	public Set<MAttribute> getAttributes() {
		return attributes;
	}
	
	public boolean visit(FieldDeclaration node){
		ControVolASTVisitor=lookup.getTypeVisitor(approach);
		return ControVolASTVisitor.visit(node,cu,packageName,className,imports,attributes);
	 }
}
