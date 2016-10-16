package ie.ucd.pel.datastructure.warning;

import ie.ucd.pel.datastructure.MLocation;
import ie.ucd.pel.datastructure.evolution.Operation;

public interface IWarning extends Comparable<IWarning> {

	public Operation getOperation();
	
	public String getEntityLocation();
	
	public MLocation getLocation();
	
	public String getLegacyVersion();
	
	public String getCurrentVersion();
	
	public int getSeverity();
	
	public Boolean isRenamingOperation();
	
}
