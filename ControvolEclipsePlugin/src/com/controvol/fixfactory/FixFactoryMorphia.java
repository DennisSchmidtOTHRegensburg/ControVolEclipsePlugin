package com.controvol.fixfactory;

import ie.ucd.pel.datastructure.warning.AttributeDeleted;
import ie.ucd.pel.datastructure.warning.AttributeImproperlyRenamed;
import ie.ucd.pel.datastructure.warning.IWarning;
import ie.ucd.pel.datastructure.warning.TypeWarning;
import ie.ucd.pel.engine.util.CstJava;
import ie.ucd.pel.engine.util.CstMorphia;

import java.util.ArrayList;
import java.util.List;

import com.controvol.quickfix.Fix;
import com.controvol.util.CstPlugin;
import com.controvol.util.UtilPlugin;
/*
 * Create a list of fixes for Morphia for a given warning
 * */
public class FixFactoryMorphia implements FixFactory {
	
	public FixFactoryMorphia(){}
	
	public List<Fix> getCodeFixes(IWarning warning){
		List<Fix> fixes = new ArrayList<Fix>();
		String codeToInsert;
		String codeToReplace;
		String annotation;
		String message;
		Fix fix;
		String type;
		String access;
		if (warning instanceof AttributeDeleted){
			AttributeDeleted warningAttDel = (AttributeDeleted) warning;
			
			// Fix 1 for Deleted: Restore Attribute and Add @Transient
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_TRANSIENT_1;
			type = UtilPlugin.getClassNameWithoutPackage(warningAttDel.getOperation().getAttribute().getType());
			String currentName = warningAttDel.getOperation().getAttribute().getName();
			String formerName = currentName;
			type = UtilPlugin.getClassNameWithoutPackage(warningAttDel.getOperation().getAttribute().getType());
			access = warningAttDel.getOperation().getAttribute().getAccess();
			codeToInsert = CstJava.TAB + annotation + CstJava.LINE_BREAK;
			if (access.equals("")){
				codeToInsert += CstJava.TAB;
			} else {
				codeToInsert += CstJava.TAB + access + CstJava.SPACE;
			}
			codeToInsert += type + CstJava.SPACE + currentName + CstJava.SEMI_COLON + CstJava.LINE_BREAK; 
			message = CstPlugin.ADD_ANNO + annotation + CstPlugin.REMOVE_ATT + CstPlugin.APOSTROPHE + currentName + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, "", "", false);
			fixes.add(fix);
			
			// Fix 2 for Deleted: Restore Attribute and Add @NotSaved
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_NOTSAVED_1;
			type = UtilPlugin.getClassNameWithoutPackage(warningAttDel.getOperation().getAttribute().getType());
			currentName = warningAttDel.getOperation().getAttribute().getName();
			formerName = currentName;
			type = UtilPlugin.getClassNameWithoutPackage(warningAttDel.getOperation().getAttribute().getType());
			access = warningAttDel.getOperation().getAttribute().getAccess();
			codeToInsert = CstJava.TAB + annotation + CstJava.LINE_BREAK;
			if (access.equals("")){
			codeToInsert += CstJava.TAB;
			} else {
			codeToInsert += CstJava.TAB + access + CstJava.SPACE;
			}
			codeToInsert += type + CstJava.SPACE + currentName + CstJava.SEMI_COLON + CstJava.LINE_BREAK; 
			message = CstPlugin.ADD_ANNO + annotation + CstPlugin.ADVANCED + CstPlugin.APOSTROPHE + currentName + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, "", "", false);
			fixes.add(fix);
						
