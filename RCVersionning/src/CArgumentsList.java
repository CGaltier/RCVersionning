
public class CArgumentsList 
{
	public String sFileName ;
	public int iMajorVersionNumber ;
	public int iMinorVersionNumber ;
	public int iReleaseVersionNumber ;
	public int iCompilationVersionNumber ;
	public boolean bValid ;
	public boolean bHelp;
	public String sNewVersion;
	
	public CArgumentsList ()
	{
		sNewVersion = null ;
		bHelp = false ;
		bValid = false ;
		this.sFileName = null ;
		this.iMajorVersionNumber = -1 ;
		this.iMinorVersionNumber = -1 ;
		this.iReleaseVersionNumber = -1 ;
		this.iCompilationVersionNumber = -1 ;
	}
	public CArgumentsList (String sFile, int iMajor, int iMinor, int iRelease, int iCompilation)
	{
		bValid = false ;
		this.sFileName = sFile ;
		this.iMajorVersionNumber = iMajor ;
		this.iMinorVersionNumber = iMinor ;
		this.iReleaseVersionNumber = iRelease ;
		this.iCompilationVersionNumber = iCompilation ;
	}
	public CReturn.eReturnCodes isValid() 
	{
		bValid = false ;
		if (bHelp)
		{
			return CReturn.eReturnCodes.ERR_HELP ;			
		}
		if (sFileName == null)
			return CReturn.eReturnCodes.ERR_UNDEFINED_RCFILE;	
		if ( iMajorVersionNumber == -1 )
			return CReturn.eReturnCodes.ERR_INVALID_MAJOR_VERSION ;
		if ( iMinorVersionNumber == -1 )
			return CReturn.eReturnCodes.ERR_INVALID_MINOR_VERSION ;
		if ( iReleaseVersionNumber == -1 )
			return CReturn.eReturnCodes.ERR_INVALID_RELEASE_VERSION ;
		if ( iCompilationVersionNumber == -1 )
			return CReturn.eReturnCodes.ERR_INVALID_COMPILATION_VERSION ;

		bValid = true ;
		return CReturn.eReturnCodes.ERR_OK ;

	}
	public void stringToVersionNumber(String string) 
	{
		this.sNewVersion = new String (string);
		String versionNumber [] = string.split ("\\.");
		if (versionNumber.length != 4 )
			return ;
		this.iMajorVersionNumber       = Integer.parseInt(versionNumber[0]);
		this.iMinorVersionNumber       = Integer.parseInt(versionNumber[1]);
		this.iReleaseVersionNumber     = Integer.parseInt(versionNumber[2]);
		this.iCompilationVersionNumber = Integer.parseInt(versionNumber[3]);
		
	}
	public String toCommaString() 
	{
		String sformat ="%1$d,%2$d,%3$d,%4$d";								
		return String.format(sformat, this.iMajorVersionNumber,this.iMinorVersionNumber, this.iReleaseVersionNumber, this.iCompilationVersionNumber	);
	}
	public String toPointString() 
	{
		String sformat ="%1$d.%2$d.%3$d.%4$d";								
		return String.format(sformat, this.iMajorVersionNumber,this.iMinorVersionNumber, this.iReleaseVersionNumber, this.iCompilationVersionNumber	);
	}	
	
}
