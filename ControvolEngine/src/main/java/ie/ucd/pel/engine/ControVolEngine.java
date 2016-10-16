package ie.ucd.pel.engine;

import ie.ucd.pel.datastructure.MApplication;
import ie.ucd.pel.datastructure.MAttribute;
import ie.ucd.pel.datastructure.MEntity;
import ie.ucd.pel.datastructure.evolution.AddAttribute;
import ie.ucd.pel.datastructure.evolution.AddEntity;
import ie.ucd.pel.datastructure.evolution.CleanRenameAttribute;
import ie.ucd.pel.datastructure.evolution.DeleteAttribute;
import ie.ucd.pel.datastructure.evolution.DeleteEntity;
import ie.ucd.pel.datastructure.evolution.Evolution;
import ie.ucd.pel.datastructure.evolution.Operation;
import ie.ucd.pel.datastructure.evolution.RenameAttribute;
import ie.ucd.pel.datastructure.evolution.RetypeAttribute;
import ie.ucd.pel.datastructure.evolution.WildRenameAttribute;
import ie.ucd.pel.datastructure.warning.AttributeCleaningNeeded;
import ie.ucd.pel.datastructure.warning.AttributeDeleted;
import ie.ucd.pel.datastructure.warning.AttributeImproperlyRenamed;
import ie.ucd.pel.datastructure.warning.CastApproximation;
import ie.ucd.pel.datastructure.warning.EntityCleaningNeeded;
import ie.ucd.pel.datastructure.warning.IWarning;
import ie.ucd.pel.datastructure.warning.IncompatibleTypes;
import ie.ucd.pel.datastructure.warning.PrecisionLoss;
import ie.ucd.pel.engine.checking.TypeChecker;
import ie.ucd.pel.engine.util.Util;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ControVolEngine {

	public ControVolEngine(){}

	public Evolution getEvolution(MApplication appV2, MApplication appV1){
		Evolution evol = new Evolution();
		String version1 = appV1.getVersion();
		String version2 = appV2.getVersion();
		// Exploration of the application V2 to check if: 
		// - entities were added, 
		// - attributes were added, 
		// - attributes were renamed, 
		// - attributes were re-types.
		for (MEntity entityV2 : appV2.getEntities()){
			String nameEntityV2 = entityV2.getEntityName();
			// Added entity?
			if (!appV1.containsEntity(nameEntityV2)){
				evol.add(new AddEntity(entityV2, version2, version1));
			} else {
				MEntity entityV1 = appV1.getEntity(nameEntityV2);
				for (MAttribute attributeV2 : entityV2.getAttributes()){
					// Added attribute?
					Boolean existedBefore = entityV1.containsAttribute(attributeV2.getName());
					Boolean isLoaded= attributeV2.isLoaded();
					Boolean renamed = false;
					MAttribute formerAttributeV2 = entityV2.getAttribute(attributeV2.getName()); // XXX Check if it's correct
					String formerNameAttributeV2 = attributeV2.getName(); 
					if (!existedBefore){ 
						for (String formerNameAttributeV2aux : attributeV2.getFormerNames()){
							if (entityV1.containsAttribute(formerNameAttributeV2aux)){
								renamed = true;
								formerNameAttributeV2 = formerNameAttributeV2aux;
							}
						}
						if (renamed){
							if (entityV2.getEntityName().equals(attributeV2.getLocation().getClassName())){ // XXX Added
								Operation op = new CleanRenameAttribute(entityV2, attributeV2, formerAttributeV2, version2, version1);			
								evol.add(op);
							}
						} else {
							if (entityV2.getEntityName().equals(attributeV2.getLocation().getClassName())){ // XXX Added
								Operation op = new AddAttribute(entityV2, attributeV2, version2, version1);
								evol.add(op);
							} 
						}
					}
					//only consider attributes which are mapped into the datastore
					if(isLoaded){ 
					if (existedBefore || renamed){
						// Re-typed attribute?
						String nameAttribute = formerNameAttributeV2;
						MAttribute attributeV1 = entityV1.getAttribute(nameAttribute);
						if (! attributeV2.getType().equals(attributeV1.getType())){ 
							if (entityV2.getEntityName().equals(attributeV2.getLocation().getClassName())){ 
								Operation op = new RetypeAttribute(entityV2, attributeV2, attributeV1, version2, version1); 
								evol.add(op);
							}
						}
						}
					}
				}
			}
		}
		// Exploration of the application v1 to check if:
		// - entities were deleted,
		// - attributes were deleted. 
		for (MEntity entityV1 : appV1.getEntities()){
			// Deleted entity?
			String nameEntityV1 = entityV1.getEntityName();
			if (!appV2.containsEntity(nameEntityV1)){
				evol.add(new DeleteEntity(entityV1, version2, version1));
			} else {
				MEntity entityV2 = appV2.getEntity(nameEntityV1);
				for (MAttribute attributeV1 : entityV1.getAttributes()){
					// Deleted attribute?
					Boolean isDeleted = false;
					Boolean stillExists = entityV2.containsAttribute(attributeV1.getName());
					if (stillExists){
						isDeleted = entityV2.getAttribute(attributeV1.getName()).isDeleted();
					}
					if (!stillExists){ 
						Boolean renamed = false; 
						for (MAttribute newAttDesc : entityV2.getAttributes()){
							renamed = renamed || newAttDesc.getFormerNames().contains(attributeV1.getName());
						}
						if (!renamed && !isDeleted){
							if (entityV1.getEntityName().equals(attributeV1.getLocation().getClassName())){ 
								Operation op = new DeleteAttribute(entityV1, attributeV1, version2, version1);
								evol.add(op);
							}
						} // If it was renamed, it has been treated in the first 'for' loop
					} 
				}
			}
		}
		return evol;
	}
	//compare the latest version to each previous version
	public Evolution getEvolution(List<MApplication> apps){
		Evolution evolution = new Evolution();
		Evolution evolutionAux;
		for (int i = apps.size()-1 ; i > 0 ; i--){
			evolutionAux = this.getEvolution(apps.get(apps.size()-1), apps.get(i-1));
			Boolean res = evolution.addAll(evolutionAux);
			if (!res){
				if (evolutionAux.size() != 0){
					Operation op1 = evolutionAux.first();
					for (Operation op2 : evolution){
						if (op1.compareTo(op2) == 0){
							System.err.println("ControVolEngine.getEvolution(): " + op1 + " " + op2);
						}
				}
			}
		   }
		}
		return evolution;
	}

	/**
	 * Checks only renaming, retyping and deletion of attributes
	 * @param evol set of operations
	 * @return warnings
	 */
	public Set<IWarning> check(Evolution evol){
		return check(evol, false);
	}

	/**
	 * Checks renaming, retyping and deletion of attributes. 
	 * If cleaning is true, it also checks if entities and attributes should be cleaned.
	 * @param evol  set of operations
	 * @param cleaning 
	 * @return warnings
	 */
	public Set<IWarning> check(Evolution evol, Boolean cleaning){
		Set<IWarning> errors = new TreeSet<IWarning>();
		errors.addAll(check1(evol));
		if (cleaning){
			errors.addAll(check2(evol));
			errors.addAll(check3(evol));
		}
		return errors;
	}

	private Set<IWarning> check1(Evolution evol){
		Set<IWarning> warnings = new TreeSet<IWarning>();
		// Retyping of attributes
		Evolution retypes = evol.filter(RetypeAttribute.class);
		for (Operation retypeAux : retypes){
			RetypeAttribute retype = (RetypeAttribute) retypeAux;
			IWarning warning = getTypeWarning(retype);
			if (warning != null){
				warnings.add(warning);
			}
		}
		// Renaming of attributes
		Evolution renames = evol.filter(WildRenameAttribute.class);
		for (Operation renameAux : renames){
			RenameAttribute rename = (RenameAttribute) renameAux;
			IWarning warning = new AttributeImproperlyRenamed(rename);
			warnings.add(warning); 
		}
		// Deletion of attributes
		Evolution deletions = evol.filter(DeleteAttribute.class);
		for (Operation deletionAux : deletions){
			DeleteAttribute deletion = (DeleteAttribute) deletionAux;
			IWarning warning = new AttributeDeleted(deletion); 
			warnings.add(warning); 
		}
		// Addition of attributes
		Evolution additions = evol.filter(AddAttribute.class);
		for (Operation additionAux : additions){
			AddAttribute addition = (AddAttribute) additionAux;
			String additionName = addition.getAttribute().getName();
			String additionType = addition.getAttribute().getType();
			Boolean found = false;
			IWarning elementToRemove = null;
			MAttribute deletedAttribute = null;
			for (IWarning warn : warnings){
				if (warn instanceof AttributeDeleted){
					AttributeDeleted warnAttDel = (AttributeDeleted) warn;
					String deletionName = warnAttDel.getOperation().getAttribute().getName();
					if (additionName.equals(deletionName)){
						found = true;
						elementToRemove = warn;
						deletedAttribute = warnAttDel.getOperation().getAttribute();
					}
				}
			}
			if (found){
				warnings.remove(elementToRemove);
				RetypeAttribute retype = new RetypeAttribute(addition.getEntity(), addition.getAttribute(), deletedAttribute, additionType, deletedAttribute.getType());
				IWarning newWarning = getTypeWarning(retype);
				if (newWarning != null){
					warnings.add(newWarning);
				}
			}
		}
		return warnings;
	}

	public IWarning getTypeWarning(RetypeAttribute retype){
		IWarning warning = null;
		String legacyType = retype.getLegacyAttribute().getType();
		String currentType = retype.getAttribute().getType();

		TypeChecker checker = new TypeChecker();

		Boolean equivalent = false;
		Boolean compatible = false; 
		Boolean bigger = false;
		Boolean parsedCorrectly = false;
		//only consider attributes which are mapped into the datastore
		if((retype.getAttribute().isLoaded())){
		if (Util.isJavaPrimitiveTypeObject(legacyType) && Util.isJavaPrimitiveTypeObject(currentType)){
			equivalent = checker.areEquivalentPrimitiveTypes(currentType, legacyType);
			compatible = checker.isCompatiblePrimitiveTypes(legacyType, currentType); 
			bigger = checker.isBiggerPrimitiveTypes(currentType, legacyType);
			parsedCorrectly = checker.isParsedCorrectlyPrimitiveTypes(legacyType, currentType);
		} else if (!Util.isJavaPrimitiveTypeObject(legacyType) && !Util.isJavaPrimitiveTypeObject(currentType)){
			equivalent = checker.areEquivalent(currentType, legacyType);
			compatible = checker.isCompatible(currentType, legacyType); 
			bigger = checker.isBigger(currentType, legacyType);
			parsedCorrectly = checker.isParsedCorrectly(currentType, legacyType);
		}
		if (!equivalent && !compatible){
			warning = new IncompatibleTypes(retype); 
		} else if (!equivalent && compatible && bigger && !parsedCorrectly){
			warning = new CastApproximation(retype); 
		} else if (!equivalent && compatible && !bigger && !parsedCorrectly){
			warning = new CastApproximation(retype); 
		} else if (!equivalent && compatible && !bigger && parsedCorrectly){
			warning = new PrecisionLoss(retype); 
		}}
		return warning;
	}

	private Set<IWarning> check2(Evolution evol){
		Set<IWarning> errors = new TreeSet<IWarning>();
		Evolution dels = evol.filter(DeleteAttribute.class);
		Evolution adds = evol.filter(AddAttribute.class);
		for (Operation addAux : adds){
			// Addition
			AddAttribute add = (AddAttribute) addAux;
			String addEntityName = add.getEntity().getEntityName();
			String addAttributeName = add.getAttribute().getName();
			String addVersion = add.getVersion2();
			for (Operation delAux : dels){
				// Deletion
				DeleteAttribute del = (DeleteAttribute) delAux;
				String delEntityName = del.getEntity().getEntityName();
				String delAttributeName = del.getAttribute().getName();
				String delVersion = del.getVersion2();

				// Checking
				Boolean cond1 = addEntityName.equals(delEntityName);
				Boolean cond2 = addAttributeName.equals(delAttributeName);
				Boolean cond3 = (addVersion.compareTo(delVersion) > 0);
				if (cond1 && cond2 && cond3){
					errors.add(new AttributeCleaningNeeded((AddAttribute) addAux));
					}

			}
		}
		return errors;
	}

	private Set<IWarning> check3(Evolution evol){
		Set<IWarning> errors = new TreeSet<IWarning>();
		// Deletion + Addition of entities
		Evolution dels = evol.filter(DeleteEntity.class);
		Evolution adds = evol.filter(AddEntity.class);
		for (Operation addAux : adds){
			// Addition
			AddEntity add = (AddEntity) addAux;
			String addEntityName = add.getEntity().getEntityName();
			String addVersion = add.getVersion2();
			for (Operation delAux : dels){
				// Deletion
				DeleteEntity del = (DeleteEntity) delAux;
				String delEntityName = del.getEntity().getEntityName();
				String delVersion = del.getVersion2();

				// Checking
				if (addEntityName.equals(delEntityName) && (addVersion.compareTo(delVersion) > 0)){
					IWarning error = new EntityCleaningNeeded((AddEntity) addAux); 
					errors.add(error); 
				}

			}
		}
		return errors;
	}

}
