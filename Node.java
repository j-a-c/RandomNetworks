import java.util.ArrayList;
import java.util.List;

/**
 * @author Joshua A. Campbell
 *
 * Represents a node in a graph.
 */
class Node
{
    private int identifier;
    // Holds the Identifiers of this nodes neighbors.
    private List<Integer> neighbors;

    /**
     * Constructes a node with the given identifier.
     * Identifiers should be unique.
     */
    public Node(int identifier)
    {
        this.identifier = identifier;
        neighbors = new ArrayList<Integer>();
    }

    /**
     * Adds the given identifier to the list of neighbors.
     * There is some auto-boxing that goes on here (int to Integer), but 
     * I thought that this should be too bad for sparse graphs.
     */
    public void addEdgeTo(int neighbor)
    {
        neighbors.add(neighbor);
    }

    /**
     * Returns the identifier wrapped in an Integer.
     */
    public Integer getIdentifier()
    {
        return new Integer(this.identifier);
    }

    /**
     * Returns the list of neighbors.
     */
    public List<Integer> getNeighbors()
    {
        return neighbors;
    }

}
