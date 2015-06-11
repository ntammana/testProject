//Comp 141 Assignment 7
//Neha Tammana

// ScannerException.java
// COMP141 - Spring 2015 - HW 5 Instructor's  Solution
// Author: Michael Doherty

class ScannerException extends Throwable
{
	public ScannerException()
	{
		msg = "unidentified scanner exception";
	}
	public ScannerException(String _msg)
	{
		msg = "scanner exception: " + _msg;
	}
	public String getMessage()
	{
		return msg;
	}
	private	String msg;
};
