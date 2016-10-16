package ie.ucd.pel.engine.util;


public class Util {
	
	// Necessary to convert a windows path to a unix path for the url-classloader in the crawlergit-class.
	public static String windowsToUnixPath(String path){
		return path.replace(CstJava.WINDOWS_SEPERATOR,CstJava.UNIX_SEPERATOR);
	}
	
	// Necessary to tell the url-classloader that the given path is a directory.
	public static String completeUnixPath(String path){
		if (!path.endsWith(CstJava.UNIX_SEPERATOR)){
			path += CstJava.UNIX_SEPERATOR;
		}
		return path;
	}

	public static String primitiveTypeToPrimitiveTypeObject(String typeName){
		String res = null;
		if (isJavaPrimitiveType(typeName)){
			Integer index = getIndexInArray(typeName, CstObjectify.JAVA_PRIMITIVE_TYPES);
			if (CstObjectify.JAVA_PRIMITIVE_TYPE_OBJECTS.length > index){
				res = CstObjectify.JAVA_PRIMITIVE_TYPE_OBJECTS[index];
			}
		} else if (isJavaPrimitiveTypeObject(typeName)){
			res = typeName;
		}
		return res;
	}

	public static Boolean isJavaPrimitiveType(String typeName){
		return Util.isInArray(Util.removePackageName(typeName), CstObjectify.JAVA_PRIMITIVE_TYPES);
	}

	public static Boolean isJavaPrimitiveTypeObject(String typeName){
		return Util.isInArray(Util.removePackageName(typeName), CstObjectify.JAVA_PRIMITIVE_TYPE_OBJECTS);
	}

	private static Boolean isInArray(String typeName, String[] array){
		return !getIndexInArray(typeName, array).equals(-1);
	}

	private static Integer getIndexInArray(String typeName, String[] array){
		Integer index = -1;
		if (typeName != null){
			for (Integer i = 0 ; i < array.length ; i++){
				if (typeName.equals(array[i])){
					index = i;
				}
			}
		}
		return index;
	}
	
	public static Boolean isBiggerPrimitiveTypeObjects(String typeObject1, String typeObject2){
		Integer indexTypeObject1 = getIndexInArray(Util.removePackageName(typeObject1), CstObjectify.JAVA_PRIMITIVE_TYPE_OBJECTS);
		Integer indexTypeObject2 = getIndexInArray(Util.removePackageName(typeObject2), CstObjectify.JAVA_PRIMITIVE_TYPE_OBJECTS);
		return (indexTypeObject1 > indexTypeObject2); 
	}

	public static Boolean isObjectifyPrimitiveTypeObject(String typeName){
		Boolean res = false;
		Integer index = getIndexInArray(Util.removePackageName(typeName), CstObjectify.JAVA_PRIMITIVE_TYPE_OBJECTS);
		if (index != -1){
			if (CstObjectify.OBJECTIFY_PRIMITIVE_TYPE_OBJECTS.length > index){
				res = CstObjectify.OBJECTIFY_PRIMITIVE_TYPE_OBJECTS[index];
			}
		}
		return res;
	}

	public static String addPackageName(String typeName){
		String res = typeName;
		if (Util.isJavaPrimitiveTypeObject(typeName)){
			if (!typeName.startsWith(CstJava.JAVA_LANG_PACKAGE)){
				res = CstJava.JAVA_LANG_PACKAGE + typeName;
			}
		}
		return res;
	}

	public static String removePackageName(String typeName){
		String res = typeName;
		if (typeName != null){
			if (typeName.startsWith(CstJava.JAVA_LANG_PACKAGE)){
				res = typeName.replace(CstJava.JAVA_LANG_PACKAGE, "");
			}
		}
		return res;
	}

}
