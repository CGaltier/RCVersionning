import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.DoubleBuffer;


public class RCVersionning 
{
	public static final String sHelpArg = new String ("/?");
	public static final String sVersion = new String ("/v");
	public static final String sFile = new String ("/f");
	public static final String sDirectory = new String ("/d");
	public static final String sReplace = new String ("/r");
	
	
	public static void main (String[]args)
	{
		//System.out.println ( System.getProperty("java.classpath"))	
		
		CArgumentsList argumentsList = null;
		argumentsList = processArguments (args);
		displayArguments (argumentsList);
		//System.out.println("Hello World");
		CReturn retValue = new CReturn () ;
		retValue.eReturnValue = argumentsList.isValid() ; 
		if (retValue.eReturnValue == CReturn.eReturnCodes.ERR_OK)
		{
			retValue = doRCVersionning (argumentsList);
		}
		retValue.displayReturnMessage();
	}



	private static  CReturn doRCVersionning(CArgumentsList argumentsList2)
	{
		CReturn.eReturnCodes eRCode = CReturn.eReturnCodes.ERR_OK ;

		
		String outputFile = argumentsList2.sFileName+"_out";
		eRCode = processFile (argumentsList2,outputFile);
		
		if ( eRCode != CReturn.eReturnCodes.ERR_OK)
			return new CReturn (eRCode) ;
		
		eRCode = swapFiles(argumentsList2.sFileName,outputFile);
		return new CReturn (eRCode) ;	

	}

	private static CReturn.eReturnCodes swapFiles(String oldFile, String newFile) 
	{
		try
		{
			String oldFileBackedUpName = oldFile+"bak";
			File fileToBackupTo = new File (oldFileBackedUpName);
			File fileToBackup = new File (oldFile);
			File fileToRename = new File (newFile);
			System.out.println("----------------------------------------------------------");
			System.out.println ("Moving "+oldFile+" to "+oldFileBackedUpName );
			fileToBackup.renameTo(fileToBackupTo);

			System.out.println ("Moving "+newFile+" to "+oldFile );
			fileToRename.renameTo(fileToBackup);
			
			System.out.println ("Deleting "+oldFileBackedUpName);
			fileToBackupTo.deleteOnExit();
			
		}
		catch (NullPointerException e)
		{
			e.printStackTrace();
			return  CReturn.eReturnCodes.ERR_FILERENAMINGERROR ;			
		}
		catch (SecurityException e)
		{
			return  CReturn.eReturnCodes.ERR_FILESECURITYERROR;
		}
		return CReturn.eReturnCodes.ERR_OK;
	}



