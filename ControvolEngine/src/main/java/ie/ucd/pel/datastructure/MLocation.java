package ie.ucd.pel.datastructure;

public class MLocation {

	protected String className;
	protected Integer lineNumber;
	
	public MLocation(String className, Integer lineNumber) {
		super();
		this.className = className;
		this.lineNumber = lineNumber;
	}
	
	public MLocation(String className) {
		super();
		this.className = className;
		this.lineNumber = null;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
		
}
