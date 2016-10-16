package com.controvol.quickfix;

import ie.ucd.pel.datastructure.evolution.RenameAttribute;
import ie.ucd.pel.engine.util.CstJava;
import ie.ucd.pel.engine.util.Util;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.swt.graphics.Image;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.TextEditGroup;
import org.eclipse.ui.IMarkerResolution2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.controvol.refactoring.RefactorAttribute;
import com.controvol.util.CstPlugin;

public class QuickFix implements IMarkerResolution2 {

	protected String label;
	protected String description;

	QuickFix(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public String getDescription() {
		return description;
	}


	/**
	 * Last correct HEAD
	 */
	public void run(IMarker mk) {
		/*
		 * TODO: Find out which fix to pick. I guess here is the place to chose (within the for loop?!?!?!)
		 * I assume that now we're applying one of them from the array and that's it (at random?!?!).
		 * 
		 * @see org.eclipse.ui.IMarkerResolutionGenerator#getResolutions(org.eclipse.core.resources.IMarker)
		 */
		try {
			Integer indexFixToApply = getIndexFixToApply(mk);
			String codeLineNbAux = (String) mk.getAttribute(IMarker.LOCATION); 
			Integer codeLineNb = Integer.valueOf(codeLineNbAux.split(CstJava.SPACE)[1]);
			String jsonFixes = (String) mk.getAttribute(CstPlugin.MARKER_JSON_FIXES);
			JSONArray jsonArray = new JSONArray(jsonFixes);
			JSONObject obj = jsonArray.getJSONObject(indexFixToApply);
			Integer fixType = obj.getInt(CstPlugin.MARKER_FIX_TYPE);
			String oldName = obj.getString(CstPlugin.MARKER_OLD_NAME_ID);
			Boolean isRenaming = obj.getBoolean(CstPlugin.MARKER_IS_RENAMING);
			IResource resrc = mk.getResource();
			if (fixType.equals(CstPlugin.MARKER_FIX_TYPE_INSERT)){
				String insertCodeFix = obj.getString(CstPlugin.MARKER_INSERT_CODE_FIX);
				if(codeCheck(insertCodeFix)){
					createChange(resrc, insertCodeFix, null, codeLineNb, CstPlugin.MARKER_FIX_TYPE_INSERT);
				}
			} else if (fixType.equals(CstPlugin.MARKER_FIX_TYPE_REPLACE)){
				String insertCodeFix = obj.getString(CstPlugin.MARKER_INSERT_CODE_FIX);
				if(codeCheck(insertCodeFix)){
					String replaceCodeFix = obj.getString(CstPlugin.MARKER_REPLACE_CODE_FIX);
					createChange(resrc, insertCodeFix, replaceCodeFix, codeLineNb, CstPlugin.MARKER_FIX_TYPE_REPLACE);
				}
			} else if (fixType.equals(CstPlugin.MARKER_FIX_TYPE_DELETE)){
				String deleteCodeFix = obj.getString(CstPlugin.MARKER_DELETE_CODE_FIX);
				createChange(resrc, "", deleteCodeFix, codeLineNb, CstPlugin.MARKER_FIX_TYPE_DELETE);
			}
			if (isRenaming){ 
				Set<RenameAttribute> opToRemove = new TreeSet<RenameAttribute>();
				for (RenameAttribute op : RefactorAttribute.renamingOperations){
					String resrcName = mk.getResource().getRawLocation().toString();
					resrcName=Util.windowsToUnixPath(resrcName);
					resrcName = resrcName.substring(0, resrcName.length()-(CstJava.JAVA_EXTENSION_1.length()+1));
					resrcName = resrcName.replace(CstJava.UNIX_SEPERATOR, CstJava.DOT);
					if (resrcName.endsWith(resrcName) && oldName.equals(op.getLegacyAttribute().getName())){
						opToRemove.add(op);
					}
				}
				RefactorAttribute.renamingOperations.removeAll(opToRemove);
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private Integer getIndexFixToApply(IMarker mk){
		Integer index = -1;
		try {
			String jsonFixes = (String) mk.getAttribute(CstPlugin.MARKER_JSON_FIXES);
			JSONArray jsonArray = new JSONArray(jsonFixes);
			for (Integer i = 0 ; i < jsonArray.length() ; i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				String description = obj.getString(CstPlugin.MARKER_DESCRIPTION);
				if (label.equals(description)){
					index = i;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return index;
	}

	@Override
	public Image getImage() {
		// TODO Load a proper image to replace the bulb on the left
		return JavaPluginImages.get(CstPlugin.MARKER_ICON_ID);
	}

	private void createChange(IResource res, String insertCodeFix, String replaceCodeFix, Integer codeLineNb, Integer typeFix) {
		IFile fFile = (IFile) res;
		TextFileChange change = new TextFileChange(CstPlugin.MSG_CHANGE, fFile);
		change.setEdit(new MultiTextEdit());
		ITextFileBufferManager manager = FileBuffers.getTextFileBufferManager();
		try {
			manager.connect(fFile.getFullPath(), LocationKind.IFILE, null);
			ITextFileBuffer textFileBuffer = manager.getTextFileBuffer(fFile.getFullPath(), LocationKind.IFILE);
			String code = textFileBuffer.getDocument().get();
			String[] lines = code.split(CstJava.LINE_BREAK);
			Integer offset = 0;
			Boolean found = false;
			for (Integer i = 0 ; (i < lines.length) && !found ; i++){
				String line = lines[i];
				found = i.equals(codeLineNb-1); 
				if (found){
					TextEdit edit = null;
					if (typeFix.equals(CstPlugin.MARKER_FIX_TYPE_INSERT)){
						edit = new InsertEdit(offset, insertCodeFix);
					} else if (typeFix.equals(CstPlugin.MARKER_FIX_TYPE_REPLACE)){
						/*offset += line.indexOf(replaceCodeFix);
						edit = new ReplaceEdit(offset, replaceCodeFix.length(), insertCodeFix);*/
						offset += line.indexOf(replaceCodeFix);
						edit = new ReplaceEdit(offset, replaceCodeFix.length(), insertCodeFix);
					} else if (typeFix.equals(CstPlugin.MARKER_FIX_TYPE_DELETE)){
						offset += line.indexOf(replaceCodeFix);
						edit = new ReplaceEdit(offset, replaceCodeFix.length(), insertCodeFix);
					}
					change.addEdit(edit);
					change.addTextEditGroup(new TextEditGroup(CstPlugin.MSG_CHANGE, edit));
					change.perform(new ProgressMonitor());
				}
				offset += line.length() + 1;
			}
		} catch (CoreException e) {
			e.printStackTrace();			
		} finally {
			try {
				manager.disconnect(fFile.getFullPath(), LocationKind.IFILE, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean codeCheck(String code){
		if(label.toLowerCase().contains("alsoload") && code.toLowerCase().contains("alsoload")){
			return true;
		} else if(label.toLowerCase().contains("restore")){
			return true;			
		}else if((label.toLowerCase().contains("ignore")) && (code.toLowerCase().contains("ignore"))){
			return true;
		}else if((label.toLowerCase().contains("transient")) && (code.toLowerCase().contains("transient"))){
			return true;
		}else if((label.toLowerCase().contains("notsaved")) && (code.toLowerCase().contains("notsaved"))){
			return true;
		}else if((label.toLowerCase().contains("property")) && (code.toLowerCase().contains("property"))){
			return true;
		}else 
			return false;
	}

}