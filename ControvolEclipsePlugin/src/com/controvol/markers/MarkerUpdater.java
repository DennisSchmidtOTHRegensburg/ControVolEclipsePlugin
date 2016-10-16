package com.controvol.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.ui.texteditor.IMarkerUpdater;

import com.controvol.util.CstPlugin;

public class MarkerUpdater implements IMarkerUpdater {
	/*
	 *Returns the attributes for which this updater is responsible.
	 *If the result is null, the updater assumes responsibility for any attributes.
	 */
	@Override
	public String[] getAttribute() {
		return null;
	}

	@Override
	public String getMarkerType() {
		//returns the marker type that we are interested in updating
		return CstPlugin.MARKER_ID;
	}

	@Override
	public boolean updateMarker(IMarker marker, IDocument doc, Position position) {
		try {
			int start = position.getOffset();
			int end = position.getOffset() + position.getLength();
			marker.delete();
			marker.setAttribute(IMarker.LINE_NUMBER, start);
			marker.setAttribute(IMarker.CHAR_END, end);
			return true;
		} catch (CoreException e) {
			return false;
		}
	}
}
