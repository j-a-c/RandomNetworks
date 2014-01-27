import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Joshua A. Campbell
 *
 * Driver for the model functions.
 */
class Driver
{
    // Our graph.
    private Graph graph;

    // Output locations.
    private static final String degreeOutput = "degree.txt";
    private static final String clusterOutput = "clustering.txt";
    private static final String closenessOutput = "closeness.txt";

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

        graph = new ScaleFreeModel(num, (int) disparity);
    }

    /**
     * Prints the graph to the screen.
     */
    public void printGraph()
    {
        System.out.println(graph);
    }

    /**
     * Writes the contents of the given map to the file at the give location.
     *
     * @param data The data to be output.
     * @param location The location to write the data to.
     */
    private void writeToFile(Map<?, ?> data, String location)
    {
        PrintWriter writer = null;

        try
        {
            writer = new PrintWriter(location, "UTF-8");
        }
        catch (Exception e)
        {
            System.out.println("Unable to write to file:");
            System.out.println(e);
            return;
        }

        for (Map.Entry<?, ?> entry : data.entrySet()) 
        {
            writer.println(entry.getKey() + " " + entry.getValue());
        }
        writer.close();
    }

    /**
     * Save the various statistics offered by the Graph implementations.
     */
    public void saveStats()
    {
        // Degree distribution.
        Map<Integer, Double> degreeDist = graph.getDegreeDistribution();
        writeToFile(degreeDist, degreeOutput);

        // Clustering coefficient distribution.
        Map<String, Double> clusterDist = graph.getClusteringCoefficientDistribution();
        writeToFile(clusterDist, clusterOutput);

        // Closeness centrality distribution.
        Map<String, Double> closenessDist = graph.getClosenessCentralityDistribution();
        writeToFile(closenessDist, closenessOutput);
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
