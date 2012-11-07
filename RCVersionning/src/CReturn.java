
public class CReturn
{
	public eReturnCodes eReturnValue ;
	public String sReturnMessage ;
	public enum eReturnCodes
	{
		ERR_OK,
		ERR_HELP,
		ERR_INVALIDARGUMENT,
		ERR_UNDEFINED, 
		ERR_UNDEFINED_RCFILE, 
		ERR_INVALID_MAJOR_VERSION, 
		ERR_INVALID_MINOR_VERSION, 
		ERR_INVALID_RELEASE_VERSION, 
		ERR_INVALID_COMPILATION_VERSION,
		ERR_FILENOTFOUND, 
		ERR_FILEERROR,
		ERR_FILERENAMINGERROR,
		ERR_FILESECURITYERROR
	};
	public CReturn ()
	{
		this.eReturnValue = eReturnCodes.ERR_UNDEFINED ;
	}
	public CReturn (eReturnCodes eRCode)
	{
		this.eReturnValue = eRCode ;
	}
	public void displayReturnMessage() 
	{
		String msg = null ;
		switch (this.eReturnValue)
		{
			case ERR_OK :
				msg = new String ("RCVersionning : No error");
				break ;
			case ERR_HELP:
				msg = new String ("RCVersionning : Usage RCVersionning /f filename.rc /v MajorVersionNumber MinorVersionNumber ReleaseVersionNumber CompilationVersionNumber");
				break ;
			case ERR_INVALIDARGUMENT:
				msg = new String ("RCVersionning : Incorrect arguments");
				break ;
			case ERR_UNDEFINED_RCFILE :
				msg = new String ("RCVersionning : RC file not specified");
				break;
			case ERR_INVALID_MAJOR_VERSION :
				msg = new String ("RCVersionning : ??.XX.XX.XX incorrect 1st version number");
				break;
			case ERR_INVALID_MINOR_VERSION :
				msg = new String ("RCVersionning : XX.??.XX.XX incorrect 2nd version number");
				break;
			case ERR_INVALID_RELEASE_VERSION :
				msg = new String ("RCVersionning : XX.XX.??.XX incorrect 3rd version number");
				break;
			case ERR_INVALID_COMPILATION_VERSION :
				msg = new String ("RCVersionning : XX.XX.XX.?? incorrect 4th version number");
				break;
			case ERR_UNDEFINED :
				msg = new String ("RCVersionning : undefined");
				break ;
			case ERR_FILENOTFOUND :
				msg = new String ("RCVersionning : File not found");
				break;
			case ERR_FILEERROR :
				msg = new String ("RCVersionning : File error");
				break;
			case ERR_FILERENAMINGERROR :
				msg = new String ("RCVersionning : File error while backuping - deleting");
				break;
			case ERR_FILESECURITYERROR :
				msg = new String ("RCVersionning : File error while backuping - forbidden access");
				break;				
				
		}
		System.out.println("----------------------------------------------------------");
		System.out.println(msg);
		System.out.println("==========================================================");
	}
}
