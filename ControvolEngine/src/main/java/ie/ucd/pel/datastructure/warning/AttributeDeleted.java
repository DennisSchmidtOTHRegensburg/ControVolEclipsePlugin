package ie.ucd.pel.datastructure.warning;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.DeleteAttribute;

import org.eclipse.core.resources.IMarker;

public class AttributeDeleted extends Warning<DeleteAttribute> {
	
	public AttributeDeleted(DeleteAttribute del){
		super(del);
	}

	public int getSeverity(){
		return IMarker.SEVERITY_WARNING;
	}
	
	@Override
	public MLocation getLocation() {
		return getOperation().getAttribute().getLocation();
	}

}
