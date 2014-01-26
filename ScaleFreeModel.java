import java.util.ArrayList;
import java.util.Set;

/**
 * @author Joshua A. Campbell
 *
 * Represents a scale-free model.
 */
class ScaleFreeModel extends Graph
{
    // Disparity index.
    private int disparity;

    /**
     * Constructs a new scale-free model.
     *
     * @param num The number of nodes in the network.
     * @param disparity The number of links per node.
     */
    public ScaleFreeModel(int num, int disparity)
    {
        super(num);
        this.disparity = disparity;

        generate();
    }

    /**
     * Generates a new scale-free model.
     */
    private void generate()
    {
        // All the nodes we have processed so far.
        ArrayList<Integer> allNodesSoFar = new ArrayList<Integer>();
        allNodesSoFar.add(1);
        allNodesSoFar.add(2);

        // Add an edge between the first two nodes if possible. 
        if (this.numNodes >= 2)
            addUndirectedEdge(1, 2);

        // The total edges we have added so far.
        double totalEdges = 1.0;

        // For each node except the first two...
        for (int i = 3; i <= this.numNodes; i++)
        {
            // Nodes we can possibly choose from.
            ArrayList<Integer> possible = new ArrayList<Integer>(allNodesSoFar);

            // Add links until we run out of nodes or run out of links.
            for (int k = 1; k <= this.disparity && k < i; k++)
            {
                // Have we created this edge?
                boolean created = false;

                // We will keep on trying to create the edge.
                // An edge will not be created if it already exists.
                while(!created)
                {
                    // Our previous and current index in the distribution.
                    double prev = 0.0;
                    double curr = 0.0;

                    // The new edge we selected from the distribution.
                    double newEdge = Math.random();

                    // Search all the possible nodes we can add for the edge we
                    // need to add.
                    for (Integer possibility : possible)
                    {
                        // The neighbors of the node we are considering. 
                        Set<Integer> possNeighs = this.nodes.get(possibility).getNeighbors();
                        
                        // Update our right endpoint in the distribution.
                        // Halve the value because we only have undirected edges.
                        curr += 0.5*(possNeighs.size() / totalEdges);

                        // If the random number falls between these bounds this
                        // might be the edge we are going to add.
                        if (prev <= newEdge && newEdge < curr)
                        {
                            // Only add the edge if it doesn't already exist.
                            if(!possNeighs.contains(i))
                            {
                                addUndirectedEdge(i, possibility);
                                created = true;
                            }
                            // Break out of the for loop regardless of whether
                            // an edge was added or not.
                            break;
                        }
                        prev = curr;
                    }
                }
                // We have created a new edge.
                totalEdges++;
            }

            // We have now seen this node.
            allNodesSoFar.add(i);
        }
    }

}
