package symtab;

public class BaseScope implements Scope  {

	protected Scope scope;
	
	public BaseScope(Scope scope) {
		this.scope = scope;
	}

	
	@Override
	public String getScopeName() {
		return scope.getScopeName();
	}

	@Override
	public Scope getEnclosingScope() {
		return scope;
	}

	@Override
	public void define(Symbol symbol) {
		
	}

	@Override
	public Symbol resolve(String name) {
		return null;
	}

}
