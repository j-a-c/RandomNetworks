import java.lang.RuntimeException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    public Map<Double, Double> getClusteringCoefficientDistribution()
    {
        Map<Double, Double> distribution = new TreeMap<Double, Double>();

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
            Double partialFreq =  distribution.get(coef);
            if (partialFreq == null)
                distribution.put(coef, 1.0);
            else
                distribution.put(coef, 1.0 + partialFreq);
        }

        // Get the actual frequency by dividing by the number of nodes at the
        // end.
        Iterator<Map.Entry<Double,Double>> it = distribution.entrySet().iterator();
        while (it.hasNext()) 
        {
            Map.Entry<Double, Double> pair = it.next();
            distribution.put(pair.getKey(), pair.getValue() / this.numNodes);
        }

        return distribution;
    }

    /**
     * Returns the distribution of the closeness centralities.
     */
    public Map<Integer, Double> getClosenessCentralityDistribution()
    {
        Map<Integer, Double> distribution = new TreeMap<Integer, Double>();

        // TODO

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
