package ie.ucd.pel.engine.crawler;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MEntity;
import ie.ucd.pel.engine.astvisitor.ASTVisitorSuperClass;
import ie.ucd.pel.engine.astvisitor.ASTVisitorAttribute;
import ie.ucd.pel.engine.util.CstMorphia;
import ie.ucd.pel.engine.util.CstObjectify;
import ie.ucd.pel.engine.util.Util;
import ie.ucd.pel.engine.util.CstJava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import accessgit.GTAccessFactory;
import accessgit.GTHistory;


/*
 * This class is used to extract the source code of the database-project and to save it in the MApplication-model.
 * It calls the ASTVisitorAttribute to extract the attributes of the database-project.
 * To extract the attributes it uses the delegation-pattern to differ between the annotations of Morphia and Objectify.
 * Also it converts a windows-path to a unix-path before using the URL-classLoader.
 * */
public class CrawlerGit implements ICrawler {

	public static URLClassLoader classLoader;

	private String projectLocation;
	private String srcFolder;
	private String binFolder;

	public CrawlerGit(String projectLocation, String srcFolder, String binFolder){
		this.projectLocation=Util.windowsToUnixPath(projectLocation);
		this.projectLocation=Util.completeUnixPath(this.projectLocation);
		this.srcFolder=Util.windowsToUnixPath(srcFolder);
		this.srcFolder=Util.completeUnixPath(this.srcFolder);
		this.binFolder=Util.windowsToUnixPath(binFolder);
		this.binFolder=Util.completeUnixPath(this.binFolder);
		init();
	}

