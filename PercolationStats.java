
/**
 *This program takes a system of N^2 sites and randomly opens them until 
 * the system percolates.  It then adds the fraction of sites opened divided 
 * by the system size to an array.  It repeats this process T times.  It then 
 * allows access to that array so the mean and the standard deviation for the 
 * experiment can be calculated.  This program assumes that N and T are 
 * greater than 0.
 * 
 * Bradley Ogilvie
 */
public class PercolationStats {

    private double[] results;
    /**
     * This constructor creates a percolator size N and then randomly opens 
     * sites until it percolates.  It then adds the number of sites opened 
     * divided by N^2 to the results array.  It repeats this process T times.
     * @param N
     * @param T
     */
    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new java.lang.IllegalArgumentException();
        }
        results = new double[T];
        for (int i = 0; i < T; i++) {
            Percolation perc = new Percolation(N);
            int numSites = 0;
            while (!perc.percolates()) {
                int p = StdRandom.uniform(N) + 1;
                int q = StdRandom.uniform(N) + 1;
                while (perc.isOpen(p, q)) {//If the site at p, q is already open
                    p = StdRandom.uniform(N) + 1;//another site is chosen.
                    q = StdRandom.uniform(N) + 1;
                }
                perc.open(p, q);
                numSites++;                
            }            
            results[i] = (double)numSites/(N*N);            
        }
    }
    public double mean() {//This calulates the mean of the experiment.
        double xT = 0;
        for (int i = 0; i < results.length; i++) {
            xT += results[i];
        }
        return xT / results.length;
    }
    public double stddev() {//This calulates the standard deviention of the 
        if (results.length == 1) {//experiment.
            return Double.NaN;//If only one run was made, this method returns
        }//undefined.
        double mean = this.mean();
        double xT = 0;
        for (int i = 0; i < results.length; i++) {
            xT += (results[i] - mean) * (results[i] - mean);
        }
        return Math.sqrt(xT / (results.length - 1));
    }
    /**
     * The main method takes two Integer values from the command line as N and
     * T.  It then shows the mean, standard deviation, and the 95% confidence 
     * interval for the experiment.
     */
    public static void main(String[] args) {
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        double mean = stats.mean();
        double stddev = stats.stddev();
        double confidence = (1.96 * stddev) / Math.sqrt(T);
        StdOut.println("mean = " + mean);
        StdOut.println("stddev = " + stddev);
        StdOut.println("95% confidence interval = " + (mean - confidence) + ", "
                + (mean + confidence));
    }
}
