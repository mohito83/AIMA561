/**
 * 
 */
package edu.usc.csci561.assignment2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.usc.csci561.data.Edge;
import edu.usc.csci561.data.MSTState;
import edu.usc.csci561.data.Node;
import edu.usc.csci561.data.State;
import edu.usc.csci561.data.TSPNode;

/**
 * @author mohit aggarwl
 * 
 */
public class TravellingSalesMan {

	private static String inputFile;
	private static String outputPathFile;
	private static String outputLogFile;
	private static int task;
	private static TSPNode grid[][];
	private static List posts = new ArrayList();

	private static FileWriter outputPath;
	private static FileWriter outputLog;

	private static List edges = new ArrayList();

	private static Comparator nameComp = new Comparator() {

		public int compare(Object o1, Object o2) {
			return ((TSPNode) o1).getName().compareTo(((TSPNode) o2).getName());
		}
	};

	private static Comparator totalCostComp = new Comparator() {

		public int compare(Object o1, Object o2) {
			TSPNode n1 = (TSPNode) o1;
			TSPNode n2 = (TSPNode) o2;
			double d = n1.getTotalDistance() - n2.getTotalDistance();
			if (d == 0) {
				// XXX resolve any conflict if present.
				return n1.isHighPrecedence(n2);
			} else if (d < 0) {
				return -1;
			} else {
				return 1;
			}
		}
	};

	private static Comparator edgeComp = new Comparator() {

		public int compare(Object o1, Object o2) {
			Edge e1 = (Edge) o1;
			Edge e2 = (Edge) o2;
			return e1.getCost() - e2.getCost() == 0 ? 0 : e1.getCost()
					- e2.getCost() > 0 ? 1 : -1;
		}
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 1. parse the command line arguments
		parseCommandline(args);

		try {
			outputPath = new FileWriter(new File(outputPathFile));
			outputLog = new FileWriter(new File(outputLogFile));
		} catch (IOException e) {
			System.out
					.println("Exception occurred while opening file writer on the output files - "
							+ e.getMessage());
		}

		// 2. create the grid
		parseInputFile(inputFile);
		// 3. create the graph
		populateGraph();
		System.out.println("Grid is populated");

		// 4. sort the posts array list and start finding the shortest distance
		// path between the pairs
		Collections.sort(posts, nameComp);

		for (int i = 0; i < posts.size() - 1; i++) {
			for (int j = i + 1; j < posts.size(); j++) {
				TSPNode src = (TSPNode) posts.get(i);
				TSPNode dest = (TSPNode) posts.get(j);
				try {
					if (task == 1) {
						outputLog.write("from \'" + src.getName() + "\' to \'"
								+ dest.getName() + "\'");
						outputLog.write(System.getProperty("line.separator"));
						outputLog
								.write("----------------------------------------------------------------");
						outputLog.write(System.getProperty("line.separator"));
						outputLog.write("x,y,g,h,f");
						outputLog.write(System.getProperty("line.separator"));
					}
					findShortestPath(src, dest);
					if (task == 1) {
						outputLog
								.write("----------------------------------------------------------------");
						outputLog.write(System.getProperty("line.separator"));
					}

					// populate the Graph
					src.addChild(dest, dest.getDistance());
					dest.addChild(src, dest.getDistance());
					edges.add(new Edge(src, dest, dest.getDistance()));

					// reset the grid
					resetGrid();
				} catch (IOException e) {
					System.out
							.println("Exception while writing logs to the output file - "
									+ e.getMessage());
				}
			}
		}

		// 5. Find the Minimum Spanning Tree
		if (task == 2) {
			try {
				solveTSP((TSPNode) posts.get(0));
			} catch (IOException e) {
				System.out
						.println("Exception occured while solving the TSP with MST as heuristics. - "
								+ e.getMessage());
			}
		}

		try {
			outputLog.close();
			outputPath.close();
		} catch (IOException e) {
			System.out
					.println("Exception occurred while closing file writer - "
							+ e.getMessage());
		}
	}

