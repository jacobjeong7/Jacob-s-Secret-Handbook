package avengers;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an adjacency matrix, use a random() function to remove half of the nodes. 
 * Then, write into the output file a boolean (true or false) indicating if 
 * the graph is still connected.
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * PredictThanosSnapInputFile name is passed through the command line as args[0]
 * Read from PredictThanosSnapInputFile with the format:
 *    1. seed (long): a seed for the random number generator
 *    2. p (int): number of people (vertices in the graph)
 *    2. p lines, each with p edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note: the last p lines of the PredictThanosSnapInputFile is an ajacency matrix for
 * an undirected graph. 
 * 
 * The matrix below has two edges 0-1, 0-2 (each edge appear twice in the matrix, 0-1, 1-0, 0-2, 2-0).
 * 
 * 0 1 1 0
 * 1 0 0 0
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * Delete random vertices from the graph. You can use the following pseudocode.
 * StdRandom.setSeed(seed);
 * for (all vertices, go from vertex 0 to the final vertex) { 
 *     if (StdRandom.uniform() <= 0.5) { 
 *          delete vertex;
 *     }
 * }
 * Answer the following question: is the graph (after deleting random vertices) connected?
 * Output true (connected graph), false (unconnected graph) to the output file.
 * 
 * Note 1: a connected graph is a graph where there is a path between EVERY vertex on the graph.
 * 
 * Note 2: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, isConnected is true if the graph is connected,
 *   false otherwise):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(isConnected);
 * 
 * @author Yashas Ravi
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/PredictThanosSnap.in predictthanossnap.out
*/

public class PredictThanosSnap {

    public static void main(String[] args) {

        if (args.length < 2) {
            StdOut.println("Execute: java PredictThanosSnap <INput file> <OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE
        String inputFile = args[0];
        String outputFile = args[1];

        StdIn.setFile(inputFile);
        StdOut.setFile(outputFile);

        long longSeed = StdIn.readInt();
        StdRandom.setSeed(longSeed);

        int n = StdIn.readInt();
        int[][] newNodeJMap = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newNodeJMap[i][j] = StdIn.readInt();
            }
        }
        for (int i = 0; i < n; i++) {
            if (StdRandom.uniform() <= 0.5) {
                newNodeJMap[i] = null;
            }
        }
        StdOut.print(connectingGraph(newNodeJMap));
    }

    private static int firstNotNull(int[][] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i] != null) {
                return i;
            }
        }
        return 0;

    }

    private static boolean connectingGraph(int[][] nodes) {
        ArrayList<Integer> jQueue = new ArrayList<>(List.of(firstNotNull(nodes)));
        ArrayList<Integer> jVertice = new ArrayList<>();

        while (!jQueue.isEmpty()) {
            int current = jQueue.remove(0);
            jVertice.add(current);
            for (int i = 0; i < nodes[current].length; i++) {
                if(nodes[current][i]==1 &&nodes[i] != null && !jVertice.contains(i)) {
                    jQueue.add(i);
                }
            }}
            for (int i = 0; i < nodes.length; i++) {

                if (nodes[i] != null && !jVertice.contains(i)) {
                    return false;
                }

            }
            return true;

        }


    }
