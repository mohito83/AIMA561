/**
 * 
 */
package edu.usc.csci561.assignment1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import edu.usc.csci561.assignment1.data.Edge;
import edu.usc.csci561.assignment1.data.Node;
import edu.usc.csci561.assignment1.data.State;

/**
 * This is the main class that presents a social network
 * 
 * <b>java search -t <task> -s <start_node> -g <goal_node> -i <input_file> -t
 * <tie_breaking_file> -op <output_path> -ol <output_log></b>
 * 
 * @author mohit aggarwl
 * 
 */
public class SocialNetwork {

	private static int task;
	private static String startNode;
	private static String goalNode;
	private static String inputFile;
	private static String tieBrakingFile;
	private static String outputPathFile;
	private static String outputLogFile;
	private static Map nodes = new HashMap();
	private static List edges = new ArrayList();
	private static List tieBreakList = new ArrayList();
	private static List existingNodes = new ArrayList();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		parseCommandline(args);

		parseInputFile(inputFile);

		// prepare the output files
		BufferedWriter opWriter = null, olWriter = null;
		try {
			opWriter = new BufferedWriter(new FileWriter(new File(
					outputPathFile)));
			olWriter = new BufferedWriter(new FileWriter(
					new File(outputLogFile)));
			olWriter.write("name,depth,cost");
			olWriter.newLine();
			olWriter.flush();
		} catch (IOException e) {
			System.out.println("Error while creating output files!! - ("
					+ e.getMessage() + " ). Exiting application!!");
			System.exit(1);
		}

