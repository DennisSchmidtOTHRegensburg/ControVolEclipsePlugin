package com.controvol.typechecking;
import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MEntity;
import ie.ucd.pel.datastructure.evolution.AddAttribute;
import ie.ucd.pel.datastructure.evolution.DeleteAttribute;
import ie.ucd.pel.datastructure.evolution.Evolution;
import ie.ucd.pel.datastructure.evolution.Operation;
import ie.ucd.pel.datastructure.evolution.WildRenameAttribute;
import ie.ucd.pel.datastructure.warning.AttributeDeleted;
import ie.ucd.pel.datastructure.warning.AttributeImproperlyRenamed;
import ie.ucd.pel.datastructure.warning.IWarning;
import ie.ucd.pel.datastructure.warning.TypeWarning;
import ie.ucd.pel.engine.ControVolEngine;
import ie.ucd.pel.engine.crawler.CrawlerGit;
import ie.ucd.pel.engine.util.CstJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import com.controvol.markers.MarkerFactory;
import com.controvol.refactoring.RefactorAttribute;
import com.controvol.util.UtilPlugin;

/* First get the legacy list of the application. Then check the current model with the legacy list.
 * Create warnings and filter warnings (This is necessary because the current model is checked with every legacy application
 * and because of that the same warnings are created repeatedly).
 */
public class TypeChecking {

	public static IProject project;
	public static IResource resr;

	/* The following attribute corresponds to the current model of the application. 
	 * It will be modified every time the Java code changes. 
	 * XXX allow to have only 1 model in the environment... 
	 */
	public static MApplication currentApplication;

	
	public void checking(IResource resr) {	
		visit(resr);
	}

	public void checking(IResourceDelta delta) {
		visit(delta.getResource());
	}

	private void visit(IResource resr){
		setResource(resr);
		setProject(resr.getProject());
		visitor();
	}

