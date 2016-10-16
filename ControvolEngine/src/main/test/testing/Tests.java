package testing;
	import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;


import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;
import ie.ucd.pel.datastructure.evolution.DeleteAttribute;
import ie.ucd.pel.datastructure.evolution.Evolution;
import ie.ucd.pel.datastructure.evolution.RenameAttribute;
import ie.ucd.pel.datastructure.evolution.RetypeAttribute;
import ie.ucd.pel.engine.ControVolEngine;
import ie.ucd.pel.engine.astvisitor.ASTVisitorAttribute;
import ie.ucd.pel.engine.crawler.CrawlerGit;
import ie.ucd.pel.engine.crawler.CrawlerXml;
import ie.ucd.pel.engine.crawler.ICrawler;

import org.junit.Test;

import ie.ucd.pel.engine.util.CstJava;
import ie.ucd.pel.engine.util.Util;

public class Tests {
	
	@Test
	public void MorphiaAnnotationTests(){
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String sourceCode= UtilTest.makeMorphiaSourceCode();
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ASTVisitorAttribute visitor = new ASTVisitorAttribute(cu,UtilTest.WINDOWSCOMPLETE+"src\\","Morphia");
		cu.accept(visitor);
		int i=0;
		String [] atts = new String [5];
		for (MAttribute att : visitor.getAttributes()){
			atts[i]=att.getName();
			i++;
		}
		assertEquals("Annotation AlsoLoad",	"att2" , atts[0]);
		assertEquals("Ohne Annotation"	  , "att5" , atts[1]);
		assertEquals("Annotation Property",	"rename1", atts[2]);
	}
	
	@Test
	public void ObjectifyAnnotationTests(){
		
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String sourceCode=UtilTest.makeObjectifySourceCode();
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ASTVisitorAttribute visitor = new ASTVisitorAttribute(cu,UtilTest.WINDOWSCOMPLETE+"src\\","Objectify");
		cu.accept(visitor);
		int i=0;
		String [] atts = new String [5];
		for (MAttribute att : visitor.getAttributes()){
			atts[i]=att.getName();
			i++;
		}
		assertEquals("Annotation AlsoLoad", "att1", atts[0]);
		assertEquals("Ohne Annotation",     "att5", atts[1]);
	
	}
	
	
	@Test
	public void CrawlerTestsWin(){
		CrawlerGit c = new CrawlerGit(UtilTest.WINDOWS, "src\\", UtilTest.WINDOWSCOMPLETE+"bin\\");
		MApplication testapp=c.getApplication();

		for (MEntity ent : testapp.getEntities()){
			assertEquals("GuestbookTest","persistance.Guestbook",ent.getEntityName());
			assertEquals("GetLocationTest","src/",ent.getLocation());
			String [] feld = new String [5];
			int i=0;
			
			for (MAttribute att : ent.getAttributes()){
				feld[i]=att.getName();
				i++;
			}
			
			assertEquals("att 1","id",feld[0]);
			assertEquals("att 2","text",feld[1]);
			assertEquals("att 3","titel",feld[2]);
			assertEquals("att 4","userName",feld[3]);
		}
		assertEquals("Windows to Unix Test",	UtilTest.UNIX,			Util.windowsToUnixPath(UtilTest.WINDOWS));
		assertEquals("Windows to Unix Test2",	UtilTest.UNIXCOMPLETE,	Util.windowsToUnixPath(UtilTest.WINDOWSCOMPLETE));
	}
	
	@Test
	public void CrawlertestUnix(){
		CrawlerGit c2 = new CrawlerGit(UtilTest.UNIX, "src/", UtilTest.UNIXCOMPLETE+"bin/");
		MApplication testapp2=c2.getApplication();

		for (MEntity ent : testapp2.getEntities()){
			assertEquals("Guestbookijijij","persistance.Guestbook",ent.getEntityName());
			assertEquals("getLocation","src/",ent.getLocation());
			String [] feld = new String [5];
			int i=0;
			
			for (MAttribute att : ent.getAttributes()){
				feld[i]=att.getName();
				i++;
				}
			
			assertEquals("feld","id",feld[0]);
			assertEquals("feld","text",feld[1]);
			assertEquals("feld","titel",feld[2]);
			assertEquals("feld","userName",feld[3]);
			}
	}
	
	
	@Test
	public void PathTests() {
		
		assertEquals("Unix Test",				UtilTest.UNIX,			Util.windowsToUnixPath(UtilTest.UNIX));
		assertEquals("Unix Test",				UtilTest.UNIXCOMPLETE,	Util.windowsToUnixPath(UtilTest.UNIXCOMPLETE));
		assertEquals("UnixComplete Test",		UtilTest.UNIXCOMPLETE,	Util.completeUnixPath(UtilTest.UNIX));
		assertEquals("Unix Complete Test2",		UtilTest.UNIXCOMPLETE,	Util.completeUnixPath(UtilTest.UNIXCOMPLETE));

	}

/*	@Test
	public void EvolutionTest(){
		ICrawler crawlerXml = new CrawlerXml();
		List<MApplication> apps = new ArrayList<MApplication>();	
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String sourceCode=UtilTest.makeObjectifySourceCode();
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ASTVisitorAttribute visitor = new ASTVisitorAttribute(cu,UtilTest.WINDOWSCOMPLETE+"src\\","Objectify");
		cu.accept(visitor);
		int i=0;
		MApplication app = new MApplication("-");
		
		app.addEntity(visitor.getAttributes());
		ControVolEngine eng = new ControVolEngine();
		Evolution evol = eng.getEvolution(apps);
		
		DeleteAttribute delete=new DeleteAttribute(,,,);
		RetypeAttribute retype=new RetypeAttribute(,,,);
		RenameAttribute rename=new RenameAttribute(,,,);
		
		assertEquals("Attribute deleted",true,evol.contains(delete));
		assertEquals("Attribute retyped",true,evol.contains(retype));
		assertEquals("Attribute renamed",true,evol.contains(rename));
	}*/
} 