	private void init(){
		File fBinFolder = new File(binFolder);
		try {
			URL urlRoot = fBinFolder.toURI().toURL();
			CrawlerGit.classLoader = URLClassLoader.newInstance(new URL[] { urlRoot });
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public MApplication getApplication(){
		return this.getApplication(null);
	}

	public MApplication getApplication(String revision){
		MApplication app = new MApplication(projectLocation, revision);

		Map<String, String> hierarchy = getHierarchy(revision);
		hierarchy = eliminateExternalClasses(hierarchy);

		Map<String, MEntity> classesEntities = new HashMap<String, MEntity>();

		File fSrcFolder = new File(projectLocation + srcFolder);

		Set<String> classesToProcess = new TreeSet<String>();
		classesToProcess.addAll(hierarchy.keySet());
		classesToProcess.addAll(hierarchy.values());
		List<String> sortedClasses = sortClasses(hierarchy);
		sortedClasses = eliminateEmptyClasses(sortedClasses);
		for (String currentClass : sortedClasses){
			File srcFile = new File(getFileNameFromClassName(projectLocation + srcFolder + currentClass));
			File srcFileRevision = srcFile;
			if (revision != null){
				srcFileRevision = this.getFile(srcFile, revision);
			}
			MEntity entity = getEntity(srcFileRevision, fSrcFolder, srcFolder, currentClass);
			if (hierarchy.containsKey(currentClass)){
				String superClassName = hierarchy.get(currentClass);
				if (!superClassName.equals("")){
					MEntity superEntity = classesEntities.get(superClassName);
					entity.addAttributes(superEntity.getAttributes());
				}
			}
			if (entity.isEntity()){
				app.addEntity(entity);
			}
			classesEntities.put(currentClass, entity);
		}
		return app;
	}

	private List<String> sortClasses(Map<String, String> hierarchy){
		List<String> sortedClasses = new ArrayList<String>();
		Set<String> classesToProcess = new TreeSet<String>();
		classesToProcess.addAll(hierarchy.keySet());
		classesToProcess.addAll(hierarchy.values());
		while (sortedClasses.size() != classesToProcess.size()){
			for (String currentClass : classesToProcess){
				if (!sortedClasses.contains(currentClass)){
					if (hierarchy.containsKey(currentClass)){
						String superClassName = hierarchy.get(currentClass);
						if (sortedClasses.contains(superClassName)){
							sortedClasses.add(currentClass);
						}
					} else {
						sortedClasses.add(currentClass);
					}
				}
			}

		}
		return sortedClasses;
	}

	private List<String> eliminateEmptyClasses(List<String> classes){
		List<String> res = new ArrayList<String>();
		for (String clas : classes){
			if (!clas.equals("")){
				res.add(clas);
			}
		}
		return res;
	}

	private MEntity getEntity(File srcFile, File srcFolder2, String srcFolder, String className){
		MEntity entity = new MEntity(className, srcFolder);
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String sourceCode = CrawlerGit.extractSourceCode(srcFile);
		
		try {
			Class<?> clas = Class.forName(className, false, CrawlerGit.classLoader);

			Boolean isObjEntity = isObjectify(sourceCode);;
			Boolean isMophEntity = isMorphia(sourceCode);
			Boolean isAbstract = Modifier.isAbstract(clas.getModifiers());
			Boolean isInterface = clas.isInterface();
			Boolean isEnum = clas.isEnum();
			
			if (!isInterface && !isEnum){
				parser.setSource(sourceCode.toCharArray());
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
				entity.setIsObjEntity(false);
				entity.setIsMophEntity(false);
				ASTVisitorAttribute visitor;
				
				if(isObjEntity){
					visitor = new ASTVisitorAttribute(cu, srcFolder2.getAbsolutePath(),"Objectify");
					}
				else if(isMophEntity){
					visitor = new ASTVisitorAttribute(cu, srcFolder2.getAbsolutePath(),"Morphia");
				}
				else {
					visitor = new ASTVisitorAttribute(cu, srcFolder2.getAbsolutePath(),"Objectify");
				}

				cu.accept(visitor);
				entity = new MEntity(clas.getName(), srcFolder);
				entity.addAttributes(visitor.getAttributes());
				entity.setSuperEntity(clas.getSuperclass().getName());
				entity.setIsObjEntity((isObjEntity&&(!isAbstract)));
				entity.setIsMophEntity((isMophEntity&&(!isAbstract)));
				
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return entity;
	}

	public Map<String, String> getHierarchy(String revision){
		Map<String, String> hierarchy = new HashMap<String, String>();
		File fSrcFolder = new File(projectLocation + srcFolder);
		if (fSrcFolder.isDirectory()){
			Set<File> srcFiles = getFiles(fSrcFolder);
			for (File srcFile : srcFiles){
				String className = this.getClassNameFromFile(srcFile);
				File srcFileRevision = srcFile;
				if (revision != null){
					srcFileRevision = this.getFile(srcFile, revision);
				}
				String superClassName = getSuperClassName(srcFileRevision, fSrcFolder, className); 
				if (superClassName == null){
					superClassName = "";
				}
				hierarchy.put(className, superClassName);
			}
		}
		return hierarchy;
	}

	private Map<String, String> eliminateExternalClasses(Map<String, String> hierarchy){
		Map<String, String> newHierarchie = new HashMap<String, String>();
		for (String className : hierarchy.keySet()){
			Boolean keepClassName = true;
			List<String> superClasses = getSuperClasses(className, hierarchy);
			for (String superClassName : superClasses){
				keepClassName = keepClassName && isInProject(superClassName);
			}
			if (keepClassName){
				newHierarchie.put(className, hierarchy.get(className));
			}
		}
		return newHierarchie;
	}

	public Boolean isInProject(String className){
		File f = new File(projectLocation + srcFolder + getFileNameFromClassName(className));
		return f.exists();
	}

	private List<String> getSuperClasses(String className, Map<String, String> hierarchy){
		List<String> superClasses = new ArrayList<String>();
		String key = className;
		while (hierarchy.containsKey(key) && !hierarchy.get(key).equals("")){
			superClasses.add(hierarchy.get(key));
			key = hierarchy.get(key);
		}
		return superClasses;
	}

	private String getSuperClassName(File srcFile, File srcFolder2, String className){
		String superClassName = null;
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		String sourceCode = CrawlerGit.extractSourceCode(srcFile);
		parser.setSource(sourceCode.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		ASTVisitorSuperClass cvVisitor = new ASTVisitorSuperClass(cu);
		cu.accept(cvVisitor);
		superClassName = cvVisitor.getSuperClassName(); 
		return superClassName;
	}

	private Set<File> getFiles(File root) {
		Set<File> files = new TreeSet<File>();
		File[] list = root.listFiles();
		if (list != null){
			for (File f : list) {
				if (f.isDirectory()) {
					files.addAll(getFiles(f));
				} else if (f.getName().endsWith(CstJava.JAVA_EXTENSION_2)){
					files.add(f);
				}
			}
		}
		return files;
	}

	private static String extractSourceCode(File file) {
		String sourceCode = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				sourceCode += line + "\n";
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sourceCode;
	}
	
	private  Boolean isObjectify(String sourceCode){
		Boolean condEntity = sourceCode.contains(CstObjectify.ANNOTATION_ENTITY_1);
		Boolean condobej = sourceCode.contains(CstObjectify.ANNOTATION_ENTITY_2);
		Boolean condImportObj = sourceCode.contains(CstObjectify.IMPORT_ENTITY);
		Boolean isObjEntity = (condEntity && condImportObj||condobej);
		return isObjEntity;
	}
	
	private  Boolean isMorphia(String sourceCode){
		Boolean condEntity_1 = sourceCode.contains(CstObjectify.ANNOTATION_ENTITY_1);
		Boolean condEntity_2 = sourceCode.contains(CstMorphia.ANNOTATION_ENTITY_2);
		Boolean condEmbedded_1=sourceCode.contains(CstMorphia.ANNOTATION_EMBEDDED_1);
		Boolean condEmbedded_2=sourceCode.contains(CstMorphia.ANNOTATION_EMBEDDED_2);
		Boolean condImportEmbedded=sourceCode.contains(CstMorphia.IMPORT_EMBEDDED);
		Boolean condImportMoph = sourceCode.contains(CstMorphia.IMPORT_ENTITY);
		Boolean isMophEntity = (((condEntity_1 && condImportMoph)||(condEntity_2)||(condEmbedded_2)||(condEmbedded_1&&condImportEmbedded)));
		return isMophEntity;
	}

	public File getFile(File file, String revision){
		File currentFile = file;
		Calendar limit = this.getRevisionTime(revision);
		try {
			GTHistory history = GTAccessFactory.getHome().getGitHistoryOfFile(file);
			Integer cnt = 0;
			Calendar currentTime = history.getHistoryFiles().get(cnt).getCommitDate();
			while ((currentTime.compareTo(limit) > 0) && (cnt < history.getHistoryFiles().size())){
				currentFile = history.getHistoryFiles().get(cnt).getFile();
				currentTime = history.getHistoryFiles().get(cnt).getCommitDate();
				cnt++;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return currentFile;
	}

	private String getClassNameFromFile(File file){
		String fileString=Util.windowsToUnixPath(file.getAbsolutePath());
		return fileString.replace(projectLocation+this.srcFolder, "").replace(CstJava.UNIX_SEPERATOR, CstJava.PACKAGE_SEPERATOR).replace(CstJava.JAVA_EXTENSION_2, "");
	}

	private String getFileNameFromClassName(String className){
		return className.replace(CstJava.PACKAGE_SEPERATOR, CstJava.UNIX_SEPERATOR) + CstJava.JAVA_EXTENSION_2;
	}

	private Calendar getRevisionTime(String revision){
		Calendar t = Calendar.getInstance();
		File gitDir = new File(projectLocation);
		try {
			Git git = Git.open(gitDir);
			Repository repo = git.getRepository();
			ObjectId commitId = repo.resolve(revision);
			RevWalk walk = new RevWalk(repo);
			RevCommit commit = walk.parseCommit(commitId);
			t.setTime(commit.getCommitterIdent().getWhen());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}

}