			// Fix 3 for Deleted: Restore Attribute
			type = UtilPlugin.getClassNameWithoutPackage(warningAttDel.getOperation().getAttribute().getType());
			access = warningAttDel.getOperation().getAttribute().getAccess();
			if (access.equals("")){
				codeToInsert = CstJava.TAB;
			} else {
				codeToInsert = CstJava.TAB + access + CstJava.SPACE;
			}
			codeToInsert += type + CstJava.SPACE + formerName + CstJava.SEMI_COLON + CstJava.LINE_BREAK;
			codeToReplace = type + CstJava.SPACE + currentName;  
			message = CstPlugin.RESTORE_ATT + CstPlugin.APOSTROPHE + formerName + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, "", "", false);
			fixes.add(fix);
			
		} else if (warning instanceof AttributeImproperlyRenamed){
			AttributeImproperlyRenamed warningAttRenamed = (AttributeImproperlyRenamed) warning;
			String currentName = warningAttRenamed.getOperation().getAttribute().getName();
			String formerName = warningAttRenamed.getOperation().getLegacyAttribute().getName();
			
			// Fix 1 for Renamed: Add @Property("fromerAttribute")
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_PROPERTY_1;
			codeToInsert = CstJava.TAB + annotation + CstJava.BRACKET_1 + CstJava.QUOTE + formerName + CstJava.QUOTE + CstJava.BRACKET_2 + CstJava.LINE_BREAK;
			codeToReplace = UtilPlugin.getClassNameWithoutPackage(warningAttRenamed.getOperation().getAttribute().getType()); 
			codeToReplace = codeToReplace + " " + warningAttRenamed.getOperation().getLegacyAttribute() + CstJava.SEMI_COLON;
			message = CstPlugin.ADD_ANNO + annotation + CstJava.BRACKET_1 + CstJava.QUOTE + formerName + CstJava.QUOTE + CstJava.BRACKET_2 + CstPlugin.DATALOSS + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, codeToReplace, "", true);
			fixes.add(fix);
			
			// Fix 2 for Renamed: Add @AlsoLoad("formerAttribute")
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_ALSOLOAD_1;
			codeToInsert = CstJava.TAB + annotation + CstJava.BRACKET_1 + CstJava.QUOTE + formerName + CstJava.QUOTE + CstJava.BRACKET_2 + CstJava.LINE_BREAK;
			codeToReplace = UtilPlugin.getClassNameWithoutPackage(warningAttRenamed.getOperation().getAttribute().getType()); 
			codeToReplace = codeToReplace + " " + warningAttRenamed.getOperation().getLegacyAttribute() + CstJava.SEMI_COLON;
			message = CstPlugin.ADD_ANNO + annotation + CstJava.BRACKET_1 + CstJava.QUOTE + formerName + CstJava.QUOTE + CstJava.BRACKET_2 + CstPlugin.DATALOSS + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, codeToReplace, "", true);
			fixes.add(fix);
			
			// Fix 3 for Renamed: Restore formerAttribute and Add @Transient
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_TRANSIENT_1;
			type = UtilPlugin.getClassNameWithoutPackage(warningAttRenamed.getOperation().getAttribute().getType());
			access = warningAttRenamed.getOperation().getAttribute().getAccess();
			codeToInsert = CstJava.TAB + annotation + CstJava.LINE_BREAK;
			if (access.equals("")){
				codeToInsert += CstJava.TAB;
			} else {
				codeToInsert += CstJava.TAB + access + CstJava.SPACE;
			}
			codeToInsert += type + CstJava.SPACE + formerName + CstJava.SEMI_COLON + CstJava.LINE_BREAK;
			codeToReplace = UtilPlugin.getClassNameWithoutPackage(warningAttRenamed.getOperation().getAttribute().getType()); 
			codeToReplace = codeToReplace + CstJava.SPACE + warningAttRenamed.getOperation().getAttribute() + CstJava.SEMI_COLON;
			message = CstPlugin.ADD_ANNO + annotation + CstPlugin.REMOVE_ATT + CstPlugin.APOSTROPHE + formerName + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, codeToReplace, "", true);
			fixes.add(fix);
			
			// Fix 4 for Renamed: Restore fromerAttribute and Add @NotSaved
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_NOTSAVED_1;
			type = UtilPlugin.getClassNameWithoutPackage(warningAttRenamed.getOperation().getAttribute().getType());
			access = warningAttRenamed.getOperation().getAttribute().getAccess();
			codeToInsert = CstJava.TAB + annotation + CstJava.LINE_BREAK;
			if (access.equals("")){
			codeToInsert += CstJava.TAB;
			} else {
			codeToInsert += CstJava.TAB + access + CstJava.SPACE;
			}
			codeToInsert += type + CstJava.SPACE + formerName + CstJava.SEMI_COLON + CstJava.LINE_BREAK;
			codeToReplace = UtilPlugin.getClassNameWithoutPackage(warningAttRenamed.getOperation().getAttribute().getType()); 
			codeToReplace = codeToReplace + CstJava.SPACE + warningAttRenamed.getOperation().getAttribute() + CstJava.SEMI_COLON;
			message =  CstPlugin.ADD_ANNO + annotation + CstPlugin.ADVANCED + CstPlugin.APOSTROPHE + formerName + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, codeToReplace, "", true);
			fixes.add(fix);
		
			// Fix 5 for Renamed: Restore Attribute
			type = UtilPlugin.getClassNameWithoutPackage(warningAttRenamed.getOperation().getAttribute().getType());
			codeToInsert = type + CstJava.SPACE + formerName;
			codeToReplace = type + CstJava.SPACE + currentName; 
			message = CstPlugin.RESTORE_ATT + CstPlugin.APOSTROPHE + formerName + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_REPLACE, currentName, formerName, codeToInsert, codeToReplace, "", true);
			fixes.add(fix);
			
		} else if (warning instanceof TypeWarning){
			
			// Fix 1 for TypeWarning: Restore Attribute
			TypeWarning typeWarning = (TypeWarning) warning;
			type = UtilPlugin.getClassNameWithoutPackage(typeWarning.getOperation().getAttribute().getType());
			String legacyType = UtilPlugin.getClassNameWithoutPackage(typeWarning.getOperation().getLegacyAttribute().getType());
			codeToInsert = legacyType; 
			codeToReplace = type;
			String currentName = typeWarning.getOperation().getAttribute().getName();
			String formerName = currentName;
			message = CstPlugin.RESTORE_TYPE + CstPlugin.APOSTROPHE + legacyType + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_REPLACE, currentName, formerName, codeToInsert, codeToReplace, "", false);
			fixes.add(fix);
			
			// Fix 2 for TypeWarning: Add @NotSaved + migration-method
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_NOTSAVED_1;
			String advancedmigration = CstMorphia.ANNOTATION_POSTLOAD_1 + CstJava.LINE_BREAK + CstJava.TAB + CstPlugin.MIGRATION + formerName + CstJava.BRACKET_1 + CstJava.BRACKET_2 + CstPlugin.COMMEND_1 + CstPlugin.MIG_MESSAGE_1 + currentName + CstPlugin.MIG_MESSAGE_2 + currentName + CstPlugin.MIG_MESSAGE_3 + CstPlugin.COMMEND_2;
			codeToInsert = advancedmigration + CstJava.LINE_BREAK + CstJava.TAB + type+CstJava.SPACE + currentName + "New" + CstJava.SEMI_COLON + CstJava.LINE_BREAK + CstJava.TAB + annotation + CstJava.LINE_BREAK + CstJava.TAB + legacyType; 
			codeToReplace = type;
			message = CstPlugin.ADD_ANNO + annotation + CstPlugin.ADVANCED + CstPlugin.APOSTROPHE + formerName + CstPlugin.APOSTROPHE +CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_REPLACE, currentName, formerName, codeToInsert, codeToReplace, "", false);
			fixes.add(fix);
			
			// Fix 3 for TypeWarning: Add @Transient
			annotation = ie.ucd.pel.engine.util.CstMorphia.ANNOTATION_TRANSIENT_1;
			access = typeWarning.getOperation().getAttribute().getAccess();
			codeToInsert = CstJava.TAB + annotation + CstJava.LINE_BREAK;
			message = CstPlugin.ADD_ANNO + annotation + CstPlugin.REMOVE_ATT + CstPlugin.APOSTROPHE + currentName + CstPlugin.APOSTROPHE + CstPlugin.DOT;
			fix = new Fix(message, CstPlugin.MARKER_FIX_TYPE_INSERT, currentName, formerName, codeToInsert, "", "", false);
			fixes.add(fix);
						
		}
		return fixes;
	}
	
}
