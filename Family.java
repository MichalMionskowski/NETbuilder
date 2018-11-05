package familyTree;

import java.util.ArrayList;
import java.util.Arrays;

public class Family {

//	public static void main(String[] args) {
//		Family fam = new Family();
//		System.out.println(fam.setParentOf("Frank", "Morgan"));
//		System.out.println(fam.setParentOf("Frank", "Dylan"));
//		System.out.println(fam.male("Dylan"));
//		System.out.println(fam.setParentOf("Joy", "Frank"));
//		System.out.println(fam.male("Frank"));
//		System.out.println(fam.male("Morgan"));
//		System.out.println(fam.setParentOf("July", "Morgan"));
//		System.out.println(fam.isMale("Joy") || fam.isFemale("Joy"));
//		System.out.println(fam.getChildrenOf("Morgan"));
//		System.out.println(fam.setParentOf("Jennifer", "Morgan"));
//		System.out.println(fam.getChildrenOf("Morgan"));
//		System.out.println(fam.getChildrenOf("Dylan"));
//		System.out.println(fam.getParentsOf("Frank"));
//		System.out.println(fam.setParentOf("Morgan", "Frank"));
//	}

	private ArrayList<Person> familyMembers = new ArrayList<Person>();

	private Person findMember(String name) {
		for (Person member : this.familyMembers) {
			if (member.getName().equals(name)) {
				return member;
			}
		}

		return null;
	}

	// returns true if a person already exists in the family tree
	private boolean inFamilyTree(String name) {
		return findMember(name) != null ? true : false;
	}

	// checks whether the gender has been set
	private boolean genderDefined(String name) {
		return findMember(name).getGender() != Gender.undefined ? true : false;
	}

	// checks if person with name "name" has children
	private boolean hasChildren(String name) {
		return findMember(name).getChildren().size() > 0 ? true : false;
	}

	// returns gender of second parent
	// @child = child name
	// @parent = first known parent
	private Gender findGender(String child, String parent) {
		Person parent1 = findMember(child).getParents()[0];
		Person parent2 = findMember(child).getParents()[1];
		if (parent1.getName().equals(parent)) {
			return parent2.getGender();
		} else {
			return parent1.getGender();
		}
	}

	// checks if child has both parents set
	private boolean hasBothParents(Person child) {
		return child.getParents()[1] != null ? true : false;
	}

	// adds the members to family tree if needed and then checks whether this
	// relationship is allowed
	private boolean parentSetup(String childName, String parentName) {
		boolean checkAncestors = true;
		if (!inFamilyTree(parentName)) {
			Person parent = new Person(parentName);
			familyMembers.add(parent);
			checkAncestors = false;
		}

		if (!inFamilyTree(childName)) {
			Person child = new Person(childName);
			familyMembers.add(child);
			checkAncestors = false;
		}
		return checkAncestors ? checkIfOwnAncestor(childName, parentName) : false;
	}

	// checks child nodes for invalid relationship
	private boolean checkLowerNodes(Person child, Person parent) {
		// currentMemmbers holds all current members at this level of a tree
		ArrayList<Person> currentMembers = new ArrayList<Person>();
		// add members is temporary storage for nodes to be checked in the next loop
		ArrayList<Person> addMembers = new ArrayList<Person>();
		// always start with the child node and check for its children first
		currentMembers.add(child);
		boolean isChild = false;
		while (!isChild) {
			// loops through each member of the currentMember list, these were
			// the children specified in the last loop
			for (Person curMember : currentMembers) {
				addMembers.clear();
				// adds all the child nodes to be added for checking in the next loop
				if (curMember.getChildren() != null) {
					for (Person leaf : curMember.getChildren()) {
						if (leaf.getName().equals(child.getName()) || leaf.getName().equals(parent.getName())) {
							return true;
						} else {
							addMembers.add(leaf);
						}
					}
				}
			}
			// I currentMember are being clear as these have already been checked
			currentMembers.clear();
			// if there are no nodes to check we know that we have reached the bottom of the
			// tree and there is nothing else to be checked
			if (addMembers.size() == 0) {
				isChild = true;
			}
			// of there are more nodes to be checked then add them to the current members
			// and check them in the next loop
			currentMembers.addAll(addMembers);
		}
		return false;
	}

	// checks parent nodes for invalid relationship
	private boolean checkUpperNodes(Person child, Person parent) {
		ArrayList<Person> currentMembers = new ArrayList<Person>();
		ArrayList<Person> addMembers = new ArrayList<Person>();
		currentMembers.add(child);
		boolean isParent = false;
		while (!isParent) {
			for (Person curMember : currentMembers) {
				addMembers.clear();
				if (curMember.getParents() != null) {
					// if parents is not null then we know that the first parent position has been
					// filled
					Person parent1 = curMember.getParents()[0];
					if (parent1.getName().equals(child.getName())) {
						return true;
					} else {
						addMembers.add(parent1);
					}

					if (hasBothParents(curMember)) {
						Person parent2 = curMember.getParents()[1];
						if (parent2.getName().equals(child.getName())) {
							return true;
						} else {
							addMembers.add(parent2);
						}

					}
				}
			}
			currentMembers.clear();
			if (addMembers.isEmpty()) {
				isParent = true;
			}
			currentMembers.addAll(addMembers);
		}
		return false;
	}

