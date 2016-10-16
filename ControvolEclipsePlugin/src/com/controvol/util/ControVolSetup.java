package com.controvol.util;


import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import ie.ucd.pel.engine.util.CstJava;


public class ControVolSetup {

	private File file;
	private IProject project;

	public ControVolSetup(IResource res){
		this.project = res.getProject();	
	}

	public File getControVolFolder(){		
		file = new File(this.getProjectFolder() + "controvol");
		if (!file.exists()) {
			if (!file.mkdirs()) {
				System.out.println("Folder is NOT created!");
			}
		}
		return file;
	}


	/**
	 * Code from external resource
	 * @return
	 */
	public static IProject getCurrentProject(){
		IProject project = null;
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null){
			IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
			if (window != null){
				ISelectionService selectionService = window.getSelectionService(); 
				if (selectionService != null){
					ISelection selection = selectionService.getSelection();
					if (selection instanceof IStructuredSelection) {
						Object element = ((IStructuredSelection)selection).getFirstElement();    
						if (element instanceof IResource) {  
							project = ((IResource)element).getProject();
						} else if (element instanceof PackageFragmentRootContainer) {    
							IJavaProject jProject = ((PackageFragmentRootContainer)element).getJavaProject();    
							project = jProject.getProject();    
						} else if (element instanceof IJavaElement) {    
							IJavaProject jProject= ((IJavaElement)element).getJavaProject();    
							project = jProject.getProject();    
						}    
					}
				}
			}
		}
		return project;
	}



	public  String getProjectFolder(){
		IJavaProject javaProject = JavaCore.create(project); 
		return ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation().toString() + CstJava.UNIX_SEPERATOR;

	}

}
