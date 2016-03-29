package symtab;

public class ClassSymbol extends ScopedSymbol implements Type {

	public ClassSymbol(String name) {
		super(name, null);
	}

}
