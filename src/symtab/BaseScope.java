package symtab;

import java.util.HashMap;

import se701.A2SemanticsException;

public abstract class BaseScope implements Scope {
	
	private HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();
	protected Scope enclosingScope = null;
	
	public BaseScope(Scope enclosingScope) {
		this.enclosingScope = enclosingScope;
		define(new BuiltInTypeSymbol("int"));
		define(new BuiltInTypeSymbol("String"));
		define(new BuiltInTypeSymbol("boolean"));
		define(new BuiltInTypeSymbol("long"));
		define(new BuiltInTypeSymbol("double"));
		define(new BuiltInTypeSymbol("char"));
		define(new BuiltInTypeSymbol("null"));

		//map type for feature
		define(new BuiltInTypeSymbol("Map"));
		define(new BuiltInTypeSymbol("Entry"));
		define(new BuiltInTypeSymbol("System"));

	}
	
	@Override
	public String getScopeName() {
		return enclosingScope.getScopeName();
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
		boolean isContained = false;
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
