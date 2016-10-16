package ie.ucd.pel.datastructure.warning;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.RetypeAttribute;

import org.eclipse.core.resources.IMarker;

public class CastApproximation extends TypeWarning {
	
	public CastApproximation(RetypeAttribute retype){
		super(retype);
	}
	
	public int getSeverity(){
		return IMarker.SEVERITY_INFO;
	}
	
	@Override
	public MLocation getLocation() {
		return getOperation().getAttribute().getLocation();
	}

}
