//Comp 141 Assignment 7
//Neha Tammana

// ParserException.java
// COMP141 - Spring 2015 - HW 6 Instructor's  Solution
// Author: Michael Doherty

class ParserException extends Throwable
{
	public ParserException()
	{
		msg = "unidentified parser exception";
	}
	public ParserException(String _msg)
	{
		msg = "parser exception: " + _msg;
	}
	public String getMessage()
	{
		return msg;
	}
	private	String msg;
};
