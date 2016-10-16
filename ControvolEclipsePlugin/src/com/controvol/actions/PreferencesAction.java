package com.controvol.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

import com.controvol.util.ControVolSetup;

public class PreferencesAction extends ActionDelegate {
	
	/**
	 * This action creates the Controvol folder (Staging area) for us to drop the XML schema
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action){
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		System.out.println("Shell: " + shell);
		MessageDialog dialog = new MessageDialog(
				shell, 
				"ControVol", null, 
				"Would you like to create the ControVol setup folder?", 
				MessageDialog.ERROR, new String[] { "No", "Yes"}, 0);
		int result = dialog.open();
		if (result == 1){ 
			ControVolSetup setup = new ControVolSetup(ControVolSetup.getCurrentProject());
			setup.getControVolFolder();
		}
	}

	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
	}
}
