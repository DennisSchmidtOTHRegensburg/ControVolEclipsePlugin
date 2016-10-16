package com.controvol.fixfactory;

import java.util.List;

import com.controvol.quickfix.Fix;

import ie.ucd.pel.datastructure.warning.IWarning;

public class FixFactoryDelegation {
	
	private String approach;
	private FixFactory fixFactory;
	private FixFactoryLoockup lookup=new FixFactoryLoockup();
	private IWarning warning;
	
	public void setIWarning(IWarning warning){
		this.warning=warning;
	}
	public void setTypeChecking(String approach){
		this.approach = approach;
	}
	
	public List<Fix>  getCodeFixes(){
		fixFactory=lookup.getTypeFixFactory(approach);
		return fixFactory.getCodeFixes(warning);
	}
}
