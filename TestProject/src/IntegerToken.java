

// A IntegerToken stores a long integer
class IntegerToken extends Token
{
	public IntegerToken(long _value) { value = _value; }
	public long getValue() { return value; }
	private	long value;
}
