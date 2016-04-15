package symtab;

public class MethodSymbol extends ScopedSymbol {

	public MethodSymbol(String name, Scope scope, boolean defaultMethod) { //default method (eg. System.println());
		super(name, scope, defaultMethod);
	}
	
	public MethodSymbol(String name, Scope scope) {
		super(name, scope); 
	}

}
