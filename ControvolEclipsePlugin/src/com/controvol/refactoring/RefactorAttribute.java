package com.controvol.refactoring;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;
import ie.ucd.pel.datastructure.evolution.DeleteAttribute;
import ie.ucd.pel.datastructure.evolution.WildRenameAttribute;
import ie.ucd.pel.engine.crawler.CrawlerGit;
import ie.ucd.pel.engine.util.CstJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.text.edits.MultiTextEdit;


import com.controvol.typechecking.TypeChecking;
import com.controvol.util.UtilPlugin;

/**
 * This class detects when an attribute is renamed and triggers the ControVol engine. 
 * It is needed to do that, as otherwise this change would be seen as 'delete' + 'add' instead of 'rename'. 
 * (In this case, the Builder is not enough.)
 * @author Thomas
 * 
 * This class creates a list of renameOperation-objects. Each Attribute renamed with the refactoring-tool of Eclipse is saved
 * as a renameOperation-object. This happens only if the attribute isn't already marked with a @AlsoLoad annotation and was saved in the data store.
 * @author Dennis
 *
 */
public class RefactorAttribute extends org.eclipse.ltk.core.refactoring.participants.RenameParticipant {
	
	protected IField field;
	protected String newName;
	protected String oldName;
	protected String entityName;
	List<String> annotation = new ArrayList<String>();
	protected int i=0;
	// This list should be emptied when the application is registered as a release (?)
	// Elements from this list can be removed when the @AlsoLoad annotation is added.
	public static Set<WildRenameAttribute> renamingOperations = new TreeSet<WildRenameAttribute>();

	public static Set<DeleteAttribute> deleteOperations = new TreeSet<DeleteAttribute>();

	protected boolean initialize(Object element) {
		this.field = (IField) element;
		this.oldName = this.field.getElementName();
		this.newName = this.getArguments().getNewName();
		//list of all annotation of the refactored attribute
		try {
			for(IAnnotation annot:this.field.getAnnotations())
			{ annotation.add(annot.getElementName());
			i++;
				}
				
		} catch (JavaModelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		IJavaElement fieldClass = this.field.getParent();
		try {
			IResource resrc = (IResource) fieldClass.getUnderlyingResource();

			this.entityName = UtilPlugin.getResourceName(resrc);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	public String getName() {
		return this.oldName;
	}

	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}
	
	public String getProjectName(){
		return getProject().getName();
	}
	
	private IProject getProject(){
		return TypeChecking.project;
	}
	
	@Override
	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		TextFileChange change=null;
		MApplication app ;
		//System.out.println("RESOURCE ON THE FLY: "+getResource().toString());
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(this.getProjectName());
		IJavaProject javaProject = JavaCore.create(project);  
		IJavaElement fieldClass = this.field.getParent();
		IFile fFile = (IFile) fieldClass.getUnderlyingResource();
		change = new TextFileChange(this.oldName, fFile);
		change.setEdit(new MultiTextEdit());
		TypeChecking.currentApplication = getCurrentApplication(this.field.getResource()); 
						
		// This get the entire legacy history from the staging
		List<MApplication> appHistory = UtilPlugin.getAppList(ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation().toString() + CstJava.UNIX_SEPERATOR);		
		appHistory.add(TypeChecking.currentApplication);
		
		// For every version check if the attribute was saved once
		for (int i = appHistory.size() ; i > 0 ; i--){
			app=appHistory.get(i-1);
			MEntity entity = app.getEntity(entityName);	
			app.toString();
			if (entity.containsAttribute(this.oldName)){
				MAttribute oldAttribute = entity.getAttribute(this.oldName);
				//is attribute already marked with @AlsoLoad
				if(!(annotation.contains("AlsoLoad"))){
					//was attribute saved once
					if(!(oldAttribute.isDeleted())){
						MAttribute newAttribute = new MAttribute(this.newName, oldAttribute.getType(), oldAttribute.getLocation()); // type and location didn't change
						RefactorAttribute.renamingOperations.add(new WildRenameAttribute(entity, newAttribute, oldAttribute, null, UtilPlugin.getLastVersionNb(TypeChecking.currentApplication.getLocation())));
					} 
				}
			}// if the attribute didn't exist in the current application model, it doesn't have to be updated
		}
	return change;
  } 
	
	public MApplication getCurrentApplication(IResource res) throws JavaModelException{
		IProject project = res.getProject();
		IJavaProject javaProject = JavaCore.create(project);  
		String projectFolderPath = null;
		String srcFolderPath = null;
		String binFolderPath = null;
		
		try {
			projectFolderPath = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation().toString() + CstJava.UNIX_SEPERATOR;
			binFolderPath = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getOutputLocation()).getLocation().toString() + CstJava.UNIX_SEPERATOR;
			IClasspathEntry[] srcFolders = javaProject.getResolvedClasspath(false);
			 if(srcFolders[0].getContentKind() == IPackageFragmentRoot.K_SOURCE){
	                IPath path = srcFolders[0].getPath();
	                srcFolderPath = path.segments()[path.segmentCount()-1] + CstJava.UNIX_SEPERATOR;
			 }
			
		} catch (JavaModelException e) {
			e.printStackTrace();
		}


		CrawlerGit c = new CrawlerGit(projectFolderPath, srcFolderPath, binFolderPath);
		return c.getApplication();
	}
}