/**
 * @author Joshua A. Campbell
 *
 * Driver for the model functions.
 */
class Driver
{
    private Graph graph;

    public Driver(){}

    /**
     * Creates a new Erdo-Renyi model.
     *
     * @param num The number of nodes in the graph.
     * @param prob The probability of edge existence.
     */
    public void createErdoRenyiModel(int num, double prob)
    {
        graph = new ErdoRenyiModel(num, prob);
    }

    /**
     * Creates a new Watts-Strogatz model.
     *
     * @param num The number of nodes in the graph.
     * @param avgDeg The average degree.
     * @param prob The rewiring probability.
     */
    public void createWattsStrogatzModel(int num, int avgDeg, double prob)
    {
        graph = new WattsStrogatzModel(num, avgDeg, prob);
    }

    /**
     * Creates a new Scale-Free model.
     *
     * @param num The number of nodes in the graph.
     * @param disparity The disparity index.
     */
    public void createScaleFreeModel(int num, double disparity)
    {
        // TODO 
    }

    /**
     * Prints the graph to the screen.
     */
    public void printGraph()
    {
        System.out.println(graph);
    }

    // TODO
    public void saveStats()
    {
    
    }

    public static void main(String[] args)
    {
        // Check number of arguments.
        if (args.length != 3 && args.length != 4)
        {
            System.out.println("Usage:");
            System.out.println("\tjavac Driver ER n p");
            System.out.println("\tjavac Driver WS n k p");
            System.out.println("\tjavac Driver SF n y");
            return;
        }

        Driver driver = new Driver();

        // Check arguments specific to each graph before creating the graph.
        if (args[0].equals("ER"))
        {
            if (args.length != 3)
            {
                System.out.println("Usage: javac Driver ER n p");           
                return;
            }
        
            driver.createErdoRenyiModel(Integer.parseInt(args[1]), 
                    Double.parseDouble(args[2]));
        }
        else if (args[0].equals("WS"))
        {
            if (args.length != 4)
            {
                System.out.println("Usage: javac Driver WS n k p");
                return;
            }

           driver.createWattsStrogatzModel(Integer.parseInt(args[1]),
                   Integer.parseInt(args[2]), Double.parseDouble(args[3]));
        
        }
        else if (args[0].equals("SF"))
        {
            if (args.length != 3)
            {
                System.out.println("Usage: javac Driver SF n y");
                return;
            }

            driver.createScaleFreeModel(Integer.parseInt(args[1]), 
                    Double.parseDouble(args[2]));

        }
        else
        {
            System.out.println("Invalid graph type!");
            return;
        }

        // Print graph save the statistics.
        driver.printGraph();
        driver.saveStats();
    }
}
