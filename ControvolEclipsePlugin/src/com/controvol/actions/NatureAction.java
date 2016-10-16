package com.controvol.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.controvol.natures.ControVolNature;

public abstract class NatureAction implements IWorkbenchWindowActionDelegate{

	protected ControVolNature nature = new ControVolNature();
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		IProject project = extractSelectionProject(selection);
		this.nature.setProject(project);
	}
	
	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}
	
	protected IProject extractSelectionProject(ISelection selection) {
		IProject project = null;
		if (selection instanceof IStructuredSelection){
			IStructuredSelection ss = (IStructuredSelection) selection;
			Object element = ss.getFirstElement();
			if (element instanceof IResource){
				project = (IProject) element;
			} else if (element instanceof IAdaptable){
				IAdaptable adaptable = (IAdaptable)element;
				Object adapter = adaptable.getAdapter(IProject.class);
				project = (IProject) adapter;
			}
		}
		return project;
	}
	
}
