package ie.ucd.pel.datastructure.warning;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.RenameAttribute;

import org.eclipse.core.resources.IMarker;

public class AttributeImproperlyRenamed extends Warning<RenameAttribute> {
	
	public AttributeImproperlyRenamed(RenameAttribute add){
		super(add);
	}

	public int getSeverity(){
		return IMarker.SEVERITY_WARNING;
	}
	
	@Override
	public MLocation getLocation() {
		return getOperation().getAttribute().getLocation();
	}
	
	@Override
	public Boolean isRenamingOperation(){
		return true;
	}

}
