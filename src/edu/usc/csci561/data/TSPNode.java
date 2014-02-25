/**
 * 
 */
package edu.usc.csci561.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mohit aggarwl
 * 
 */
public class TSPNode extends Node {
	private Node left;
	private Node right;
	private Node top;
	private Node bottom;
	private double heuristic;
	private Coordinate loc;

	public TSPNode(String v) {
		super(v);
		left = null;
		right = null;
		top = null;
		bottom = null;
		heuristic = 0.0;
	}
	
	public TSPNode(String v, int x,int y){
		this(v);
		loc = new Coordinate(x,y);
	}

	public String toString() {
		return name + "";
	}

	public List getUnvisitedNodes() {
		List nodes = new ArrayList();
		if (left != null && left.state == State.WHITE) {
			nodes.add(left);
		}
		if (right != null && right.state == State.WHITE) {
			nodes.add(right);
		}
		if (top != null && top.state == State.WHITE) {
			nodes.add(top);
		}
		if (bottom != null && bottom.state == State.WHITE) {
			nodes.add(bottom);
		}
		return nodes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the left
	 */
	public Node getLeft() {
		return left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(Node left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public Node getRight() {
		return right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(Node right) {
		this.right = right;
	}

	/**
	 * @return the top
	 */
	public Node getTop() {
		return top;
	}

	/**
	 * @param top
	 *            the top to set
	 */
	public void setTop(Node top) {
		this.top = top;
	}

	/**
	 * @return the bottom
	 */
	public Node getBottom() {
		return bottom;
	}

	/**
	 * @param bottom
	 *            the bottom to set
	 */
	public void setBottom(Node bottom) {
		this.bottom = bottom;
	}

	/**
	 * @return the heuristic
	 */
	public double getHeuristic() {
		return heuristic;
	}

	/**
	 * @param heuristic
	 *            the heuristic to set
	 */
	public void setHeuristic(double heuristic) {
		this.heuristic = heuristic;
	}

	/**
	 * @return the loc
	 */
	public Coordinate getLoc() {
		return loc;
	}
}
