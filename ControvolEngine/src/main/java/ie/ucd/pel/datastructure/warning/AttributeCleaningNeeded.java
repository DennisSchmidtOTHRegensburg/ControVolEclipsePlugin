package ie.ucd.pel.datastructure.warning;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.AddAttribute;

import org.eclipse.core.resources.IMarker;

public class AttributeCleaningNeeded extends Warning<AddAttribute> {
	
	public AttributeCleaningNeeded(AddAttribute add){
		super(add);
	}

	public int getSeverity(){
		return IMarker.SEVERITY_WARNING;
	}

	@Override
	public MLocation getLocation() {
		return getOperation().getAttribute().getLocation();
	}

}
