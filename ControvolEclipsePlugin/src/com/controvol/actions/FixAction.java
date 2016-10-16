package com.controvol.actions;

import ie.ucd.pel.engine.crawler.CrawlerGit;
import ie.ucd.pel.engine.util.CstJava;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

import com.controvol.typechecking.TypeChecking;
import com.controvol.util.ControVolSetup;
import com.controvol.util.CstPlugin;
import com.controvol.util.UtilPlugin;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class FixAction extends ActionDelegate {

	/**
	 * The constructor.
	 */
	public FixAction() {}

	/**
	 * This action spits the XML for the current version in the data dictionary
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		MessageDialog dialog = new MessageDialog(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
				CstPlugin.CONTROVOL, 
				null, 
				CstPlugin.MSG_QUESTION_REGISTER, 
				MessageDialog.QUESTION, 
				new String[] {CstPlugin.MSG_NO, CstPlugin.MSG_YES}, 1);
		int result = dialog.open();

		if (result == 1){ 
			IProject project = ControVolSetup.getCurrentProject();
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

			if (TypeChecking.currentApplication == null){
				CrawlerGit crawler = new CrawlerGit(projectFolderPath, srcFolderPath, binFolderPath);
				TypeChecking.currentApplication = crawler.getApplication();
			} 
			UtilPlugin.exportToXml(TypeChecking.currentApplication, project);
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {}	

}