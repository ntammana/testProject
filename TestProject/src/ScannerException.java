

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
