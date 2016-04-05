package symtab;

public abstract class Symbol {

	protected String name;
	protected Type type;
	protected int lineNumber;
	
	public Symbol(String name, Type type, int lineNumber) {
		this.name = name;
		this.type = type;
		this.lineNumber = lineNumber;
	}
	
	public String getName() {
		return name;
	}
	
	public Type getType(){
		return type;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
}
