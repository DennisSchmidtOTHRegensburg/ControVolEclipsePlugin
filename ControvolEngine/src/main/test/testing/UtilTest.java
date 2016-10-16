package testing;


public class UtilTest {

	//Paths	
	public static final String WINDOWS         = "C:\\Users\\pc\\runtime-EclipseApplication\\MorphiaTestProject";
	public static final String UNIX			   = "C:/Users/pc/runtime-EclipseApplication/MorphiaTestProject";
	public static final String WINDOWSCOMPLETE = "C:\\Users\\pc\\runtime-EclipseApplication\\MorphiaTestProject\\";
	public static final String UNIXCOMPLETE    = "C:/Users/pc/runtime-EclipseApplication/MorphiaTestProject/";
	
	//Annotations
	public static final String PROPERTY      = "@Property(\"";
	public static final String ANNOTEND    	 = "\")";
	public static final String ALSOLOAD      = "@AlsoLoad(\"";
	public static final String NOTSAVED      = "@NotSaved";
	public static final String TRANSIENT     = "@Tansient";
	public static final String IGNORE        = "@Ignore";
	public static final String IGNORESAVE    = "@IgnoreSave";
	public static final String IGNORELOAD    = "@IgnoreLoad";
	public static final String TYPE_STRING   = "String";
	public static final String TYPE_INTEGER  = "Integer";
	public static final String TYPE_OBJECTID = "ObjectId";
	public static final String TYPE_SHORT    = "Short";
	public static final String TYPE_LONG     = "Long";
	public static final String TYPE_BOOLEAN  = "Boolean";
	public static final String TYPE_BYTE     = "Byte";
	public static final String TYPE_DOUBLE   = "Double";
	public static final String SEMICOLON     = ";";
	public static final String SPACE         = " ";

	public static String makeMorphiaSourceCode(){	
		return ("class Test { " +
				PROPERTY  + "rename1" + ANNOTEND + TYPE_STRING   + SPACE + "att1" + SEMICOLON +
				ALSOLOAD  + "rename2" + ANNOTEND + TYPE_BYTE     + SPACE + "att2" + SEMICOLON +
				TRANSIENT +                        TYPE_DOUBLE   + SPACE + "att3" + SEMICOLON +
				NOTSAVED  + 				   	   TYPE_INTEGER  + SPACE + "att4" + SEMICOLON +
								 			       TYPE_OBJECTID + SPACE + "att5" + SEMICOLON +
				"}" );
		}
	public static String makeMorphiaSourceCode2(){	
		return ("class Test { " +
				PROPERTY  + "rename1" + ANNOTEND + TYPE_INTEGER   + SPACE + "att1" + SEMICOLON +
				                                   TYPE_BYTE     + SPACE + "att2" + SEMICOLON +
				TRANSIENT +                        TYPE_DOUBLE   + SPACE + "att3" + SEMICOLON +
				NOTSAVED  + 				   	   TYPE_OBJECTID  + SPACE + "att4" + SEMICOLON +
				"}" );
		}
	public static String makeObjectifySourceCode(){	
		return ("class Test { " +
				
				ALSOLOAD  	+ "rename1" + ANNOTEND + TYPE_STRING   + SPACE + "att1" + SEMICOLON +
				IGNORE		+                        TYPE_BYTE     + SPACE + "att2" + SEMICOLON +
				IGNORESAVE  + 					     TYPE_DOUBLE   + SPACE + "att3" + SEMICOLON +
				IGNORELOAD  + 					     TYPE_INTEGER  + SPACE + "att4" + SEMICOLON +
								 			         TYPE_OBJECTID + SPACE + "att5" + SEMICOLON +
				"}" );
		}
	public static String makeObjectifySourceCode2(){	
		return ("class Test { " +
				
				                                     TYPE_STRING   + SPACE + "att1" + SEMICOLON +
				IGNORE		+                        TYPE_INTEGER     + SPACE + "att2" + SEMICOLON +
				IGNORESAVE  + 					     TYPE_STRING   + SPACE + "att3" + SEMICOLON +
				IGNORELOAD  + 					     TYPE_INTEGER  + SPACE + "att4" + SEMICOLON +
				"}" );
		}
	
}
