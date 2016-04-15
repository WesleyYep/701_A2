package symtab;

import java.util.HashMap;

import se701.A2SemanticsException;

public abstract class BaseScope implements Scope {
	
	private HashMap<String, Symbol> symbols = new HashMap<String, Symbol>();
	protected Scope enclosingScope = null;
	
	public BaseScope(Scope enclosingScope) {
		this.enclosingScope = enclosingScope;
		define(new BuiltInTypeSymbol("int"));
		define(new ClassSymbol("String"));
		define(new BuiltInTypeSymbol("boolean"));
		define(new BuiltInTypeSymbol("long"));
		define(new BuiltInTypeSymbol("double"));
		define(new BuiltInTypeSymbol("char"));
		define(new BuiltInTypeSymbol("null"));
		
		//map type for feature
		ClassSymbol entry = new ClassSymbol("Entry", true);
		entry.define(new MethodSymbol("getKey", entry, true));
		entry.define(new MethodSymbol("getValue", entry, true));
		define(entry);
		
		ClassSymbol system = new ClassSymbol("System", true);
		define(system);
		
		define(new ClassSymbol("String[]", true));
		define(new ClassSymbol("Map", true));
		define(new ClassSymbol("HashMap", true));
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
	
	@Override
	public Symbol resolveMember(String name) {
		// if the symbol exists in the current scope, return it
		Symbol s = symbols.get(name);
		if (s != null)
			return s;
		
		// don't look in the enclosing scope for this one
		
		// otherwise it doesn't exist
		return null;
	}
}
