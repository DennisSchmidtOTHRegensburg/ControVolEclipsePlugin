package com.controvol;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.texteditor.ITextEditor;
import org.osgi.framework.BundleContext;

import com.controvol.util.CstPlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID has to be the same as in the manifest!
	public static final String PLUGIN_ID = CstPlugin.PLUGIN_ID;
	
	// The shared instance
	private static Activator plugin;

	//public static List<IProject> projects = new ArrayList<IProject>();
	
	/**
	 * The constructor
	 */
	public Activator() {}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	
	public static Shell getShell() {
		return getActiveWorkbenchWindow().getShell();
	}
	
	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow();
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	/**
	 * Always good to have this static method as when dealing with IResources
	 * having a interface to get the editor is very handy
	 * @return
	 */
	public static ITextEditor getEditor() {
		ITextEditor editor = null;
		if (getActiveWorkbenchWindow() != null){
			if (getActiveWorkbenchWindow().getActivePage() != null){
				if (getActiveWorkbenchWindow().getActivePage().getActiveEditor() != null){
					editor = (ITextEditor) getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				} else {
					System.err.println("3");
				}
			} else {
				System.err.println("2");
			}
		} else {
			System.err.println("1");
		}
		return editor;
	}

}
