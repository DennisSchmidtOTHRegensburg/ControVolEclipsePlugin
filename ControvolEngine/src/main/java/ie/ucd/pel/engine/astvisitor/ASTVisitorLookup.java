package ie.ucd.pel.engine.astvisitor;



public class ASTVisitorLookup {
	public IASTVisitor getTypeVisitor(String approach){
		
		if(approach.equalsIgnoreCase("Morphia")){
			return new ASTVisitorMorph();
		}
		
		else if(approach.equalsIgnoreCase("Objectify")){
			return new ASTVisitorObj();
		}
		
			else return new ASTVisitorObj();
	}
}