	/**
	 * This method the traveling sales man problem
	 * 
	 * @param object
	 * @throws IOException
	 */
	private static void solveTSP(TSPNode src) throws IOException {

		LinkedList q = new LinkedList();
		List visited = new ArrayList();

		src.setDistance(0.0);
		src.setState(State.GREY);
		q.addLast(src);
		StringBuffer tour = new StringBuffer();

		while (!q.isEmpty()) {
			TSPNode u = (TSPNode) q.removeFirst();
			
			MSTState state = new MSTState(src);
			heuresticFuncMST(state, src);
			src.setMSTVisited(true);
			src.setHeuristic(state.getH());
			src.setTotalDistance(src.getDistance() + state.getH());
			tour.append(src.getName());

			outputLog.write(tour.toString() + "," + u.getDistance() + ","
					+ u.getHeuristic() + "," + u.getTotalDistance());
			outputLog.write(System.getProperty("line.separator"));
			outputPath.write(u.getName());
			outputPath.write(System.getProperty("line.separator"));

			visited.add(u);
			resetEdges();
			Map unvisited = u.getUnvisitedMSTNodes();

			Iterator iter = unvisited.keySet().iterator();
			while (iter.hasNext()) {
				TSPNode key = (TSPNode) iter.next();
				state = new MSTState(key);
				double d = getEdgeCost(u, key);
				heuresticFuncMST(state, src);
				key.setMSTVisited(true);
				key.setHeuristic(state.getH());
				key.setDistance(d+u.getDistance());
				key.setTotalDistance(key.getDistance() + state.getH());
				q.addLast(key);
			}
		}
	}

	private static void resetEdges() {
		Iterator iter = edges.iterator();
		while (iter.hasNext()) {
			Edge e = (Edge) iter.next();
			e.setUsed(false);
			e.getA().setVisited(false);
			e.getB().setVisited(false);
		}
	}

