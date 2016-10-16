package ie.ucd.pel.datastructure.evolution;

import java.util.TreeSet;

public class Evolution extends TreeSet<Operation> {

	private static final long serialVersionUID = 1L;

	public Evolution(){
		super();
	}
	
	public String toString(){
		String res = "";
		for (Operation op : this){
			res += op.toString() + "\n";
		}
		return res;
	}

	public Evolution filter(Class<?> clas){
		Evolution evol = new Evolution();
		for (Operation o : this){
			if (o.getClass().equals(clas)){
				evol.add(o);
			}
		}
		return evol;
	}
	
}
