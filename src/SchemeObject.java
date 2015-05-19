//Comp 141 Assignment 7
//Neha Tammana

// SchemeObjects.java - Expression component objects for a simplified version of Scheme.
// COMP141 - Spring 2015 - HW 6 Instructor's  Solution
// Author: Michael Doherty

public abstract class SchemeObject
{};

// An empty list is a special object, not an empty cons cell
// https://groups.csail.mit.edu/mac/ftpdir/scheme-7.4/doc-html/scheme_8.html
class EmptyList extends SchemeObject
{}

// A pair holds a car and cdr, which are both references to Scheme objects.
class Pair extends SchemeObject
{
	//private SchemeObject car;
	//private SchemeObject cdr;
	public Pair() { car = null; cdr = null; }
	public Pair(SchemeObject _car) { car = _car; cdr = null; }
	public Pair(SchemeObject _car, SchemeObject _cdr) { car = _car; cdr = _cdr; }
	
	public void setCar(SchemeObject _car) { car = _car; }
	public void setCdr(SchemeObject _cdr) { cdr = _cdr; }

	public SchemeObject getCar() { return car; }
	public SchemeObject getCdr() { return cdr; }

	private SchemeObject car;
    private SchemeObject cdr;
	public int length() {
		if (cdr == null) return 1;
		if (!(cdr instanceof Pair)) return 1;
		return 1+((Pair)cdr).length();
	}
}

class SymbolAtom extends SchemeObject
{
	public SymbolAtom(String _value) { value = _value; }
	public String getValue() { return value; }
	private	String value;
}

class BooleanAtom extends SchemeObject
{
	public BooleanAtom(boolean _value) { value = _value; }
	public boolean getValue() { return value; }
	private	boolean value;
}

class IntegerAtom extends SchemeObject
{
	public IntegerAtom(long _value) { value = _value; }
	public long getValue() { return value; }
	private	long value;
}

class RealAtom extends SchemeObject
{
	public RealAtom(double _value) { value = _value; }
	public double getValue() { return value; }
	private	double value;
}

class Function extends SchemeObject
{

		public Function()
		{ parameters = null; body = null; }
	 

		public void setParameters(Pair _params) { parameters = _params; }
		public void setBody(SchemeObject _body) { body = _body; }

		public Pair getParameters() { return parameters; }
		public SchemeObject getBody() { return body; }

	
	    private Pair parameters;
	    private SchemeObject body;
};
