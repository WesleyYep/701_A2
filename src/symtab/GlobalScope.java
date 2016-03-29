package symtab;

import java.util.HashMap;

public class GlobalScope {//extends BaseScope {
	
	private HashMap<String,Symbol> symbols = new HashMap<String,Symbol>();
	
	public GlobalScope(){
		define(new BuiltInTypeSymbol("int"));
		//TODO add all primitives
		define(new ClassSymbol("String"));
	}
	
	public String getScopeName() {
		return "Global Scope";
	}

	public void define(Symbol symbol) {
		symbols.put(symbol.getName(), symbol);
	}

	public Symbol resolve(String name) {
		return symbols.get(name);
	}
	
}