	private static CReturn.eReturnCodes processFile	(CArgumentsList argumentsList2, String outputFile)
	{
		String BlockVersionInfotag = "VERSIONINFO";
		String FileVersionTag="FILEVERSION";
		String ProductVersionTag="PRODUCTVERSION";
		String BlockValueTag="VALUE";
		String StringInfoFileVersionTag="\"FileVersion\"";
		String StringInfoProductVersionTag="\"ProductVersion\"";
		/*String BlockStartTag="BEGIN";
		String BlockEndTag="END";
		String BlockTag="BLOCK";*/
		
		String CommaVersionString = argumentsList2.toCommaString();
		String PointVersionString = argumentsList2.toPointString();

		boolean bInBlock = false ;
		boolean bStringProductVersion  = false ;
		boolean bStringFileVersion = false ;
		boolean bProductVersion  = false ;
		boolean bFileVersion = false ;
		String strLine = null ;
		String strOut = null;
		String EoL = System.getProperty("line.separator");

		try 
		{
			String encoding = FileUtils.getEncoding (argumentsList2.sFileName);
			//BufferedReader in  = new BufferedReader (new FileReader(argumentsList2.sFileName));
			BufferedReader in  = new BufferedReader (new InputStreamReader(new FileInputStream (argumentsList2.sFileName),encoding));
			//BufferedWriter out = new BufferedWriter (new FileWriter(outputFile));
			BufferedWriter out = new BufferedWriter (new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
			

			while ((strLine = in.readLine())!=null)
			{
				String strWithoutFirstBlanks = strLine.trim();
				if (strWithoutFirstBlanks.endsWith(BlockVersionInfotag))
				{
					bInBlock=true;
					strOut = strLine ;
				}
				else
				{
					if (bInBlock)
					{					
						if (bStringProductVersion && bStringFileVersion && bProductVersion && bFileVersion)
						{
							bInBlock=false;
							strOut = strLine ;
						}
						else						
						{
							String strComponents [];
							if (strWithoutFirstBlanks.startsWith(BlockValueTag))
								strComponents = strWithoutFirstBlanks.split("(\\s++|,++)++");						
							else
								strComponents = strWithoutFirstBlanks.split("\\s++");							
							if (strComponents.length >= 2)
							{
								if (strComponents[0].contentEquals(FileVersionTag))
								{
									System.out.print ("Replacing "+strLine);
									strOut = strLine.replace(strComponents[1], CommaVersionString);
									System.out.println (" with "+strOut);
									bFileVersion=true;
								}
								else if(strComponents[0].contentEquals(ProductVersionTag))
								{
									System.out.print ("Replacing "+strLine);
									strOut = strLine.replace(strComponents[1], CommaVersionString);
									System.out.println (" with "+strOut);
									bProductVersion=true;
								}
								else if (strComponents[1].contentEquals(StringInfoFileVersionTag))
								{
									System.out.print ("Replacing "+strLine);
									strOut = strLine.replace(strComponents[2], "\""+PointVersionString+"\"");
									System.out.println (" with "+strOut);
									bStringFileVersion=true;
								}
								else if (strComponents[1].contentEquals(StringInfoProductVersionTag))
								{
									System.out.print ("Replacing "+strLine);
									strOut = strLine.replace(strComponents[2], "\""+PointVersionString+"\"");
									System.out.println (" with "+strOut);
									bStringProductVersion=true;
								}
								else
								{
									strOut = strLine ;
								}
							}
							else
							{
								strOut = strLine ;
							}
						}
					}
					else
					{
						strOut = strLine ;
					}
				}			
				out.append(strOut);
				out.write(EoL);
			}
			in.close();
			out.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return  CReturn.eReturnCodes.ERR_FILENOTFOUND ;
		}
		catch (IOException e) 
		{
			e.printStackTrace();
			return  CReturn.eReturnCodes.ERR_FILEERROR ;
		}
		return CReturn.eReturnCodes.ERR_OK ;
	}



	private static  void displayArguments(CArgumentsList argumentsList2) 
	{
		// TODO Auto-generated method stub
		System.out.println("==========================================================");
		System.out.println ("RCVersionning copyright C.Galtier 2012");
		System.out.println ("Editing file :"+argumentsList2.sFileName);
		System.out.println ("Version number :"+argumentsList2.sNewVersion);
		System.out.println("==========================================================");
		
	}

	public static  CArgumentsList processArguments(String[] args) 
	{
		//System.out.println("arguments");
		
		CArgumentsList argRet = new CArgumentsList ();
		int iArg ;
		for ( iArg = 0 ; iArg < args.length; iArg++)
		{
			
			//System.out.print(iArg);
			//System.out.println(" : "+args[iArg]);
			
			if (args[iArg].equalsIgnoreCase(sHelpArg))
			{
				argRet.bHelp = true ;
			}
			if (args[iArg].equalsIgnoreCase(sVersion))
			{
				iArg++;				
				
				//System.out.print(iArg);
				//System.out.println(" : "+args[iArg]);
				
				argRet.stringToVersionNumber(args[iArg]);
			}
			if (args[iArg].equalsIgnoreCase(sFile))
			{
				iArg++;				
				
				//System.out.print(iArg);
				//System.out.println(" : "+args[iArg]);
				
				argRet.sFileName = new String  ( args[iArg] );
			}

		}
		return argRet; 
		
	}
	
}
