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
import java.util.LinkedList;
import java.util.List;

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

	private static Comparator nameComp = new Comparator() {

		public int compare(Object o1, Object o2) {
			return ((TSPNode) o1).getName().compareTo(((TSPNode) o2).getName());
		}
	};

	private static Comparator costComp = new Comparator() {

		public int compare(Object o1, Object o2) {
			return (((TSPNode) o1).getDistance() - ((TSPNode) o2).getDistance()) == 0 ? 0
					: (((TSPNode) o1).getDistance() - ((TSPNode) o2)
							.getDistance()) < 0 ? -1 : 1;
		}
	};

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 1. parse the command line arguments
		parseCommandline(args);

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
				findShortestPath((TSPNode) posts.get(i), (TSPNode) posts.get(j));
			}
			resetGrid();
		}

	}

	/**
	 * Calculate the shortest distance
	 * 
	 * @param src
	 * @param dest
	 */
	private static void findShortestPath(TSPNode src, TSPNode dest) {

	}

	/**
	 * This method resets the heuristic values.
	 */
	private static void resetGrid() {

	}

	/**
	 * This function returns the h(x) value.
	 * 
	 * @param n
	 * @return
	 */
	private static double heuresticFuncManhattanDist(TSPNode src, TSPNode dest) {
		double distance = -1;
		LinkedList q = new LinkedList();
		src.setState(State.GREY);
		q.addLast(src);

		loop: while (!q.isEmpty()) {
			TSPNode u = (TSPNode) q.removeFirst();
			List nodes = u.getUnvisitedNodes();
			for (int i = 0; i < nodes.size(); i++) {
				TSPNode v = (TSPNode) nodes.get(i);
				v.setState(State.GREY);
				v.setHeuristic(v.getHeuristic() + u.getHeuristic());
				if (v == dest) {
					distance = v.getHeuristic();
					break loop;
				}
				q.addLast(u);
				u.setState(State.BLACK);
			}
		}

		return distance;
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
					grid[i][j] = new TSPNode(c+"");
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

		try {
			outputPath = new FileWriter(new File(outputPathFile));
			outputLog = new FileWriter(new File(outputLogFile));
		} catch (IOException e) {
			System.out
					.println("Exception occurred while opening file writer on the output files - "
							+ e.getMessage());
		}

		System.out.println("Test command line parsing logic\n");
		System.out.println("task=" + task + "\tinputfile=" + inputFile
				+ "\toutputpathfile=" + outputPathFile + "\toutputLogfile="
				+ outputLogFile);
	}

}
