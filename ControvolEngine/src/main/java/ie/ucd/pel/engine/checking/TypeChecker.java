package ie.ucd.pel.engine.checking;

import ie.ucd.pel.engine.util.CstObjectify;
import ie.ucd.pel.engine.util.Util;

public class TypeChecker {

	public TypeChecker(){}

	//*******************
	//** GENERAL CASES **
	//*******************

	public Boolean areEquivalent(Object object1, Object object2){
		String type1 = object1.getClass().getName();
		String type2 = object2.getClass().getName();
		Boolean res = type1.equals(type2);
		System.out.println("areEq " + type1 + " " + type2);
		if (!res){
			Boolean cond1a = Util.isJavaPrimitiveType(type1);
			Boolean cond1b = Util.isJavaPrimitiveTypeObject(type1);
			Boolean cond2a = Util.isJavaPrimitiveType(type2);
			Boolean cond2b = Util.isJavaPrimitiveTypeObject(type2);
			if ((cond1a || cond1b) && (cond2a || cond2b)){
				res = areEquivalentPrimitiveTypes(type1, type2);
				
			}
		}
		return res;
	}

	public Boolean isCompatible(Object object1, Object object2){
		Boolean res = false;
		String type1 = object1.getClass().getName();
		String type2 = object2.getClass().getName();
		String typeObject1 = Util.primitiveTypeToPrimitiveTypeObject(type1);
		typeObject1 = Util.removePackageName(typeObject1);
		String typeObject2 = Util.primitiveTypeToPrimitiveTypeObject(type2);
		typeObject2 = Util.removePackageName(typeObject2);
		if (Util.isJavaPrimitiveTypeObject(typeObject1) && Util.isJavaPrimitiveTypeObject(typeObject2)){
			res = areEquivalentPrimitiveTypes(type1, type2);
			if (!res){
				res = isCompatiblePrimitiveTypes(type1, type2);
			}
		} else if (!Util.isJavaPrimitiveTypeObject(typeObject1) && !Util.isJavaPrimitiveTypeObject(typeObject2)){
			// TODO Implement for non-primitive types

		}
		return res;
	}

	public Boolean isBigger(Object object1, Object object2){
		Boolean res = false;
		String type1 = object1.getClass().getName();
		String type2 = object2.getClass().getName();
		String typeObject1 = Util.primitiveTypeToPrimitiveTypeObject(type1);
		typeObject1 = Util.removePackageName(typeObject1);
		String typeObject2 = Util.primitiveTypeToPrimitiveTypeObject(type2);
		typeObject2 = Util.removePackageName(typeObject2);
		if (Util.isJavaPrimitiveTypeObject(typeObject1) && Util.isJavaPrimitiveTypeObject(typeObject2)){
			res = areEquivalentPrimitiveTypes(type1, type2);
			if (!res){
				res = isBiggerPrimitiveTypes(type1, type2);
			}
		} else if (!Util.isJavaPrimitiveTypeObject(typeObject1) && !Util.isJavaPrimitiveTypeObject(typeObject2)){
			// TODO Implement for non-primitive types

		}
		return res;
	}

	public Boolean isParsedCorrectly(Object object1, Object object2){
		Boolean res = false;
		String type1 = object1.getClass().getName();
		String type2 = object2.getClass().getName();
		String typeObject1 = Util.primitiveTypeToPrimitiveTypeObject(type1);
		typeObject1 = Util.removePackageName(typeObject1);
		String typeObject2 = Util.primitiveTypeToPrimitiveTypeObject(type2);
		typeObject2 = Util.removePackageName(typeObject2);
		if (Util.isJavaPrimitiveTypeObject(typeObject1) && Util.isJavaPrimitiveTypeObject(typeObject2)){
			res = areEquivalentPrimitiveTypes(type1, type2);
			if (!res){
				res = isParsedCorrectlyPrimitiveTypes(type1, type2);
			}
		} else if (!Util.isJavaPrimitiveTypeObject(typeObject1) && !Util.isJavaPrimitiveTypeObject(typeObject2)){
			// TODO Implement for non-primitive types

		}
		return res;
	}

