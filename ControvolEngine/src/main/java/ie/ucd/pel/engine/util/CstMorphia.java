package ie.ucd.pel.engine.util;

public class CstMorphia {

	// Morphia
	private static final String ANNOTATION_PATH = "org.mongodb.morphia.annotations.";
	
	// Entity
	private static final String ENTITY = "Entity";
	private static final String EMBEDDED = "Embedded";
	public static final String ANNOTATION_ENTITY_1 = CstJava.AT + ENTITY;
	public static final String ANNOTATION_EMBEDDED_1 = CstJava.AT + EMBEDDED;
	public static final String ANNOTATION_EMBEDDED_2 = CstJava.AT +ANNOTATION_PATH + EMBEDDED;
	public static final String ANNOTATION_ENTITY_2 = CstJava.AT + ANNOTATION_PATH + ENTITY;
	public static final String IMPORT_ENTITY = CstJava.IMPORT + ANNOTATION_PATH + ENTITY + ";";
	public static final String IMPORT_EMBEDDED = CstJava.IMPORT + ANNOTATION_PATH + EMBEDDED + ";";
	
	// Transient
	private static final String TRANSIENT = "Transient";
	public static final String ANNOTATION_TRANSIENT_1 = CstJava.AT + TRANSIENT;
	public static final String ANNOTATION_TRANSIENT_2 = CstJava.AT + ANNOTATION_PATH + TRANSIENT;
	public static final String IMPORT_TRANSIENT = CstJava.IMPORT + ANNOTATION_PATH + TRANSIENT + ";";
	
	// AlsoLoad
	private static final String ALSOLOAD = "AlsoLoad";
	public static final String ANNOTATION_ALSOLOAD_1 = CstJava.AT + ALSOLOAD;
	public static final String ANNOTATION_ALSOLOAD_2 = CstJava.AT + ANNOTATION_PATH + ALSOLOAD;
	public static final String IMPORT_ALSOLOAD = CstJava.IMPORT + ANNOTATION_PATH + ALSOLOAD + ";";
	
	// NotSaved
	private static final String NOTSAVED = "NotSaved";
	public static final String ANNOTATION_NOTSAVED_1 = CstJava.AT + NOTSAVED;
	public static final String ANNOTATION_NOTSAVED_2 = CstJava.AT + ANNOTATION_PATH + NOTSAVED;
	public static final String IMPORT_NOTSAVED = CstJava.IMPORT + ANNOTATION_PATH + NOTSAVED + ";"; 
	
	// Property
	private static final String PROPERTY = "Property";
	public static final String ANNOTATION_PROPERTY_1 = CstJava.AT + PROPERTY;
	public static final String ANNOTATION_PROPERTY_2 = CstJava.AT + ANNOTATION_PATH + PROPERTY;
	public static final String IMPORT_Property = CstJava.IMPORT + ANNOTATION_PATH + PROPERTY + ";"; 
	
	// PostLoad
	private static final String POSTLOAD = "PostLoad";
	public static final String ANNOTATION_POSTLOAD_1 = CstJava.AT + POSTLOAD;
	public static final String ANNOTATION_POSTLOAD_2 = CstJava.AT + ANNOTATION_PATH + POSTLOAD;
	public static final String IMPORT_POSTLOAD = CstJava.IMPORT + ANNOTATION_PATH + POSTLOAD + ";"; 
	
	// Types
	public static final String JAVA_PRIMITIVE_TYPE_OBJECTS[] 		= {"Void", 	"Boolean", 	"Byte", 	"Short", 	"Integer", 	"Long", 	"Float", 	"Double", 	"Character", 	"String", 	"Object"};
	public static final String JAVA_PRIMITIVE_TYPES[] 				= {"void", 	"boolean", 	"byte", 	"short", 	"int", 		"long", 	"float", 	"double", 	"char", 		null, 		null};
	public static final Boolean OBJECTIFY_PRIMITIVE_TYPE_OBJECTS[]	= {false, 	true, 		true, 		true, 		true, 		true, 		true, 		true, 		false, 			true, 		true};
	
	public static final String VOID_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[0];
	public static final String BOOLEAN_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[1];
	public static final String BYTE_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[2];
	public static final String SHORT_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[3];
	public static final String INTEGER_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[4];
	public static final String LONG_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[5];
	public static final String FLOAT_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[6];
	public static final String DOUBLE_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[7];
	public static final String CHARACTER_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[8];
	public static final String STRING_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[9];
	public static final String OBJECT_TYPE = JAVA_PRIMITIVE_TYPE_OBJECTS[10];

}
