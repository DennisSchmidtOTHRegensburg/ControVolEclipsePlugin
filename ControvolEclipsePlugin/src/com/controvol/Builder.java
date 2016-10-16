package com.controvol;

import ie.ucd.pel.datastructure.evolution.Operation;
import ie.ucd.pel.engine.crawler.CrawlerGit;
import ie.ucd.pel.engine.util.CstJava;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.controvol.typechecking.TypeChecking;


/* Set the current application model each time the Project is build and then trigger the typechecking-process with visitor-classes */

public class Builder extends IncrementalProjectBuilder { 

	protected Set<Operation> errors = new TreeSet<Operation>();

	@SuppressWarnings("rawtypes")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws JavaModelException {
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(this.getProjectName());
		IJavaProject javaProject = JavaCore.create(project); 
		IClasspathEntry[] srcFolders = javaProject.getResolvedClasspath(false);
        IPath path = srcFolders[0].getPath();
        String srcFolderPath = path.segments()[path.segmentCount()-1] + CstJava.UNIX_SEPERATOR;
		 
		String binFolderPath = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getOutputLocation()).getLocation().toString() + CstJava.UNIX_SEPERATOR;
		String projectFolderPath = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation().toString() + CstJava.UNIX_SEPERATOR;

		CrawlerGit crawlerGit = new CrawlerGit(projectFolderPath, srcFolderPath, binFolderPath); 

		TypeChecking.currentApplication = crawlerGit.getApplication();
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			fullBuild();
		} else {
			IResourceDelta delta = getDelta(getProject());		
			if ((delta == null) || (TypeChecking.currentApplication == null)) {
				fullBuild();
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}   

	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
		MyResourceDeltaVisitor visitor = new MyResourceDeltaVisitor();
		try {
			delta.accept(visitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void fullBuild(){
		MyResourceVisitor visitor = new MyResourceVisitor();
		try {
			getProject().accept(visitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private String getProjectName(){
		return getProject().getName();
	}
	
} 