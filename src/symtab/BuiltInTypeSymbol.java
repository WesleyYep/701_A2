package symtab;

public class BuiltInTypeSymbol extends Symbol implements Type {
	
	public BuiltInTypeSymbol(String name) {
		super(name, null, 0); //built in types are always valid, so line number is 0
	}
	
}
