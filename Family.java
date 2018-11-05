package familyTree;

import java.util.ArrayList;
import java.util.Arrays;

public class Family {
	
	private ArrayList<Person> familyMembers = new ArrayList<Person>();

	// adds a male member to the family if this member is not assigned
	// this performs all the necessary checks needed
	public boolean male(String name) {
		if (findMember(name) != null) {
			if (findMember(name).getGender() == Gender.undefined) {
				if (findMember(name).getChildren().size() > 0) {
					for (Person member : findMember(name).getChildren()) {
						if (member.getParents()[0].getName().equals(name)) {
							if (member.getParents()[1] != null) {
								if (member.getParents()[1].getGender() != Gender.undefined) {
									if (member.getParents()[1].getGender() == Gender.male) {
										member.getParents()[0].setGender(Gender.female);
										return false;
									} else {
										member.getParents()[0].setGender(Gender.male);
										return true;
									}
								} else {
									member.getParents()[0].setGender(Gender.male);
									member.getParents()[1].setGender(Gender.female);
									return true;
								}
							} else {
								member.getParents()[0].setGender(Gender.male);
								return true;
							}
						} else {
							if (member.getParents()[0].getGender() != Gender.undefined) {
								if (member.getParents()[0].getGender() == Gender.male) {
									member.getParents()[1].setGender(Gender.female);
									return false;
								} else {
									member.getParents()[1].setGender(Gender.male);
									return true;
								}
							} else {
								member.getParents()[0].setGender(Gender.female);
								member.getParents()[1].setGender(Gender.male);
								return true;
							}
						}
					}
				} else {
					findMember(name).setGender(Gender.male);
					return true;
				}
				return false;
			} else {
				return false;
			}
		} else {

			Person newMember = new Person(name);
			newMember.setGender(Gender.male);
			familyMembers.add(newMember);
			return true;
		}
	}

	// adds a female member to the family if this member is not assigned
	public boolean female(String name) {
		if (findMember(name) != null) {
			if (findMember(name).getGender() == Gender.undefined) {
				if (findMember(name).getChildren().size() > 0) {
					for (Person member : findMember(name).getChildren()) {
						if (member.getParents()[0].getName().equals(name)) {
							if (member.getParents()[1] != null) {
								if (member.getParents()[1].getGender() != Gender.undefined) {
									if (member.getParents()[1].getGender() == Gender.male) {
										member.getParents()[0].setGender(Gender.female);
										return false;
									} else {
										member.getParents()[0].setGender(Gender.male);
										return true;
									}
								} else {
									member.getParents()[0].setGender(Gender.female);
									member.getParents()[1].setGender(Gender.male);
									return true;
								}
							} else {
								member.getParents()[0].setGender(Gender.female);
								return true;
							}
						} else {
							if (member.getParents()[0].getGender() != Gender.undefined) {
								if (member.getParents()[0].getGender() == Gender.male) {
									member.getParents()[1].setGender(Gender.female);
									return false;
								} else {
									member.getParents()[1].setGender(Gender.male);
									return true;
								}
							} else {
								member.getParents()[0].setGender(Gender.male);
								member.getParents()[1].setGender(Gender.female);
								return true;
							}
						}
					}
				} else {
					findMember(name).setGender(Gender.female);
					return true;
				}
			} else {
				System.out.printf("%s already has a gender\n", name);
				return false;
			}
			return false;
		} else {
			Person newMember = new Person(name);
			newMember.setGender(Gender.male);
			familyMembers.add(newMember);
			return true;
		}
	}

	// returns true if the family member is male
	public boolean isMale(String name) {
		Person member = findMember(name);
		if (member != null) {
			if (member.getGender() == Gender.male) {
				return true;
			}
		} else {
			Person newMember = new Person(name);
			familyMembers.add(newMember);
		}
		return false;
	}

