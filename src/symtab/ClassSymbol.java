package symtab;

public class ClassSymbol extends ScopedSymbol implements Type {

	public ClassSymbol(String name) { //class has no enclosing scope since it is the top level
		super(name, null); 
	}
	
	public ClassSymbol(String name, boolean defaultClass) { //class that is default (eg. System class)
		super(name, null, true); 
	}

	public ClassSymbol(String name, Scope currentScope) {
		super(name, currentScope); 
	}

}
