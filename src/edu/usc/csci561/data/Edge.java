/**
 * 
 */
package edu.usc.csci561.data;

/**
 * @author mohit aggarwl
 * 
 */
public class Edge {

	private Node a, b;
	private double cost;
	private boolean isUsed;
	private boolean isA, isB;

	public Edge(Node a, Node b, double cost) {
		this.a = a;
		this.b = b;
		this.cost = cost;
	}

	/**
	 * @return the a
	 */
	public Node getA() {
		return a;
	}

	/**
	 * @return the b
	 */
	public Node getB() {
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
	public boolean isTuple(Node p, Node q) {
		return (p.getName().equals(a.getName()) || p.getName().equals(
				b.getName()))
				&& (q.getName().equals(a.getName()) || q.getName().equals(
						b.getName()));
	}

	public String toString() {
		return "(" + a.getName() + ", " + b.getName() + ", " + cost + ")";
	}

	/**
	 * @return the isUsed
	 */
	public boolean isUsed() {
		isUsed = isUsed || (a.isVisited() && b.isVisited());
		return isUsed;
	}

	/**
	 * @param isUsed
	 *            the isUsed to set
	 */
	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	public boolean isInEdge(Node n) {
		isA = n.getName().equals(a.getName());
		isB = n.getName().equals(b.getName());
		/*a.setVisited(isA);
		b.setVisited(isB);*/
		return isA || isB;
	}
}