	// checks whether the child or parent are its' ancestors
	private boolean checkIfOwnAncestor(String childName, String ParentName) {
		Person childN = findMember(childName);
		Person parentName = findMember(ParentName);
		return checkLowerNodes(childN, parentName) || checkUpperNodes(childN, parentName) ? true : false;
	}

	// adds a male member to the family if this member is not assigned
	// this performs all the necessary checks needed
	public boolean male(String name) {
		Person member = findMember(name);
		if (inFamilyTree(name)) {
			// if the gender is defined then we cannot assign a new one
			if (genderDefined(name)) {
				return false;
			} else {
				// if member has children then if second parent exits we can check for their gender and 
				// assign a gender to the member
				if (hasChildren(name)) {
					for (Person child : member.getChildren()) {
						// in here we check which parent is the member we are trying to add
						if (hasBothParents(child)) {
							Person parent1 = child.getParents()[0];
							// this makes sure that parent2 is always the second parent, different to the member
							Person parent2 = parent1.getName().equals(name) ? child.getParents()[1] : parent1;
							switch (findGender(child.getName(), member.getName())) {
							case undefined:
								member.setGender(Gender.male);
								parent2.setGender(Gender.female);
								return true;
							case male:
								member.setGender(Gender.female);
								return false;
							case female:
								member.setGender(Gender.male);
								return true;
							}
						} else {
							member.setGender(Gender.male);
							return true;
						}
					}
				} else {
					findMember(name).setGender(Gender.male);
					return true;
				}
			}
		} else {
			// if the member is new then add it to the familyMembers arrayList
			member = new Person(name);
			member.setGender(Gender.male);
			familyMembers.add(member);
			return true;
		}
		return false;
	}

	// adds a female member to the family if this member is not assigned
	public boolean female(String name) {
		Person member = findMember(name);
		if (inFamilyTree(name)) {
			if (genderDefined(name)) {
				return false;
			} else {
				if (hasChildren(name)) {
					for (Person child : member.getChildren()) {
						if (hasBothParents(child)) {
							Person parent1 = child.getParents()[0];
							Person parent2 = parent1.getName().equals(name) ? child.getParents()[1] : parent1;
							switch (findGender(child.getName(), member.getName())) {
							case undefined:
								member.setGender(Gender.female);
								parent2.setGender(Gender.male);
								return true;
							case male:
								member.setGender(Gender.female);
								return true;
							case female:
								member.setGender(Gender.male);
								return false;
							}
						} else {
							member.setGender(Gender.female);
							return true;
						}
					}
				} else {
					findMember(name).setGender(Gender.female);
					return true;
				}
			}
		} else {
			member = new Person(name);
			member.setGender(Gender.female);
			familyMembers.add(member);
			return true;
		}
		return false;
	}

	// returns true if the family member is male
	public boolean isMale(String name) {
		Person member = findMember(name);
		if (inFamilyTree(name)) {
			if (member.getGender() == Gender.male) {
				return true;
			}
		} else {
			member = new Person(name);
			familyMembers.add(member);
		}
		return false;
	}

	// returns true if the family member is female
	public boolean isFemale(String name) {
		Person member = findMember(name);
		if (inFamilyTree(name)) {
			if (member.getGender() == Gender.female) {
				return true;
			}
		} else {
			member = new Person(name);
			familyMembers.add(member);
		}
		return false;
	}

	// sets up a new family relation, returns false if this relationship has
	// been made previously
	public boolean setParentOf(String childName, String parentName) {
		if (parentSetup(childName, parentName)) {
			return false;
		} else {
			Person newParent = findMember(parentName);
			Person child = findMember(childName);
			// if no parents have been assigned to child then add this parent
			if (child.getParents() == null) {
				child.setParents(newParent);
				newParent.addChild(child);
				return true;
			} else {
				if (hasBothParents(child)) {
					return false;
				} else {
					// at this point we know there is only one parent in the parent list for child 
					Person parent1 = child.getParents()[0];
					if (!parent1.getName().equals(parentName)
							&& (!genderDefined(parent1.getName()) || (parent1.getGender() != newParent.getGender()))) {
						child.setParents(newParent);
						newParent.addChild(child);
						if (genderDefined(parent1.getName())) {
							newParent.setGender(parent1.getGender() == Gender.male ? Gender.female : Gender.male);
						} else if (genderDefined(newParent.getName())) {
							parent1.setGender(newParent.getGender() == Gender.male ? Gender.female : Gender.male);
						}
						return true;
					}
					return false;
				}
			}
		}
	}

	// returns parents of this child
	public String getParentsOf(String name) {
		Person parents = findMember(name);
		String output[] = new String[2];
		if (parents != null) {
			int i = 0;
			for (Person p : parents.getParents()) {
				// the parent must not be null
				if (p != null) {
					output[i] = p.getName();
					i++;
				}
				if (i == 2) {
					Arrays.sort(output);
				}
			}
		}
		return Arrays.toString(output);
	}

	public String getChildrenOf(String name) {
		String output[] = new String[findMember(name).getChildren().size()];
		int i = 0;
		for (Person p : findMember(name).getChildren()) {
			output[i] = p.getName();
			i++;
		}
		Arrays.sort(output);
		return Arrays.toString(output);
	}

}
