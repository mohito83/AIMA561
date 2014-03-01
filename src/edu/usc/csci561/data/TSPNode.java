/**
 * 
 */
package edu.usc.csci561.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author mohit aggarwl
 * 
 */
public class TSPNode extends Node {
	private Node left;
	private Node right;
	private Node top;
	private Node bottom;
	private Map children;
	private double heuristic;
	private double totalDistance;
	private Coordinate loc;
	private int precedence;
	private boolean isMSTVisited;

	public TSPNode(String v) {
		super(v);
		left = null;
		right = null;
		top = null;
		bottom = null;
		heuristic = Double.POSITIVE_INFINITY;
		totalDistance = 0.0;
		children = new HashMap();
		isMSTVisited = false;
	}

	public TSPNode(String v, int x, int y) {
		this(v);
		loc = new Coordinate(x, y);
	}

	public String toString() {
		return name + "(" + loc.getX() + "," + loc.getY() + ")";
	}

	public List getUnvisitedNodes() {
		List nodes = new ArrayList();
		if (left != null && left.state != State.BLACK) {
			nodes.add(left);
		}
		if (right != null && right.state != State.BLACK) {
			nodes.add(right);
		}
		if (top != null && top.state != State.BLACK) {
			nodes.add(top);
		}
		if (bottom != null && bottom.state != State.BLACK) {
			nodes.add(bottom);
		}
		return nodes;
	}

	public List getUnvisitedMSTNodes() {
		List nodes = new ArrayList();
		List values = new ArrayList(children.values());
		Collections.sort(values);

		Iterator iter = values.iterator();
		while (iter.hasNext()) {
			Double d = (Double) iter.next();

			Iterator iter1 = children.keySet().iterator();
			while (iter1.hasNext()) {
				TSPNode key = (TSPNode) iter1.next();
				Double v = (Double) children.get(key);
				if (d.doubleValue() == v.doubleValue()) {
					if (!key.isMSTVisited() && !nodes.contains(key)) {
						nodes.add(key);
					}
				}
			}
		}

		return nodes;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public boolean equals(Object p) {
		if (p instanceof TSPNode) {
			TSPNode n = (TSPNode) p;
			if (name.trim().length() == 0 && n.getName().trim().length() == 0) {
				return this == n;
			} else {
				return this.name.equals(((TSPNode) p).getName());
			}
		} else {
			return false;
		}
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

	/**
	 * @return the totalDistance
	 */
	public double getTotalDistance() {
		return totalDistance;
	}

	/**
	 * @param totalDistance
	 *            the totalDistance to set
	 */
	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}

	/**
	 * Resolves the precedence
	 * 
	 * @param n2
	 * @return
	 */
	public int isHighPrecedence(TSPNode n2) {
		/*
		 * if (this.loc.getX() < n2.loc.getX() && this.loc.getY() <
		 * n2.loc.getY()) return -1; else return 1;
		 */
		int p1 = getPrecedence();
		int p2 = n2.getPrecedence();
		return p1 - p2;
	}

	/**
	 * @return the precedence
	 */
	public int getPrecedence() {
		return precedence;
	}

	/**
	 * @param precedence
	 *            the precedence to set
	 */
	public void setPrecedence(int precedence) {
		this.precedence = precedence;
	}

	/**
	 * @return the children
	 */
	public Map getChildren() {
		return children;
	}

	/**
	 * Adds child to the children list
	 * 
	 * @param n
	 * @param cost
	 *            TODO
	 */
	public void addChild(TSPNode n, double cost) {
		children.put(n, new Double(cost));
	}

	/**
	 * @return the isMSTVisited
	 */
	public boolean isMSTVisited() {
		return isMSTVisited;
	}

	/**
	 * @param isMSTVisited
	 *            the isMSTVisited to set
	 */
	public void setMSTVisited(boolean isMSTVisited) {
		this.isMSTVisited = isMSTVisited;
	}
}
