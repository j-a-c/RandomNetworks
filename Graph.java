import java.lang.RuntimeException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Joshua A. Campbell
 *
 * Represents a graph.
 */
class Graph
{
    protected int numNodes;
    protected List<Node> nodes;

    /**
     * Constructs a graph with the given number of nodes.
     * Initially, all nodes are disconnected.
     */
    public Graph(int numNodes)
    {
        this.numNodes = numNodes;
        this.nodes = new ArrayList<Node>();

        // We will ignore node 0 in all calculations!
        for (int i = 0; i <= numNodes; i++)
            nodes.add(new Node(i));
    }

    /**
     * Adds an undirected edge between the two nodes with the given
     * identifiers.
     */
    public void addUndirectedEdge(int node1, int node2)
    {
        // Do some bounds checking.
        if (node1 < 1 || node1 > this.numNodes)
        {
            System.out.println("node1 " + node1);
            throw new RuntimeException();
        }
        if (node2 < 1 || node2 > this.numNodes)
        {
            System.out.println("node2 " + node2);
            throw new RuntimeException();
        }

        // Connect the nodes.
        nodes.get(node1).addEdgeTo(node2);
        nodes.get(node2).addEdgeTo(node1);
    }

    /**
     * Removes the undirected edge between the two nodes with the given
     * identifier.
     */
    public void removeUndirectedEdge(int node1, int node2)
    {
        // Do some bounds checking.
        if (node1 < 1 || node1 > this.numNodes)
        {
            System.out.println("node1 " + node1);
            throw new RuntimeException();
        }
        if (node2 < 1 || node2 > this.numNodes)
        {
            System.out.println("node2 " + node2);
            throw new RuntimeException();
        }

        // Disconnect the nodes.
        nodes.get(node1).removeEdgeTo(node2);
        nodes.get(node2).removeEdgeTo(node1);
    }
    
    /**
     * Returns the degree distribution of the graph.
     * The map maps values to frequency.
     */
    public Map<Integer, Double> getDegreeDistribution()
    {
        Map<Integer, Double> distribution = new TreeMap<Integer, Double>();

        // Tally the number of degrees for each node.
        for (int i = 1; i <= this.numNodes; i++)
        {
            Node node = this.nodes.get(i);

            int degree = node.getNeighbors().size();

            Double partialFreq =  distribution.get(degree);

            if (partialFreq == null)
                distribution.put(degree, 1.0);
            else
                distribution.put(degree, 1.0 + partialFreq);
        }

        // Get the actual frequency by dividing by the number of nodes at the
        // end.
        Iterator<Map.Entry<Integer,Double>> it = distribution.entrySet().iterator();
        while (it.hasNext()) 
        {
            Map.Entry<Integer, Double> pair = it.next();
            distribution.put(pair.getKey(), pair.getValue() / this.numNodes);
        }

        return distribution;
    }

    /**
     * Returns the distribution of the clustering coefficients.
     */
    public Map<String, Double> getClusteringCoefficientDistribution()
    {
        Map<String, Double> distribution = new TreeMap<String, Double>();

        // For each node...
        for (int i = 1; i <= this.numNodes; i++)
        {
            Node node = this.nodes.get(i);

            Set<Integer> neighbors = node.getNeighbors();
            int numNeighs = neighbors.size();

            double numerator = 0.0;
            double denominator = 0.0;
            
            // The denominator = (numNeighs choose 2).
            denominator = numNeighs * (numNeighs-1);
            denominator /= 2;

            // Calculate the numerator.
            for (Integer neighbor : neighbors)
            {
                // Get the neighbors of the neighbor.
                // We need to find neighbors of the neighbor that are also
                // neighbors of the original node (node) we are calculationing
                // the clustering coefficient for.
                Set<Integer> neighsNeighs = this.nodes.get(neighbor).getNeighbors();

                // Check all the neighbor's neighbors.
                for (Integer neighsNeigh : neighsNeighs)
                {
                    // We don't want to count edges twice, so we enforce an
                    // ordering.
                    if (neighbor < neighsNeigh)
                    {
                        // If this edge actually exists, we increment the
                        // numerator because we have found an edge between two
                        // neighbors of the original node we are considering.
                        if (neighbors.contains(neighsNeigh))
                            numerator += 1.0;
                    }
                }
            }

            // Calculate the clustering coefficient.
            double coef = numerator / denominator;
            // Update the frequency.
            // Round to ten decimal places.
            String result = String.format("%.10f", coef);
            Double partialFreq =  distribution.get(result);
            if (partialFreq == null)
                distribution.put(result, 1.0);
            else
                distribution.put(result, 1.0 + partialFreq);
        }

        // Get the actual frequency by dividing by the number of nodes at the
        // end.
        Iterator<Map.Entry<String,Double>> it = distribution.entrySet().iterator();
        while (it.hasNext()) 
        {
            Map.Entry<String, Double> pair = it.next();
            distribution.put(pair.getKey(), pair.getValue() / this.numNodes);
        }

        return distribution;
    }

