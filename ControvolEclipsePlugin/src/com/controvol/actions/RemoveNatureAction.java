package com.controvol.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

public class RemoveNatureAction extends NatureAction {

	@Override
	public void run(IAction action) {
		try {
			nature.deconfigure();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
}
