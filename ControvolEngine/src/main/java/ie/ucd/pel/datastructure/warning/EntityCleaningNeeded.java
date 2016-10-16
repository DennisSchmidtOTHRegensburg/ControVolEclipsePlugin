package ie.ucd.pel.datastructure.warning;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.AddEntity;

import org.eclipse.core.resources.IMarker;

public class EntityCleaningNeeded extends Warning<AddEntity> {
	
	public EntityCleaningNeeded(AddEntity add){
		super(add);
	}

	public int getSeverity(){
		return IMarker.SEVERITY_WARNING;
	}

	@Override
	public MLocation getLocation() {
		return new MLocation(getEntityLocation());
	}

}
