package com.controvol.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class ConfigurationAction implements IWorkbenchWindowActionDelegate {
	IWorkbenchWindow activeWindow = null;

	/** Run the action. Display the Hello World message
	 */		
	public void run(IAction proxyAction) {
		// proxyAction has UI information from manifest file (ignored)
		Shell shell = activeWindow.getShell();
		String msg1 = "Configuration of ControVol";
		String msg2 = "Here we should offer a dialog box to allow users to configure the plugin: \n";
		msg2 += "- Determine the projects that are considered by the plugin\n";
		msg2 += "- For each project: Map commits with versions";
		MessageDialog.openInformation(shell, msg1, msg2);
	}

	// IActionDelegate method
	public void selectionChanged(IAction proxyAction, ISelection selection) {
		// do nothing, action is not dependent on the selection
	}
	
	// IWorkbenchWindowActionDelegate method
	public void init(IWorkbenchWindow window) {
		activeWindow = window;
	}
	
	// IWorkbenchWindowActionDelegate method
	public void dispose() {
		//  nothing to do
	}
}