		try {
			switch (task) {
			case 1:
				performBFS(startNode, goalNode, opWriter, olWriter);
				break;

			case 2:
				performDFS(startNode, goalNode, opWriter, olWriter);
				break;

			case 3:
				performUCS(startNode, goalNode, opWriter, olWriter);
				break;

			case 4:
				findConnectedComponents(opWriter, olWriter);
				break;
			}

			olWriter.close();
			opWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void findConnectedComponents(BufferedWriter opBuffWriter,
			BufferedWriter olBuffWriter) {

	}

	private static void performUCS(String startNode2, String goalNode2,
			BufferedWriter opBuffWriter, BufferedWriter olBuffWriter) {

	}

	private static void performDFS(String startNode2, String goalNode2,
			BufferedWriter opBuffWriter, BufferedWriter olBuffWriter) {

	}

	private static void performBFS(String startNode2, String goalNode2,
			BufferedWriter opBuffWriter, BufferedWriter olBuffWriter)
			throws IOException {
		Node start = null;
		Iterator iter = nodes.keySet().iterator();
		while (iter.hasNext()) {
			Node n = (Node) iter.next();
			if (n.getName().equals(startNode2)) {
				start = n;
				break;
			}
		}
		start.setDistance(0);
		start.setParent(null);
		start.setState(State.GREY);
		olBuffWriter.write(start.getName() + "," + start.getDepth() + ","
				+ start.getDistance());
		olBuffWriter.newLine();

		boolean breakLoop = false;
		Node breakNode = null;
		LinkedList queue = new LinkedList();
		queue.addLast(start);
		while (!queue.isEmpty() && !breakLoop) {
			Node u = (Node) queue.removeFirst();
			List children = (ArrayList) nodes.get(u);

			for (int i = 0; i < children.size(); i++) {
				Node v = (Node) children.get(i);
				if (v.getState() == State.WHITE) {
					v.setState(State.GREY);
					double cost = getEdgeCost(u, v) + u.getDistance();
					v.setDistance(cost);
					v.setDepth(v.getDepth() + u.getDepth() + 1);
					v.setParent(u);
					olBuffWriter.write(v.getName() + "," + v.getDepth() + ","
							+ v.getDistance());
					olBuffWriter.newLine();
					if (v.getName().equals(goalNode2)) {
						breakLoop = true;
						breakNode = v;
						break;
					}
					queue.addLast(v);
				}
			}
			u.setState(State.BLACK);
		}

		// print the path in the output file
		Stack s = new Stack();
		while (breakNode != null) {
			s.push(breakNode);
			breakNode = (Node) breakNode.getParent();
		}

		Node tmp = null;
		while (s.size() > 0 && (tmp = (Node) s.pop()) != null) {
			opBuffWriter.write(tmp.getName());
			opBuffWriter.newLine();
		}

	}

	/**
	 * parse the input file and build the social network graph
	 * 
	 * @param inputFile
	 */
	private static void parseInputFile(String inputFile) {
		File f = new File(inputFile);
		if (f == null || !f.exists()) {
			System.out.println("Invalid input file exiting the application!!");
			System.exit(1);
		}

		try {
			FileReader reader = new FileReader(f);
			BufferedReader buffReader = new BufferedReader(reader);
			String line = null;

			// populate the tie breaking list
			BufferedReader tbBufReader = new BufferedReader(new FileReader(
					new File(tieBrakingFile)));
			while ((line = tbBufReader.readLine()) != null) {
				tieBreakList.add(line);
			}

			// populate the graph
			while ((line = buffReader.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, ",");
				String[] data = new String[3];
				int i = 0;
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					data[i] = token;
					i++;
				}

				Node n1 = new Node(data[0]);
				Node n2 = new Node(data[1]);
				int index = -1;
				if ((index = existingNodes.indexOf(n1)) < 0) {
					existingNodes.add(n1);
				} else {
					n1 = (Node) existingNodes.get(index);
				}
				if ((index = existingNodes.indexOf(n2)) < 0) {
					existingNodes.add(n2);
				} else {
					n2 = (Node) existingNodes.get(index);
				}
				double cost = Double.parseDouble(data[2]);

				List l1 = (ArrayList) nodes.get(n1);
				if (l1 == null) {
					l1 = new ArrayList();
					nodes.put(n1, l1);
				}
				l1.add(n2);
				Collections.sort(l1);

				List l2 = (ArrayList) nodes.get(n2);
				if (l2 == null) {
					l2 = new ArrayList();
					nodes.put(n2, l2);
				}
				l2.add(n1);
				Collections.sort(l2);

				Edge e = new Edge(n1, n2, cost);
				edges.add(e);
			}

			System.out.println("Population is complete!!");
			buffReader.close();
			tbBufReader.close();
		} catch (FileNotFoundException e) {
			System.out
					.println("Error occurred while procesing the input file - ("
							+ e.getMessage() + "). Exiting the application!!");
			System.exit(1);
		} catch (IOException e) {
			System.out
					.println("Error occurred while procesing the input file - ("
							+ e.getMessage() + "). Exiting the application!!");
			System.exit(1);
		}
	}

	/**
	 * Parse the command line arguments and populate the corresponding place
	 * holders
	 * 
	 * @param args
	 */
	private static void parseCommandline(String[] args) {
		int i = 0;
		String tmp = null;
		while (i < args.length && args[i].startsWith("-")) {
			tmp = args[i++];

			if (tmp.equals("-t") && i == 1) {
				try {
					task = Integer.parseInt(args[i++]);
				} catch (NumberFormatException e) {
					System.out.println("Invalid command line argument!! - ("
							+ e.getMessage() + ")");
				}
			} else if (tmp.equals("-s")) {
				startNode = args[i++];
			} else if (tmp.equals("-g")) {
				goalNode = args[i++];
			} else if (tmp.equals("-i")) {
				inputFile = args[i++];
			} else if (tmp.equals("-t")) {
				tieBrakingFile = args[i++];
			} else if (tmp.equals("-op")) {
				outputPathFile = args[i++];
			} else if (tmp.equals("-ol")) {
				outputLogFile = args[i++];
			}
		}

		System.out.println("Test command line parsing logic\n");
		System.out.println("task=" + task + "\tstartnode=" + startNode
				+ "\tgoalnode=" + goalNode + "\tinputfile=" + inputFile
				+ "\ttieBreakingFile=" + tieBrakingFile + "\toutputpathfile="
				+ outputPathFile + "\toutputLogfile=" + outputLogFile);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static double getEdgeCost(Node a, Node b) {
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
