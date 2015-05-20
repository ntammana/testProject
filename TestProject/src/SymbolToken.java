

// A SymbolToken stores a symbol as a string
class SymbolToken extends Token
{
	public SymbolToken(String _value) { value = _value; }
	public String getValue() { return value; }
	private String value;

	public void define(String symbol, SchemeObject definition) {
		// TODO Auto-generated method stub
		
	}
	

};
