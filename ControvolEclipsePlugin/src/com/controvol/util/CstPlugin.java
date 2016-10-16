package com.controvol.util;

import ie.ucd.pel.engine.util.CstJava;



public class CstPlugin {
	
	//Controvol
	public static final String REP_PLUGIN = "controvol";
	public static final String CONTROVOL = "ControVol";
	
	// CONSTANTS USED IN THE JAVA CODE AND IN plugin.xml
	public static final String PLUGIN_ID = "com.controvol";
	public static final String BUILDER_ID = PLUGIN_ID + CstJava.DOT + "builder";
	public static final String NATURE_ID = PLUGIN_ID + CstJava.DOT + "nature";
	public static final String ANNOTATION_ID = PLUGIN_ID + CstJava.DOT + "annotation";
	public static final String MARKER_ID = PLUGIN_ID + CstJava.DOT + "marker";
	public static final String MARKER_ICON_ID = "icons/sample.gif";
	public static final String MARKER_INSERT_CODE_FIX = "insertCodeFix";
	public static final String MARKER_REPLACE_CODE_FIX = "replaceCodeFix";
	public static final String MARKER_DELETE_CODE_FIX = "deleteCodeFix";
	public static final String MARKER_FIX_TYPE = "fixType"; // MARKER_FIX_TYPE_INSERT: insert, MARKER_FIX_TYPE_REPLACE: replace, MARKER_FIX_TYPE_DELETE: delete
	public static final Integer MARKER_FIX_TYPE_INSERT = 0; 
	public static final Integer MARKER_FIX_TYPE_REPLACE = 1; 
	public static final Integer MARKER_FIX_TYPE_DELETE = 2; 
	public static final String MARKER_DESCRIPTION = "description";
	public static final String MARKER_OLD_NAME_ID = "oldName";
	public static final String MARKER_NEW_NAME_ID = "newName";
	public static final String MARKER_IS_RENAMING = "isRenaming";
	public static final String MARKER_JSON_FIXES = "jsonFixes";
	public static final String RESTORE_MARKER_ID = PLUGIN_ID + CstJava.DOT + "restoreMarker";	

	// MESSAGES
	public static final String MSG_YES = "Yes";
	public static final String MSG_NO = "No";
	public static final String MSG_CHANGE = "Automatic ControVol change";
	public static final String MSG_QUESTION_REGISTER = "Register the current version of the application as a new release?";
	
	//CODEFIXES
	public static final String APOSTROPHE = "'";
	public static final String DOT =".";
	public static final String ADD_ANNO = "Add annotation ";
	public static final String REMOVE_ATT = " to explicitly remove attribute ";
	public static final String RESTORE_TYPE = "Restore type ";
	public static final String RESTORE_ATT =  "Restore attribute ";
	public static final String ADVANCED = " to be able to perform advanced migration with ";
	public static final String DATALOSS = " to prevent data loss";
	public static final String MIGRATION = "void migration";
	public static final String COMMEND_1 = "{/*";
	public static final String COMMEND_2 = "*/}";
	public static final String MIG_MESSAGE_1 = " Use this method to migrate from ";
	public static final String MIG_MESSAGE_2 = " to ";
	public static final String MIG_MESSAGE_3 = "New";
}
