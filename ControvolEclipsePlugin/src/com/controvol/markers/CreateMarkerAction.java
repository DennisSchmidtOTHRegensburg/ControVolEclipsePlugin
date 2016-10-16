package com.controvol.markers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;

public class CreateMarkerAction implements IEditorActionDelegate {

	public CreateMarkerAction() {
		super();
	}
	
	@Override
	public void setActiveEditor(IAction action, IEditorPart editor) {
	}

	/*
	 * This action creates a new marker for the given IFile 
	 */
	@Override
	public void run(IAction action) {}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}

}
