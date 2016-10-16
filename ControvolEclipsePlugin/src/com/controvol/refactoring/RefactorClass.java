package com.controvol.refactoring;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IType;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;

public class RefactorClass extends org.eclipse.ltk.core.refactoring.participants.RenameParticipant {

	IType type;

	protected boolean initialize(Object element) {
		type = (IType) element;
		return true;
	}

	public String getName() {
		return "Sangam Rename Participant";
	}

	public RefactoringStatus checkConditions(IProgressMonitor pm, CheckConditionsContext context) throws OperationCanceledException {
		return new RefactoringStatus();
	}

	public Change createChange(IProgressMonitor pm) throws CoreException, OperationCanceledException {
		Change change = new NullChange();
		System.out.println("Class renamed.");
		return change;
	}

}