package com.controvol.fixfactory;

public class FixFactoryLoockup {
	
	FixFactory getTypeFixFactory(String approach){
	if (approach=="Morphia")
		return new FixFactoryMorphia();
	
	if(approach=="Objectify")
		return new FixFactoryObjectify();

	return new FixFactoryObjectify();

	}

}
