package com.controvol.quickfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.controvol.util.CstPlugin;

public class QuickFixer implements IMarkerResolutionGenerator {	

	public IMarkerResolution[] getResolutions(IMarker mk) {
		IMarkerResolution[] resolutions = new IMarkerResolution[0];
		try {
			String jsonFixes = (String) mk.getAttribute(CstPlugin.MARKER_JSON_FIXES);
			try {
				JSONArray jsonArray = new JSONArray(jsonFixes);
				resolutions = new IMarkerResolution[jsonArray.length()];
				for (Integer i = 0 ; i < jsonArray.length() ; i++){
					JSONObject obj = jsonArray.getJSONObject(i);
					resolutions[i] = new QuickFix(obj.getString(CstPlugin.MARKER_DESCRIPTION));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return resolutions;
	}

}