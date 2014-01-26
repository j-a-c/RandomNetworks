/**
 * @author Joshua A. Campbell
 *
 * Represents a scale-free model.
 */
class ScaleFreeModel extends Graph
{

    /**
     * Constructs a new scale-free model.
     *
     * @param num The number of nodes in the network.
     * @param disparity The number of links per node.
     */
    public ScaleFreeModel(int num, int disparity)
    {
        super(num);
    }

}
