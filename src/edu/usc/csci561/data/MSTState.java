/**
 * 
 */
package edu.usc.csci561.data;


/**
 * @author mohit aggarwl
 * 
 */
public class MSTState {

	private TSPNode current;
	private double g;
	private double h;

	public MSTState(TSPNode n) {
		current = n;
		g = 0.0;
		h = 0.0;
	}

	/**
	 * @return the g
	 */
	public double getG() {
		return g;
	}

	/**
	 * @param g
	 *            the g to set
	 */
	public void setG(double g) {
		this.g = g;
	}

	/**
	 * @return the h
	 */
	public double getH() {
		return h;
	}

	/**
	 * @param h
	 *            the h to set
	 */
	public void setH(double h) {
		this.h = h;
	}

	/**
	 * @return the current
	 */
	public TSPNode getCurrent() {
		return current;
	}

}
