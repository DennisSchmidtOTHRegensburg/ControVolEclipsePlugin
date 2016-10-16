package ie.ucd.pel.datastructure.warning;

import org.eclipse.core.resources.IMarker;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.RetypeAttribute;

public class IncompatibleTypes extends TypeWarning {
	
	public IncompatibleTypes(RetypeAttribute retype){
		super(retype);
	}

	public int getSeverity(){
		return IMarker.SEVERITY_ERROR;
	}
	
	@Override
	public MLocation getLocation() {
		return getOperation().getAttribute().getLocation();
	}
	
}