    /**
     * Used for the Dijkstra's algorithm implementation.
     */
    private class Pair
    {
        // Node identifier.
        int val1;
        // Node distance.
        int val2;

        public Pair(int val1, int val2)
        {
            this.val1 = val1;
            this.val2 = val2;
        }
    }

    /**
     * Comparator for the PriorityQueue used in Dijkstra's algorithm.
     */
    public class PairComparator implements Comparator<Pair>
    {
        @Override
        public int compare(Pair x, Pair y)
        {
            if (x.val2 < y.val2)
                return -1;
            else if (x.val2 > y.val2)
                return 1;
            else 
                return 0;
        }
    }

    /**
     * Returns the distribution of the closeness centralities.
     * Because some of these networks might be disconnected, the following
     * formula is used to calculate the closeness centrality:
     *  f_close(u) = sum( 1 / dist(u,v) ) for all v in V-{u}.
     *  The bigger the closeness centrality, the relatively important this node
     *  is.
     */
    public Map<String, Double> getClosenessCentralityDistribution()
    {
        Map<String, Double> distribution = new TreeMap<String, Double>();

        // Run Dijkstra's algorithm this.numNodes times to find the lengths of 
        // the shortest paths between all pairs of vertices.
        
        // Distance vector
        int[] distances = new int[this.numNodes + 1];
      
        // Populate the distribution map.
        for (int currNode = 1; currNode <= this.numNodes; currNode++)
        {
            // PriorityQueue to sort the distances.
            PriorityQueue<Pair> pQueue = 
                new PriorityQueue<Pair>(this.numNodes, new PairComparator());

            // Distance vector initialization.
            distances[currNode] = 0;
            for (int otherNode = 1; otherNode <= this.numNodes; otherNode++)
            {
                if (otherNode != currNode)
                {
                    distances[otherNode] = Integer.MAX_VALUE;
                }
                pQueue.add(new Pair(otherNode, distances[otherNode]));
            }

            Set<Integer> visitedNodes = new HashSet<Integer>();

            while (!pQueue.isEmpty())
            {
                // The current closest node we are considering.
                Pair pair = pQueue.poll();
                Set<Integer> neighbors = this.nodes.get(pair.val1).getNeighbors();
                for (Integer neighbor : neighbors)
                {
                    if (visitedNodes.contains(neighbor))
                        continue;

                    // The potential new closest distance.
                    int alt = distances[pair.val1] + 1;
                    if (alt < distances[neighbor])
                    {
                        pQueue.remove(new Pair(neighbor, distances[neighbor]));
                        distances[neighbor] = alt;
                        pQueue.add(new Pair(neighbor, distances[neighbor]));
                    }
                }

                visitedNodes.add(pair.val1);
            }

            // Closeness centrality for this node.
            double centrality = 0.0;

            for (int otherNode = 1; otherNode <= this.numNodes; otherNode++)
            {
                // Ignore the current node.
                if (currNode != otherNode)
                    centrality += (1.0 / distances[otherNode]);
            }

            // Update the frequency.
            // Round to ten decimal places.
            String result = String.format("%.10f", centrality);
            Double partialFreq =  distribution.get(result);
            if (partialFreq == null)
                distribution.put(result, 1.0);
            else
                distribution.put(result, 1.0 + partialFreq);
        }

        // Scale the distribution.
        // Get the actual frequency by dividing by the number of nodes at the
        // end.
        Iterator<Map.Entry<String, Double>> it = distribution.entrySet().iterator();
        while (it.hasNext()) 
        {
            Map.Entry<String, Double> pair = it.next();
            distribution.put(pair.getKey(), pair.getValue() / this.numNodes);
        }

        return distribution;
    }


    /**
     * Returns the edges in the graph, sorted from low identifiers to high
     * identifiers.
     */
    @Override
    public String toString()
    {
        String SPACE = " ";
        String NEWLINE = "\n";

        StringBuilder builder = new StringBuilder();

        for (Node node : nodes)
        {
            Integer identifier = node.getIdentifier();
            Set<Integer> neighbors = node.getNeighbors();
            for (Integer neighbor : neighbors)
                if (identifier.compareTo(neighbor) < 0)
                {
                    builder.append(identifier);
                    builder.append(SPACE);
                    builder.append(neighbor);
                    builder.append(NEWLINE);
                }
        }

        return builder.toString();
    }

}