	/**
	 * Calculate the shortest distance
	 * 
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	private static void findShortestPath(TSPNode src, TSPNode dest)
			throws IOException {

		LinkedList q = new LinkedList();
		q.addLast(src);
		src.setState(State.GREY);
		double d = heuristicFuncManhattanDist(src, dest);
		double c = 0.0;
		src.setHeuristic(d);
		src.setTotalDistance(d);
		src.setDepth(0);
		src.setDistance(c);

		while (!q.isEmpty()) {
			TSPNode u = (TSPNode) q.removeFirst();
			List nodes = u.getUnvisitedNodes();

			if (task == 1) {
				outputLog.write(u.getLoc().getX() + "," + u.getLoc().getY()
						+ "," + u.getDistance() + "," + u.getHeuristic() + ","
						+ u.getTotalDistance());
				outputLog.write(System.getProperty("line.separator"));
			}

			if (u.getName().equals(dest.getName())) {
				break;
			}

			for (int i = 0; i < nodes.size(); i++) {
				TSPNode v = (TSPNode) nodes.get(i);
				d = heuristicFuncManhattanDist(v, dest);
				c = u.getDistance() + 1;
				if (c < v.getDistance()) {
					v.setDistance(c);
					v.setDepth(u.getDepth() + 1);
					v.setHeuristic(d);
					v.setTotalDistance(c + d);
					v.setState(State.GREY);
					if (!q.contains(v)) {
						q.addLast(v);
					}
				}
			}
			u.setState(State.BLACK);

			Collections.sort(q, totalCostComp);
		}

		if (task == 1) {
			outputPath.write(src.getName() + "," + dest.getName() + ","
					+ dest.getDistance());
			outputPath.write(System.getProperty("line.separator"));
		}

	}

	/**
	 * This method resets the heuristic values.
	 */
	private static void resetGrid() {
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (grid[i][j] != null) {
					grid[i][j].setDepth(0);
					grid[i][j].setState(State.WHITE);
					grid[i][j].setDistance(Double.POSITIVE_INFINITY);
					grid[i][j].setHeuristic(0.0);
					grid[i][j].setTotalDistance(0.0);
				}
			}
		}
	}

	/**
	 * This function returns the h(x) value.
	 * 
	 * @param n
	 * @return
	 */
	private static double heuristicFuncManhattanDist(TSPNode src, TSPNode dest) {
		double distance = -1;
		distance = Math.abs(src.getLoc().getX() - dest.getLoc().getX())
				+ Math.abs(src.getLoc().getY() - dest.getLoc().getY());
		return distance;
	}

	/**
	 * calculates the MST heuristic.
	 * 
	 * @param state
	 * @return
	 */
	private static void heuresticFuncMST(MSTState state, TSPNode start) {

		// TSPNode node = state.getCurrent();
		List visited = state.getVisitedNodes();
		List unVisited = new ArrayList();

		// find unvisited nodes
		Iterator iter = posts.iterator();
		while (iter.hasNext()) {
			TSPNode n = (TSPNode) iter.next();
			Iterator iter2 = visited.iterator();
			while (iter2.hasNext()) {
				TSPNode p = (TSPNode) iter2.next();
				if (n != p) {
					unVisited.add(n);
				}
			}
			unVisited.add(n);
		}
		// unVisited.add(node); //unVisited.remove(start);

		List vNew = new ArrayList();
		double h = 0.0;
		int i = 0;
		vNew.add(start);
		List mstEdges = new ArrayList();

		while (vNew.size() != unVisited.size()) {
			TSPNode u = (TSPNode) vNew.get(i);
			u.setVisited(true);
			iter = edges.iterator();

			while (iter.hasNext()) {
				Edge e = (Edge) iter.next();
				if (e.isInEdge(u) && !e.isUsed() && !mstEdges.contains(e)) {
					mstEdges.add(e);
				}
			}
			Collections.sort(mstEdges, edgeComp);

			// normalize the edge list
			Edge m = null;
			while (true) {
				Edge del = (Edge) mstEdges.remove(0);
				if (!del.getA().isVisited() || !del.getB().isVisited()) {
					h += del.getCost();
					del.setUsed(true);
					m = del;
					break;
				}
			}

			if (!vNew.contains(m.getA()))
				vNew.add(m.getA());
			else
				vNew.add(m.getB());

			i++;
		}

		state.setH(h);
		System.out.println("State==>" + state.getH());

	}

	/**
	 * This method builds the
	 */
	private static void populateGraph() {
		int m = grid.length;
		int n = grid[0].length;
		System.out.println("rows=" + m + ",cols=" + n);

		for (int i = 1; i < m - 1; i++) {
			for (int j = 1; j < n - 1; j++) {
				if (grid[i][j] != null) {
					grid[i][j].setLeft(grid[i][j - 1]);
					grid[i][j].setRight(grid[i][j + 1]);
					grid[i][j].setTop(grid[i - 1][j]);
					grid[i][j].setBottom(grid[i + 1][j]);
				}
			}
		}
	}

	/**
	 * This method parses the input files and populate the fully connected graph
	 * 
	 * @param inputFile2
	 */
	private static void parseInputFile(String inputFile2) {
		int m = 0, n = 0;
		int i = 0, j = 0;
		int val = -1;
		File f = new File(inputFile2);
		try {
			// calculate the size of the grid.
			FileReader reader = new FileReader(f);
			LineNumberReader lread = new LineNumberReader(reader);
			String str = null;
			while ((str = lread.readLine()) != null) {
				n = str.length();
				m++;
			}
			lread.close();
			grid = new TSPNode[m][n];

			// populate the grid
			reader = new FileReader(f);
			while ((val = reader.read()) > 0) {
				if (val == 10 || val == 13) {
					j++;
					continue;
				}
				if (j > n) {
					i++;
					j = 0;
				}
				if (val != 42) {
					char c = (char) val;
					grid[i][j] = new TSPNode(c + "", j, i);
					grid[i][j].setPrecedence(calculatePrecedence(i, j, m, n));
					if (val != 32) {
						posts.add(grid[i][j]);
					}
				}

				j++;
			}
		} catch (FileNotFoundException e) {
			System.out
					.println("Failed to create Filereader for the input file.-- "
							+ e.getMessage());
		} catch (IOException e) {
			System.out.println("Exception while reading the file. --- "
					+ e.getMessage());
		}
	}

	/**
	 * This method calculates the precedence on a TSPNode based on its location
	 * in the mXn maze.
	 * 
	 * @param j
	 * @param i
	 * @param m2
	 * @param n2
	 * @return
	 */
	private static int calculatePrecedence(int j, int i, int tRows, int tCols) {
		int result = 0;
		if (j % 2 == 0) {
			result = j * tCols + i;
		} else {
			result = j * tCols + (tCols - i);
		}
		return result;
	}

	/**
	 * Parse the command line arguments for the task number, input and output
	 * files
	 * 
	 * @param args
	 */
	private static void parseCommandline(String[] args) {
		int i = 0;
		String tmp = null;
		while (i < args.length && args[i].startsWith("-")) {
			tmp = args[i++];

			if (tmp.equals("-t")) {
				try {
					task = Integer.parseInt(args[i++]);
				} catch (NumberFormatException e) {
					System.out.println("Invalid command line argument!! - ("
							+ e.getMessage() + ")");
				}
			} else if (tmp.equals("-i")) {
				inputFile = args[i++];
			} else if (tmp.equals("-op")) {
				outputPathFile = args[i++];
			} else if (tmp.equals("-ol")) {
				outputLogFile = args[i++];
			}
		}

		System.out.println("Test command line parsing logic\n");
		System.out.println("task=" + task + "\tinputfile=" + inputFile
				+ "\toutputpathfile=" + outputPathFile + "\toutputLogfile="
				+ outputLogFile);
	}

	private static double getEdgeCost(Node a, Node b) {
		double cost = 0.0;
		Iterator iter = edges.iterator();
		while (iter.hasNext()) {
			Edge e = (Edge) iter.next();
			if (e.isTuple(a, b)) {
				cost = e.getCost();
				break;
			}
		}
		return cost;
	}
}