	public void visitor(){
		String ext = getResource().getFileExtension();

		if (ext != null){
			if (ext.equals(CstJava.JAVA_EXTENSION_1)){
				//System.out.println("RESOURCE ON THE FLY: "+getResource().toString());
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IProject project = root.getProject(this.getProjectName());
				IJavaProject javaProject = JavaCore.create(project);  
				String entityName = UtilPlugin.getResourceName(getResource());

				// This get the entire legacy history from the staging
				List<MApplication> appHistory = UtilPlugin.getAppList(ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation().toString() + CstJava.UNIX_SEPERATOR);		

				if (TypeChecking.currentApplication.containsEntity(entityName)){
					MEntity Entity=TypeChecking.currentApplication.getEntity(entityName);
					String projectFolderPath = null;
					String srcFolderPath = null;
					String binFolderPath = null;
					
					try {
						projectFolderPath = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getPath()).getLocation().toString() + CstJava.UNIX_SEPERATOR;
						binFolderPath = ResourcesPlugin.getWorkspace().getRoot().findMember(javaProject.getOutputLocation()).getLocation().toString() + CstJava.UNIX_SEPERATOR;
						IClasspathEntry[] srcFolders = javaProject.getResolvedClasspath(false);
						 if(srcFolders[0].getContentKind() == IPackageFragmentRoot.K_SOURCE){
				                IPath path = srcFolders[0].getPath();
				                srcFolderPath = path.segments()[path.segmentCount()-1] + CstJava.UNIX_SEPERATOR;
						 }
						
					} catch (JavaModelException e) {
						e.printStackTrace();
					}

					CrawlerGit c = new CrawlerGit(projectFolderPath, srcFolderPath, binFolderPath);
					TypeChecking.currentApplication = c.getApplication();

					// Compute evolutions	
					appHistory.add(TypeChecking.currentApplication);
					ControVolEngine eng = new ControVolEngine();
					Evolution evol = eng.getEvolution(appHistory);

					List<Operation> operationsToRemove = new ArrayList<Operation>();
					for (WildRenameAttribute operation : RefactorAttribute.renamingOperations){
						String operationEntityName = operation.getEntity().getEntityName();
						String operationAttributeName = operation.getAttribute().getName();
						String operationLegagcyAttributeName = operation.getLegacyAttribute().getName();
						String versionNb = operation.getVersion2();
						// Remove the add operations and the remove operations
						for (Operation op : evol){
							if (op instanceof AddAttribute){
								AddAttribute opAdd = (AddAttribute) op;
								if (operationEntityName.equals(opAdd.getEntity().getEntityName()) && operationAttributeName.equals(opAdd.getAttribute().getName())){
									operationsToRemove.add(op);
								}
							} else if (op instanceof DeleteAttribute){
								DeleteAttribute opDelete = (DeleteAttribute) op;
								if (operationEntityName.equals(opDelete.getEntity().getEntityName()) && operationLegagcyAttributeName.equals(opDelete.getAttribute().getName())){
									operationsToRemove.add(op);									
								}
							}
						}
						evol.removeAll(operationsToRemove);
						// Add the rename operations
						operation.getLegacyAttribute().getName();
						operation.setVersion2(versionNb);
						evol.add(operation);
					}
					
					MarkerFactory.setMoph(Entity.getIsMophEntity());
					MarkerFactory.setObj(Entity.getIsObjEntity());
					
					Set<IWarning> warnings = eng.check(evol);
					
					// Filter warnings
					Set<IWarning> warnings2 = new TreeSet<IWarning>();
					for (IWarning warning : warnings){
						Boolean same=false;
						if (warning instanceof TypeWarning){
							TypeWarning warningRetype = (TypeWarning) warning;
							for(IWarning warning2:warnings2){
								if(warning2 instanceof TypeWarning){
									TypeWarning warningRetype2 = (TypeWarning) warning2;
									String name1=(warningRetype2.getOperation().getAttribute().getName());
									String name2=(warningRetype.getOperation().getAttribute().getName());
									if(name1.compareTo(name2)==0)
								same=true;
								}
							}
							if(same==false){
								warnings2.add(warning);
							}
						}
						else if (warning instanceof AttributeImproperlyRenamed){
							AttributeImproperlyRenamed warningRename = (AttributeImproperlyRenamed) warning;
							for(IWarning warning2:warnings2){
								if(warning2 instanceof AttributeImproperlyRenamed){
									AttributeImproperlyRenamed warningRename2 = (AttributeImproperlyRenamed) warning2;
									String name1=(warningRename2.getOperation().getAttribute().getName());
									String name2=(warningRename.getOperation().getAttribute().getName());
									if(name1.compareTo(name2)==0)
								same=true;
								}
							}
							if(same==false){
								warnings2.add(warning);
							}
						}
						else if (warning instanceof AttributeDeleted){
							AttributeDeleted warningAttDel = (AttributeDeleted) warning;
							for(IWarning warning2:warnings2){
								if(warning2 instanceof AttributeDeleted){
									AttributeDeleted warningAttDel2 = (AttributeDeleted) warning2;
									String name1=(warningAttDel2.getOperation().getAttribute().getName());
									String name2=(warningAttDel.getOperation().getAttribute().getName());
									if(name1.compareTo(name2)==0){
										same=true;}
								}
							}
							if(same==false){
								warnings2.add(warning);
							}
						}
					}
					//Create Markers
					for (IWarning warning : warnings2){
						MarkerFactory.createWarning(warning, project);
					}

					MarkerFactory.findMarkers(resr);
				} 
			} // it should never be null
		}
	}

	private String getProjectLocation(){
		return getProject().getWorkspace().getRoot().getLocation().toString();
	}

	public String getProjectName(){
		return getProject().getName();
	}

	private String getProjectFullName(){
		return getProjectLocation() + CstJava.UNIX_SEPERATOR + getProjectName() + CstJava.UNIX_SEPERATOR;
	}

	private IProject getProject(){
		return project;
	}

	private void setProject(IProject project){
		this.project = project;
	}

	private void setResource(IResource resr){
		this.resr = resr;
	}
	private IResource getResource(){
		return resr;
	}
}
