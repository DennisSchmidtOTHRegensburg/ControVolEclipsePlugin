package com.controvol;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;

import com.controvol.typechecking.TypeChecking;

public class MyResourceVisitor implements IResourceVisitor {
	
	@Override
    public boolean visit(IResource res) {
		TypeChecking checking = new TypeChecking();
		checking.checking(res);
		return true;
    }

}