	// returns true if the family member is female
	public boolean isFemale(String name) {
		Person member = findMember(name);
		if (member != null) {
			if (member.getGender() == Gender.female) {
				return true;
			}
		} else {
			Person newMember = new Person(name);
			familyMembers.add(newMember);
		}
		return false;
	}

	// sets up a new family relation, returns false if this relationship has
	// been made previously
	public boolean setParentOf(String childName, String parentName) {
		Person parent = findMember(parentName);
		Person child = findMember(childName);

		// check if this parent exists if not then initialise it
		if (parent == null) {
			parent = new Person(parentName);
			this.familyMembers.add(parent);
		}

		if (child == null) {
			child = new Person(childName);
			this.familyMembers.add(child);
		}

		Person[] parents = child.getParents();
		if (!checkIfOwnAncestor(childName, parentName)) {
			System.out.println("nope not aallowd");
			return false;
		}
		// if the child has no added parents then add it to the first one and check for
		// gender

		if (parents == null) {
			child.setParents(parent);
			parent.addChild(child);
			if (parent.getGender() == Gender.female) {
				child.setMother(parent);
			} else if (parent.getGender() == Gender.male) {
				child.setFather(parent);
			}
			return true;
		} else if (parents[1] == null && parents[0].getName() != parentName) {
			// if the second parent is being filled we have to check the gender of this
			// parent
			// and the existing one
			child.setParents(parent);
			parent.addChild(child);
			if (parent.getGender() == Gender.female) {
				// in case when the child already has a mother display a message
				if (child.getMother() == null) {
					child.setMother(parent);
					// when the first parent does not specify gender we can assume that it
					// is the opposite gender to this parent
					if (parents[0].getGender() == Gender.undefined) {
						parents[0].setGender(Gender.male);
					}
				} else {
					System.out.printf("%s already has a mother!!", childName);
				}

			} else if (parent.getGender() == Gender.male) {
				if (child.getFather() == null) {
					child.setFather(parent);
					if (parents[0].getGender() == Gender.undefined) {
						parents[0].setGender(Gender.female);
					}
				} else {
					System.out.printf("%s already has a father!!", childName);
				}
			}
			return true;
		} else {
			System.out.printf("incorect parent input \n");
		}

		return false;
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
				if(i==2) {
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

	private Person findMember(String name) {
		for (Person member : this.familyMembers) {
			if (member.getName().equals(name)) {
				return member;
			}
		}

		return null;
	}

	// checks whether the child or parent are its' ancestors
	private boolean checkIfOwnAncestor(String childName, String ParentName) {
		Person childN = findMember(childName);
		Person parentName = findMember(ParentName);
		ArrayList<Person> currentMembers = new ArrayList<Person>();
		ArrayList<Person> addMembers = new ArrayList<Person>();
		currentMembers.add(childN);
		boolean isParent = false;
		while (!isParent) {
			for (Person curMember : currentMembers) {
				addMembers.clear();
				if (curMember.getParents() != null) {
					if (curMember.getParents()[0] != null) {
						if (curMember.getParents()[0].getName().equals(childN.getName())) {
							return false;
						} else {
							addMembers.add(curMember.getParents()[0]);
						}

					}
					if (curMember.getParents()[1] != null) {
						if (curMember.getParents()[1].getName().equals(childN.getName())) {
							return false;
						} else {
							addMembers.add(curMember.getParents()[1]);
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
		currentMembers.add(childN);

		boolean isChild = false;
		while (!isChild) {
			for (Person curMember : currentMembers) {
				addMembers.clear();
				if (curMember.getChildren() != null) {
					for (Person child : curMember.getChildren()) {
						if (child.getName().equals(childN.getName()) || child.getName().equals(parentName.getName())) {
							return false;
						} else {
							addMembers.add(child);
						}
					}
				} else {
					isChild = true;
				}
			}
			currentMembers.clear();
			if (addMembers.size() == 0) {
				isChild = true;
			}
			currentMembers.addAll(addMembers);
		}
		return true;
	}

}
