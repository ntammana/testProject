//Comp 141 Assignment 7
//Neha Tammana


// IntegerToken.java
// COMP141 - Spring 2015 - HW 5 Instructor's Solution
// Author: Michael Doherty

// A IntegerToken stores a long integer
class IntegerToken extends Token
{
	public IntegerToken(long _value) { value = _value; }
	public long getValue() { return value; }
	private	long value;
}
