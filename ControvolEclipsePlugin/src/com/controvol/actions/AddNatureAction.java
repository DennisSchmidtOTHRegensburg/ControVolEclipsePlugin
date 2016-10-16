package com.controvol.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

public class AddNatureAction extends NatureAction {
	
	@Override
	public void run(IAction action) {
		try {
			nature.configure();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
}
