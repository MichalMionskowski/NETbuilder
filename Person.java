package familyTree;

import java.util.ArrayList;

enum Gender { male , female , undefined};

public class Person {
	
	private String name;
	private Person[] parents= new Person[2];
	private ArrayList<Person> children = new ArrayList<Person>();
	private Gender gender = Gender.undefined;
	
	public Person(String name) {
		this.name = name;
	}
	
	public void setGender(Gender gender) {this.gender = gender;}	
	public void addChild(Person child) {this.children.add(child);}
	public void setParents(Person parent) {
		if(this.parents[0]==null) {
			parents[0] = parent;
		}else if (this.parents[1] == null) {
			parents[1] = parent;
		}
	
	}
	
	public Gender getGender() { return this.gender;}
	public String getName() { return this.name;}
	public Person[] getParents() {
		if(this.parents[0] != null) {
			return this.parents;
		}
		return null;
		
	}
	public ArrayList<Person> getChildren() {
		return this.children;
	}
	
}
