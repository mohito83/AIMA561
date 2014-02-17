package edu.usc.csci561.assignment1;

/**
 * This class defines the state of the a Person as a node in the graph
 * 
 * @author mohit aggarwl
 * 
 */
public class State {
	private String name;
	
	/**
	 * WHITE == Unvisited
	 */
	public static final State WHITE = new State("white");
	/**
	 * GREY == Traversed
	 */
	public static final State GREY = new State("grey");
	/**
	 * BLACK == Visited
	 */
	public static final State BLACK = new State("black");

	private State(String _state) {
		this.name = _state;
	}

	public String toString() {
		return name;
	}
}