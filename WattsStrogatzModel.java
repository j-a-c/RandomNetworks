import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Joshua A. Campbell
 *
 * Represents the Watts-Strogatz model.
 */
class WattsStrogatzModel extends Graph
{
    // Average degree of the nodes.
    private int averageDegree;
    // Probability of rewiring.
    private double rewiringProbability;

    /**
     * Construct a Watts-Strogatz model.
     *
     * @param n Number of nodes.
     * @param k Average degree.
     * @param p Rewiring probability, assumed even.
     */
    public WattsStrogatzModel(int n, int k, double p)
    {
        super(n);

        this.averageDegree = k;
        this.rewiringProbability = p;

        generate();
    }

    private void generate()
    {
        int halfAvg = this.averageDegree / 2;

        // Construct a regular ring lattice.
        for (int i = 1; i <= this.numNodes; i++)
            for (int j = i+1; j <= this.numNodes; j++)
            {
                int distance = Math.abs(i -j);
                distance = distance % (this.numNodes - 1 - halfAvg);
                if (0 < distance && distance <= halfAvg)
                    addUndirectedEdge(i,j);
            }

        // Rewire edges.
        for (Node node : this.nodes)
        {
            Integer identifier = node.getIdentifier();
            // Store a copy of the original neighbors in order to avoid
            // concurrent modifications.
            Set<Integer> neighbors = 
                new LinkedHashSet<Integer>(node.getNeighbors());

            // Try each of the original neighbors.
            for (Integer neighbor : neighbors)
            {
                // Only try to rewire neighbors with a greater identifier than
                // the current node.
                if (identifier.compareTo(neighbor) < 0)
                {
                    // Rewiring probability.
                    if (Math.random() < this.rewiringProbability)
                    {
                        // Possible new neighbors.
                        Set<Node> possible = new LinkedHashSet<Node>(this.nodes);
                        // Don't select neighbors. 
                        // Get the current copy of the neighbors.
                        possible.removeAll(node.getNeighbors());
                        // Don't select this node.
                        possible.remove(identifier);

                        // Index of the new neighbor.
                        int newNeighborIndex = 
                            (int)(Math.random() * possible.size());

                        Integer newNeighborIdentifier = null;
                        // Select the new neighbor.
                        int currIndex = 0;
                        for (Node newNeighbor : possible)
                        {
                            if (currIndex == newNeighborIndex)
                            {
                                newNeighborIdentifier = 
                                    newNeighbor.getIdentifier();
                                break;
                            }
                            currIndex++;
                        }

                        removeUndirectedEdge(identifier, neighbor);
                        addUndirectedEdge(identifier, newNeighborIdentifier);
                    }
                }
            }
        }
    
    }
}
