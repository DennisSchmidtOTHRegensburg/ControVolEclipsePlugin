package com.controvol.natures;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.controvol.util.ControVolSetup;
import com.controvol.util.CstPlugin;

public class ControVolNature implements IProjectNature {

	private IProject project = null;

	public ControVolNature(){}

	public void configure() throws CoreException {
		addBuilder();
		addNature();
		ControVolSetup setup = new ControVolSetup(ControVolSetup.getCurrentProject());
		setup.getControVolFolder();
		this.project.refreshLocal(IProject.DEPTH_INFINITE, null);
	}

	public void deconfigure() throws CoreException {
		removeBuilder();
		removeNature();
		this.project.refreshLocal(IProject.DEPTH_INFINITE, null);
	}

	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	private void addBuilder() {
		if (!hasControVolBuilder()) {
			try {
				// Add builder to project
				IProjectDescription desc = this.project.getDescription();
				ICommand[] buildSpecCmds = desc.getBuildSpec();
				ICommand buildSpecCmd = desc.newCommand();
				buildSpecCmd.setBuilderName(CstPlugin.BUILDER_ID);
				ICommand[] newBuildSpecCmds = new ICommand[buildSpecCmds.length + 1];
				System.arraycopy(buildSpecCmds, 0, newBuildSpecCmds, 0, buildSpecCmds.length);
				newBuildSpecCmds[buildSpecCmds.length] = buildSpecCmd;
				desc.setBuildSpec(newBuildSpecCmds);
				project.setDescription(desc, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void removeBuilder() {
		if (hasControVolBuilder()) {
			try {
				// Remove builder from project
				IProjectDescription desc = this.project.getDescription();
				ICommand[] buildSpecCmds = desc.getBuildSpec();
				ICommand buildSpecCmd = desc.newCommand();
				buildSpecCmd.setBuilderName(CstPlugin.BUILDER_ID);
				ICommand[] newBuildSpecCmds = new ICommand[buildSpecCmds.length - 1];
				Integer cnt = 0;
				for (Integer i = 0 ; i < buildSpecCmds.length ; i++){
					if (!buildSpecCmds[i].getBuilderName().equals(CstPlugin.BUILDER_ID)){
						newBuildSpecCmds[cnt] = buildSpecCmds[i];
						cnt++;
					}
				}
				desc.setBuildSpec(newBuildSpecCmds);
				project.setDescription(desc, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void addNature() {
		if (!hasControVolNature()) {
			try {
				// Add nature to the project
				IProjectDescription desc = this.project.getDescription();
				String[] natureIds = desc.getNatureIds();
				String[] newNatureIds = new String[natureIds.length + 1];
				System.arraycopy(natureIds, 0, newNatureIds, 0, natureIds.length);
				newNatureIds[natureIds.length] = CstPlugin.NATURE_ID;
				desc.setNatureIds(newNatureIds);
				project.setDescription(desc, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void removeNature() {
		if (hasControVolNature()) {
			try {
				// Remove nature from the project
				IProjectDescription desc = this.project.getDescription();
				String[] natureIds = desc.getNatureIds();
				String[] newNatureIds = new String[natureIds.length - 1];
				Integer cnt = 0;
				for (Integer i = 0 ; i < natureIds.length ; i++){
					if (!natureIds[i].equals(CstPlugin.NATURE_ID)){
						newNatureIds[cnt] = natureIds[i];
						cnt++;
					}
				}
				desc.setNatureIds(newNatureIds);
				project.setDescription(desc, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Boolean hasControVolBuilder(){
		Boolean found = false;
		IProjectDescription desc;
		try {
			desc = this.project.getDescription();
			ICommand[] commands = desc.getBuildSpec();
			Integer i = 0;
			while (i < commands.length  && !found) {
				found = commands[i].getBuilderName().equals(CstPlugin.BUILDER_ID);
				i++;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return found;
	}

	private Boolean hasControVolNature(){
		Boolean found = false;
		IProjectDescription desc;
		try {
			desc = this.project.getDescription();
			String[] natureIds = desc.getNatureIds();
			Integer i = 0;
			while (i < natureIds.length  && !found) {
				found = natureIds[i].equals(CstPlugin.NATURE_ID);
				i++;
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return found;
	}

}
