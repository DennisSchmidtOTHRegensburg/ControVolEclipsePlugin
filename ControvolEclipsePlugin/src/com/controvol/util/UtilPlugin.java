package com.controvol.util;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.engine.crawler.CrawlerXml;
import ie.ucd.pel.engine.crawler.ICrawler;
import ie.ucd.pel.engine.util.CstJava;
import ie.ucd.pel.engine.util.Util;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.controvol.crawler.MyLegacyList;

public class UtilPlugin {

	public static String getResourceFolderName(IResource resrc){
		IProject project =  resrc.getProject();
		IJavaProject javaProject = JavaCore.create(project);  
		String srcFolderPath = null;
		
			IClasspathEntry[] srcFolders = null;
			try {
				srcFolders = javaProject.getResolvedClasspath(false);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
			 if(srcFolders[0].getContentKind() == IPackageFragmentRoot.K_SOURCE){
	                IPath path = srcFolders[0].getPath();
	                srcFolderPath = path.segments()[path.segmentCount()-1] +CstJava.UNIX_SEPERATOR;
			 }
			 String srcFolderPathWin=Util.windowsToUnixPath(srcFolderPath);
			 String folderName = srcFolderPathWin.replace(CstJava.UNIX_SEPERATOR, CstJava.PACKAGE_SEPERATOR);
			 return folderName;
	}

	public static String getResourceName(IResource resrc){
		String relativePath = resrc.getProjectRelativePath().toString();
		String relativePathWin=Util.windowsToUnixPath(relativePath);
		String folderName = getResourceFolderName(resrc);
		return UtilPlugin.getResourceName(relativePathWin, folderName);
	}
	
	public static String getResourceName(String relativePath, String folderName){
		String nameResrc = "";
		nameResrc = relativePath.replace(CstJava.UNIX_SEPERATOR, CstJava.PACKAGE_SEPERATOR);
		nameResrc = nameResrc.replaceFirst(folderName, "");
		nameResrc = nameResrc.substring(0, nameResrc.lastIndexOf(CstJava.PACKAGE_SEPERATOR + CstJava.JAVA_EXTENSION_1));
		return nameResrc;
	}	
	
	public static String getNextVersionNb(){
		Integer time =  new Integer((int) (System.currentTimeMillis() % Integer.MAX_VALUE));
		return String.valueOf(time);
	}
	
	public static String getLastVersionNb(String projectFullName){
		// XXX Costly but easy to write ;-)
		String versionNb = null;
		List<MApplication> appList = UtilPlugin.getAppList(projectFullName);
		versionNb = appList.get(appList.size() - 1).getVersion();
		return versionNb;
	}
	
	public static void exportToXml(MApplication app, IProject project){
		String version = UtilPlugin.getNextVersionNb();
		app.setVersion(version);
		IJavaProject javaProject = JavaCore.create(project);  

		String projectFolderPath = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation().toString() + CstJava.UNIX_SEPERATOR;	
		try {
			String projectFolderPathWin=Util.windowsToUnixPath(projectFolderPath);
			String fileName = projectFolderPathWin + CstPlugin.REP_PLUGIN + CstJava.UNIX_SEPERATOR + version + CstJava.DOT + CstJava.XML_EXTENSION;
			OutputStream os = new DataOutputStream(new FileOutputStream(fileName));
			app.toXml(os);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<MApplication> getAppList(String projectFullName){
		MyLegacyList appList = new MyLegacyList();
		projectFullName=Util.windowsToUnixPath(projectFullName);
		System.out.println(projectFullName + CstPlugin.REP_PLUGIN + CstJava.UNIX_SEPERATOR);
		List<String> legacyFiles  = appList.getLegacyXML(projectFullName + CstPlugin.REP_PLUGIN + CstJava.UNIX_SEPERATOR);
		ICrawler crawlerXml = new CrawlerXml();
		List<MApplication> apps = new ArrayList<MApplication>(legacyFiles.size());
		for (String file : legacyFiles){
			apps.add(crawlerXml.getApplication(file));
		}
		return apps;
	}
	
	/**
	 * 
	 * @param classNameWithPackage
	 * @return the class name without the package description. 
	 * For instance, "java.lang.String" will return "String" and 
	 * "String" will return "String"
	 */
	public static String getClassNameWithoutPackage(String classNameWithPackage){
		return classNameWithPackage.substring(classNameWithPackage.lastIndexOf(CstJava.DOT) + 1);
	}
}
