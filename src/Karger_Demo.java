import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class Karger_Demo {
    static File contraction = new File("kargerMinCut.txt");

    public static void main (String[] args) throws IOException {
        if (contraction.exists()) {
            System.out.println("Contraction Algorithms (MODE 3): \n");

            List<String> lines = Files.readAllLines(contraction.toPath());
            ArrayList<ArrayList<Integer>> graph = new ArrayList<>(lines.size());

            lines.forEach(line -> {
                String[] elements = line.split("\\s+");
                Integer[] values = new Integer[elements.length];

                for (int i = 0; i < elements.length; i++) {
                    values[i] = Integer.parseInt(elements[i]);
                }
                graph.add(new ArrayList<Integer>(Arrays.asList(values)));
            });

            // Making second copy of graph and perform karger's algorithm n^2log(n) times
            int k = 0;
            int mincut = 10000;
            ArrayList<ArrayList<Integer>> graphut = new ArrayList<ArrayList<Integer>>(graph.size());

            do {
                graphut = new ArrayList<ArrayList<Integer>>(graph.size());
                for (int i = 0; i < graph.size(); i++) {
                    ArrayList<Integer> line = graph.get(i);
                    graphut.add(new ArrayList<Integer>(line.size()));
                    for (Integer integer : line) {
                        graphut.get(i).add(integer);
                    }
                }

                mincut = Math.min(mincut, karger(graphut));
                graphut.clear();
                k += 100;
            } while (k < (Math.pow(graph.size(), 2) * Math.log(graph.size())));

            System.out.println("\nMinimum Cut: " + mincut + "\n");
        }
    }

    public static int karger (ArrayList<ArrayList<Integer>> graph) {
        Random rand = new Random();
        int nodesize;
        int node1;
        int node2 = -1;
        int node1label;
        int node2label;

        while (graph.size() > 2) {
            nodesize = graph.size();
            node2 = -1;

            // Obtain first random node index, retry when edge detected
            do {
                rand = new Random();
                node1 = rand.nextInt(nodesize);
                node1label = graph.get(node1).get(0);
            } while (graph.get(node1).size() < 2);

            // Obtain second random node index that connects to node 1
            node2label = graph.get(node1).get(1);

            for (int i = 0; i < nodesize; i++) {
                if (graph.get(i).get(0) == node2label) {
                    node2 = i;
                    break;
                }
            }
            if (node2 == -1)
                return -1;

            // Merge node 2 to node 1
            for (int i = 0; i < nodesize; i++) {
                for (int j = 1; j < graph.get(i).size(); j++) {
                    if (graph.get(i).get(j) == node2label) {
                        graph.get(i).set(j, node1label);
                    }
                }
            }

            for (int i = 1; i < graph.get(node2).size(); i++) {
                graph.get(node1).add(graph.get(node2).get(i));
            }

            // Remove self-loops
            for (int i = 1; i < graph.get(node1).size(); i++) {
                if (graph.get(node1).get(i) == node1label) {
                    graph.get(node1).set(i, -1);
                }
            }
            graph.get(node1).removeAll(Collections.singleton(-1));

            // Remove node 2
            graph.remove(node2);
        }
        return graph.get(0).size() - 1;
    }
}
