package com.controvol.quickfix;

public class Fix {
	
	protected String message;
	protected Integer type; // MARKER_FIX_TYPE_INSERT  or MARKER_FIX_TYPE_REPLACE or MARKER_FIX_TYPE_DELETE
	protected String newName;
	protected String oldName;
	protected String codeToInsert;
	protected String codeToReplace;
	protected String codeToDelete;
	protected Boolean isRenamingOperation = false;
	
	public Fix(String message, Integer type, String newName, String oldName,
			String codeToInsert, String codeToReplace, String codeToDelete,
			Boolean isRenamingOperation) {
		super();
		this.message = message;
		this.type = type;
		this.newName = newName;
		this.oldName = oldName;
		this.codeToInsert = codeToInsert;
		this.codeToReplace = codeToReplace;
		this.codeToDelete = codeToDelete;
		this.isRenamingOperation = isRenamingOperation;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getCodeToInsert() {
		return codeToInsert;
	}
	
	public void setCodeToInsert(String codeToInsert) {
		this.codeToInsert = codeToInsert;
	}
	
	public String getCodeToReplace() {
		return codeToReplace;
	}
	
	public void setCodeToReplace(String codeToReplace) {
		this.codeToReplace = codeToReplace;
	}
	
	public String getCodeToDelete() {
		return codeToDelete;
	}
	
	public void setCodeToDelete(String codeToDelete) {
		this.codeToDelete = codeToDelete;
	}
	
	public Boolean isRenamingOperation() {
		return isRenamingOperation;
	}
	
	public void setIsRenamingOperation(Boolean isRenamingOperation) {
		this.isRenamingOperation = isRenamingOperation;
	}
	
}
