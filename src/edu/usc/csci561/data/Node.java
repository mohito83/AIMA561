/**
 * 
 */
package edu.usc.csci561.data;

/**
 * This class represents the attributes and behavior of a person in a social
 * network
 * 
 * @author mohit aggarwl
 * 
 */
public class Node implements Comparable {

	protected String name;
	protected State state;
	private Node parent;
	protected double distance;
	private int depth;
	private boolean isVisited;

	/**
	 * friendList contains the List of friends.
	 */

	public Node(String name) {
		this.name = name;
		this.state = State.WHITE;
		this.parent = null;
		this.distance = Double.POSITIVE_INFINITY;
		this.depth = 0;
	}
	
	public Node(){
		super();
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
		if (p instanceof Node) {
			return this.name.equals(((Node) p).getName());
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.name.hashCode();
	}

	/**
	 * @return the parent
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}

	/**
	 * @return the distance
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public int compareTo(Object o) {
		return this.getName().compareTo(((Node) o).getName());
	}

	/**
	 * @return the depth
	 */
	public int getDepth() {
		return depth;
	}

	/**
	 * @param depth the depth to set
	 */
	public void setDepth(int depth) {
		this.depth = depth;
	}

	/**
	 * @return the isVisited
	 */
	public boolean isVisited() {
		return isVisited;
	}

	/**
	 * @param isVisited the isVisited to set
	 */
	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}
}