	//***************************
	//** PRIMITIVE TYPES CASES **
	//***************************

	public Boolean areEquivalentPrimitiveTypes(String type1, String type2){
		Boolean res = false;
		String typeObject1 = Util.primitiveTypeToPrimitiveTypeObject(type1);
		typeObject1 = Util.removePackageName(typeObject1);
		String typeObject2 = Util.primitiveTypeToPrimitiveTypeObject(type2);
		typeObject2 = Util.removePackageName(typeObject2);
		if (typeObject1 != null){
			res = typeObject1.equals(typeObject2);
		} else {
			res = (typeObject2 == null);
		}
		return res;
	}
	
	public Boolean isCompatiblePrimitiveTypes(String type1, String type2){
		String typeObject1 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type1));
		String typeObject2 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type2));
		Boolean res = typeObject1.equals(typeObject2);
		if (!res){
			res = typeObject2.equals(CstObjectify.STRING_TYPE) || typeObject2.equals(CstObjectify.OBJECT_TYPE);
			if (!res){
				res = !(typeObject1.equals(CstObjectify.BOOLEAN_TYPE) || typeObject1.equals(CstObjectify.STRING_TYPE) || typeObject2.equals(CstObjectify.BOOLEAN_TYPE));
			}
		}
		return res;
	}

	// current, legacy
	public Boolean isBiggerPrimitiveTypes(String type1, String type2){
		String typeObject1 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type1));
		String typeObject2 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type2));
		Boolean cond1 = Util.isBiggerPrimitiveTypeObjects(typeObject1, typeObject2);
		Boolean cond2 = typeObject1.equals(CstObjectify.STRING_TYPE) && typeObject2.equals(CstObjectify.FLOAT_TYPE);
		Boolean cond3 = typeObject1.equals(CstObjectify.STRING_TYPE) && typeObject2.equals(CstObjectify.DOUBLE_TYPE);
		Boolean res = cond1 && !cond2 && !cond3;
		//System.out.println(typeObject2 + " to " + typeObject1 + " = " + cond1 + " && !" + cond2 + " && !" + cond3);
		return res;
	}

	public Boolean isParsedCorrectlyPrimitiveTypes(String type1, String type2){
		String typeObject1 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type1));
		String typeObject2 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type2));
		Boolean res = typeObject1.equals(typeObject2) || !typeObject2.equals(CstObjectify.OBJECT_TYPE);
		return res;
	}
	
	//*************************
	//** COMPLEX TYPES CASES **
	//*************************
	
	public Boolean areEquivalentComplexTypes(Object object1, Object object2){
		Class<?> class1 = object1.getClass();
		Class<?> class2 = object2.getClass();
		return class1.equals(class2);
	}
	
	public Boolean isCompatibleComplexTypes(Object object1, Object object2){
		Class<?> class1 = object1.getClass();
		Class<?> class2 = object2.getClass();
		return class2.isAssignableFrom(class1);
	}

	public Boolean isBiggerComplexTypes(String type1, String type2){
		String typeObject1 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type1));
		String typeObject2 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type2));
		Boolean cond1 = Util.isBiggerPrimitiveTypeObjects(typeObject1, typeObject2);
		Boolean cond2 = typeObject2.equals(CstObjectify.STRING_TYPE);
		Boolean cond3 = typeObject1.equals(CstObjectify.FLOAT_TYPE) || typeObject1.equals(CstObjectify.DOUBLE_TYPE);
		return cond1 || (cond2 && cond3);
	}

	public Boolean isParsedCorrectlyComplexTypes(String type1, String type2){
		String typeObject1 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type1));
		String typeObject2 = Util.removePackageName(Util.primitiveTypeToPrimitiveTypeObject(type2));
		Boolean res = typeObject1.equals(typeObject2) || !typeObject2.equals(CstObjectify.OBJECT_TYPE);
		return res;
	}

}
