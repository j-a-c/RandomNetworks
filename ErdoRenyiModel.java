/**
 * @author Joshua A. Campbell
 * Represents the Erdo-Renyi model.
 */
class ErdoRenyiModel extends Graph
{
    private double probability;

    /**
     * Constructs the G(n, p) model, where a graph is constructed by 
     * connecting nodes randomly. Each edge is included in the graph 
     * with probability p independent from every other edge.
     */
    public ErdoRenyiModel(int numNodes, double probability)
    {
        super(numNodes);

        this.probability = probability;
        generate();
    }

    /**
     * Generates the graph using the Erdo-Renyi model.
     */
    private void generate()
    {
        for (int i = 1; i <= this.numNodes; i++)
            for (int j = i+1; j <= this.numNodes; j++)
                if (Math.random() < this.probability)
                    addUndirectedEdge(i,j);
    }

}
