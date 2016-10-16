package testing;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;
import ie.ucd.pel.engine.astvisitor.ASTVisitorAttribute;
import ie.ucd.pel.engine.crawler.CrawlerGit;

public class Testing {
	private static final String WINDOWSCOMPLETE="C:\\Users\\pc\\runtime-EclipseApplication\\MorphiaTestProject\\";
	private static final String UNIXCOMPLETE="C:/Users/pc/runtime-EclipseApplication/MorphiaTestProject/";
	private static final String PROPERTY = "@Property(\"";
	private static final String ANNOTEND ="\")";
	private static final String ALSOLOAD ="@AlsoLoad(\"";
	private static final String NOTSAVED ="@NotSaved";
	private static final String TRANSIENT ="@Tansient";
	private static final String IGNORE ="@Ignore";
	private static final String IGNORESAVE="@IgnoreSave";
	private static final String IGNORELOAD="@IgnoreLoad";
	private static final String TYPE_STRING="String";
	private static final String SEMICOLON =";";
	private static final String SPACE=" ";
	
	
	private static String makeMorphiaSourceCode(){	
		return ("class Test { " +
				PROPERTY  + "name1" + ANNOTEND + TYPE_STRING + SPACE + "att1" + SEMICOLON +
				ALSOLOAD  + "name2" + ANNOTEND + TYPE_STRING + SPACE + "att2" + SEMICOLON +
				TRANSIENT +                      TYPE_STRING + SPACE + "att3" + SEMICOLON +
				NOTSAVED  + 					 TYPE_STRING + SPACE + "att4" + SEMICOLON +
								 			     TYPE_STRING + SPACE + "att5" + SEMICOLON +
				"}" );
		}
	private static String makeObjectifySourceCode(){	
		return ("class Test { " +
				
				ALSOLOAD  	+ "name1" + ANNOTEND + TYPE_STRING + SPACE + "att1" + SEMICOLON +
				IGNORE		+                      TYPE_STRING + SPACE + "att2" + SEMICOLON +
				IGNORESAVE  + 					   TYPE_STRING + SPACE + "att3" + SEMICOLON +
				IGNORELOAD  + 					   TYPE_STRING + SPACE + "att4" + SEMICOLON +
								 			       TYPE_STRING + SPACE + "att5" + SEMICOLON +
				"}" );
		}
	public static void main(String[] args) {
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String sourceCode=makeMorphiaSourceCode();
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
	ASTVisitorAttribute visitor = new ASTVisitorAttribute(cu,WINDOWSCOMPLETE+"src\\","Morphia");
	cu.accept(visitor);
	int i6=0;
	String [] feld6 = new String [5];
	for (MAttribute att : visitor.getAttributes()){
		feld6[i6]=att.getName();
		i6++;
	}
	
System.out.println(feld6[0]);
System.out.println(feld6[1]);
System.out.println(feld6[2]);
System.out.println(feld6[3]);

	
	
	
	}
	
}