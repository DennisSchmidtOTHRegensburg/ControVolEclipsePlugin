package ie.ucd.pel.engine.astvisitor;

import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;

import ie.ucd.pel.datastructure.MAttribute;

public interface IASTVisitor  {


boolean visit(FieldDeclaration node,CompilationUnit cu,String packageName,String className, Map<String, String> imports,Set<MAttribute> attributes);

}
