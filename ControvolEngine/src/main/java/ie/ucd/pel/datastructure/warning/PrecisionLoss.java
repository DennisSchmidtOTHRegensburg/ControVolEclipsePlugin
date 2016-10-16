package ie.ucd.pel.datastructure.warning;

import org.eclipse.core.resources.IMarker;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.RetypeAttribute;

public class PrecisionLoss extends TypeWarning {
	
	public PrecisionLoss(RetypeAttribute retype){
		super(retype);
	}

	public int getSeverity(){
		return IMarker.SEVERITY_WARNING;
	}
	
	@Override
	public MLocation getLocation() {
		return getOperation().getAttribute().getLocation();
	}

}
