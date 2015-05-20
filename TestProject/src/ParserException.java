

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
