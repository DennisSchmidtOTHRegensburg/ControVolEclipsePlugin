package com.controvol.views;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.evolution.Evolution;
import ie.ucd.pel.datastructure.evolution.Operation;
import ie.ucd.pel.datastructure.evolution.RetypeAttribute;
import ie.ucd.pel.datastructure.warning.IWarning;
import ie.ucd.pel.engine.ControVolEngine;
import ie.ucd.pel.engine.crawler.CrawlerXml;
import ie.ucd.pel.engine.crawler.ICrawler;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import com.controvol.Activator;
import com.controvol.util.CstPlugin;

public class ProblemsView extends ViewPart {

	protected Set<IWarning> warnings = new TreeSet<IWarning>();
	Composite shell;
	protected Table table;
	ListViewer viewer;
	

	public ProblemsView(){		

		ICrawler crawler = new CrawlerXml();
		ControVolEngine eng = new ControVolEngine();
		Evolution evol = new Evolution();

		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);

		Map<String, MApplication> mapApps = new HashMap<String, MApplication>();
		URL urlRepPlugin = bundle.getEntry(CstPlugin.REP_PLUGIN);
		try {
			File fRepPlugin = new File(FileLocator.resolve(urlRepPlugin).toURI());
			String[] sAppFiles = fRepPlugin.list();
			for (Integer i = 0 ; i < sAppFiles.length ; i++){
				String sAppFile = sAppFiles[i];
				if (sAppFile.endsWith(".xml")){
					URL urlAppFile = new URL(urlRepPlugin.toString() + sAppFile);
					File fAppFile = new File(FileLocator.resolve(urlAppFile).toURI());
					MApplication app = crawler.getApplication(fAppFile.getPath());
					mapApps.put(app.getVersion(), app);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// XXX Should we consider 
		// (0,1), (0,2), (0,3) ..., (0,N), (1, 2), (1,3), ..., (1,N), (2, 3), ..., (2, N), ... (N-1, N)
		// or only (0,1), (1,2), ..., (N-1,N)?
		for (Integer i = mapApps.keySet().size() ; i > 1 ; i--){
			MApplication appA = mapApps.get((i-1)+"");
			MApplication appB = mapApps.get(i+"");
			evol.addAll(eng.getEvolution(appB, appA));
		}
		System.out.println("There is " + evol.size() + " evolutions."); // TODO Remove trace
		for (Operation o : evol){
			System.out.println(o.toString());
		}
		this.warnings = eng.check(evol);
		System.out.println("There is " + this.warnings.size() + " errors."); // TODO Remove trace
		
	}

	public void createPartControl(final Composite shell) {		
		this.shell=shell;
		table = new Table (shell, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		//data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {"Warning", "Legacy version", " Current version", "Resource", "Location", "Type"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles[i]);
		}	
		for (IWarning error : this.warnings) {
			TableItem item = new TableItem(table, SWT.NONE);
			Operation op = error.getOperation();
			if (op instanceof RetypeAttribute){

				RetypeAttribute retypeOperation = (RetypeAttribute) op;
				String description = "The type of '" + retypeOperation.getAttribute().getName() + "'" +
						" has changed from '" + retypeOperation.getLegacyAttribute().getType() + "' to '" + retypeOperation.getAttribute().getType() + "'.";
				item.setText (0, description);
				item.setText (1, retypeOperation.getVersion1());
				item.setText (2, retypeOperation.getVersion2());
				item.setText (3, retypeOperation.getEntity().getEntityName());
				item.setText (4, "line " + retypeOperation.getAttribute().getLocation().getLineNumber());
				item.setText (5, "'com.googlecode.objectify.LoadException' when loading legacy entities.");
			}
		}
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack();
		}
		createToolbar();


		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				//new QuickFix().dialog(shell);	
				////String string = "";
				//TableItem[] selection = table.getSelection();

				TableItem item = (TableItem)event.item;
				for(int i=0; i< 6 ;i++)
					System.out.print(item.getText(i)+"; ");


				// for (int i = 0; i < selection.length; i++)
				// string += selection[i] + " ";
				//System.out.println(selection.length+" from Selection={" + string + "}"+  selection);

			}
		});

	}

	public void setFocus() {
		// set focus to my widget.  For a label, this doesn't
		// make much sense, but for more complex sets of widgets
		// you would decide which one gets the focus.		
	}

	public void setWarnings(Set<IWarning> warnings){
		this.warnings = warnings;
	}
	/**
	 * Create menu.
	 */
	/**
	 * Create toolbar.
	 */
	private void createToolbar() {

		Action compileAction=new Action("Compiler"){
			public void run(){		
				System.out.println("It is ALIVEEEE!!!!");
				TableItem[] selected = table.getSelection();
				for(TableItem sel: selected){
					System.out.println(sel.toString());
				}
			}
		};

		Action fixAction=new Action("Fixing Dialog"){
			public void run(){
				//new QuickFix().dialog(shell);		
			}
		};
		
		ImageData compileIcon = new ImageData(CstPlugin.PLUGIN_ID+"/icons/compile.png");

		compileIcon.scaledTo(16, 16);
		compileAction.setImageDescriptor(ImageDescriptor.createFromImageData(compileIcon));

		ImageData fixIcon = new ImageData(CstPlugin.PLUGIN_ID+"/icons/fix.png");

		fixIcon.scaledTo(16, 16);
		fixAction.setImageDescriptor(ImageDescriptor.createFromImageData(fixIcon));

		/* Setup the toolbar */
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();
		mgr.add(compileAction);
		mgr.add(fixAction);

	}

}