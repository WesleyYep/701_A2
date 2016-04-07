package symtab;

import java.util.HashMap;

import se701.A2SemanticsException;

public abstract class ScopedSymbol extends Symbol implements Scope {
	
	private HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();
	protected Scope enclosingScope = null;
	
	public ScopedSymbol(String name, Scope enclosingScope) {
		super(name, null, 0); //scoped symbols are valid anywhere in the scope
		this.enclosingScope = enclosingScope;
		define(new BuiltInTypeSymbol("int"));
		define(new BuiltInTypeSymbol("String"));
		define(new BuiltInTypeSymbol("boolean"));
		define(new BuiltInTypeSymbol("long"));
		define(new BuiltInTypeSymbol("double"));
		define(new BuiltInTypeSymbol("char"));
		define(new BuiltInTypeSymbol("null"));
		define(new BuiltInTypeSymbol("Integer"));
		define(new BuiltInTypeSymbol("Long"));
		define(new BuiltInTypeSymbol("Character"));
		define(new BuiltInTypeSymbol("Double"));
		define(new BuiltInTypeSymbol("Boolean"));

		//map type for feature
		define(new BuiltInTypeSymbol("Map"));
		define(new BuiltInTypeSymbol("Entry"));
		define(new BuiltInTypeSymbol("System"));
		define(new BuiltInTypeSymbol("System.out"));
		define(new BuiltInTypeSymbol("String[]"));
	}
	
	@Override
	public String getScopeName() {
		return name;
	}
	
	@Override
	public Scope getEnclosingScope() {
		return enclosingScope;
	}
	
	@Override
	public void define(Symbol symbol) {
		String name = symbol.getName();
		if (symbols.get(name) != null) {
			throw new A2SemanticsException("\""+name+"\" is already defined in scope "+getScopeName());
		}
		symbols.put(name, symbol);
	}
	
	@Override
	public Symbol resolve(String name) {
		// if the symbol exists in the current scope, return it
		Symbol s = symbols.get(name);
		if (s != null)
			return s;
		
		// otherwise look in the enclosing scope, if there is one
		Scope sc = enclosingScope;
		while (sc != null) {
			Symbol sym = enclosingScope.resolve(name);
			if (sym != null) {
				return sym;
			}
			sc = sc.getEnclosingScope();
		}
		// otherwise it doesn't exist
		return null;
	}
}
