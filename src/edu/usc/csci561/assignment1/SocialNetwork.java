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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import edu.usc.csci561.assignment1.data.Edge;
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
	private static Map visitList = new HashMap();

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
			olWriter.write("name,depth,cost\n");
			olWriter.flush();
		} catch (IOException e) {
			System.out.println("Error while creating output files!! - ("
					+ e.getMessage() + " ). Exiting application!!");
			System.exit(1);
		}

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

		try {
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
			BufferedWriter opBuffWriter, BufferedWriter olBuffWriter) {
		LinkedList queue = new LinkedList();
		List children = (ArrayList) nodes.get(startNode2);
		if (startNode2.equals(goalNode2)) {
			
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
			while ((line = buffReader.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, ",");
				String[] data = new String[3];
				int i = 0;
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					data[i] = token;
					i++;
				}

				double cost = Double.parseDouble(data[2]);

				List l1 = (ArrayList) nodes.get(data[0]);
				if (l1 == null) {
					l1 = new ArrayList();
					nodes.put(data[0], l1);
				}
				l1.add(data[1]);

				List l2 = (ArrayList) nodes.get(data[1]);
				if (l2 == null) {
					l2 = new ArrayList();
					nodes.put(data[1], l2);
				}
				l2.add(data[0]);

				Edge e = new Edge(data[0], data[1], cost);
				edges.add(e);
			}

			// populate the tie breaking list
			BufferedReader tbBufReader = new BufferedReader(new FileReader(
					new File(tieBrakingFile)));
			while ((line = tbBufReader.readLine()) != null) {
				tieBreakList.add(line);
				visitList.put(line, State.WHITE);
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

}
