/**
 * 
 */
package edu.usc.csci561.assignment1.data;

/**
 * This class represents the attributes and behavior of a person in a social
 * network
 * 
 * @author mohit aggarwl
 * 
 */
public class Person {

	private String name;
	private State state;

	/**
	 * friendList contains the List of friends.
	 */

	public Person(String name) {
		this.name = name;
		this.state = State.WHITE;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public boolean equals(Object p) {
		if (p instanceof Person) {
			return this.name.equals(((Person) p).getName());
		} else {
			return false;
		}
	}
}
