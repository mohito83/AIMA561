/**
 * 
 */
package edu.usc.csci561.assignment1.data;

/**
 * @author mohit aggarwl
 * 
 */
public class Edge {

	private String a, b;
	private double cost;

	public Edge(String a, String b, double cost) {
		this.a = a;
		this.b = b;
		this.cost = cost;
	}

	/**
	 * @return the a
	 */
	public String getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public String getB() {
		return b;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * Check if the given person is part of the tuple
	 * 
	 * @param p
	 * @return
	 */
	public boolean isTuple(Person p) {
		return p.equals(a) || p.equals(b);
	}
}
