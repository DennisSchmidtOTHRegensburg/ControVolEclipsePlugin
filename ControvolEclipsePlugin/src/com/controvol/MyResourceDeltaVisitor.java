package com.controvol;

import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;

import com.controvol.typechecking.TypeChecking;

public class MyResourceDeltaVisitor implements IResourceDeltaVisitor {

	@Override
	public boolean visit(IResourceDelta delta) { 
    	TypeChecking checking = new TypeChecking();
    	checking.checking(delta.getResource());
		return true;
	}

}
