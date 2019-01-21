import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
  private double[] percolationMeans;
  private int trials;
  private double mean;
  private double stddev;

  /**
   * Creates PercolationStats object.
   * @param n grid size.
   * @param trials number of trials.
   */
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException("n and trials must be greater than 0");
    }

    this.trials = trials;
    percolationMeans = new double[trials];
    while (--trials >= 0) {
      Percolation percolation = new Percolation(n);
      while (!percolation.percolates()) {
        int row = StdRandom.uniform(1, n + 1);
        int col = StdRandom.uniform(1, n + 1);
        percolation.open(row, col);
      }
      percolationMeans[trials] = (double) percolation.numberOfOpenSites() / (n * n);
    }
    mean = StdStats.mean(percolationMeans);
    stddev = StdStats.stddev(percolationMeans);
  }

  /**
   * Main method.
   * @param args main method arguments
   */
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    Stopwatch stopwatch = new Stopwatch();
    PercolationStats percolationStats = new PercolationStats(n, trials);
    System.out.println(stopwatch.elapsedTime());
    System.out.format("mean                     = %1.15f%n", percolationStats.mean());
    System.out.format("stddev                   = %1.15f%n", percolationStats.stddev());
    System.out.format("95%% confidence interval  = [%1.15f, %1.15f]",
        percolationStats.confidenceLo(), percolationStats.confidenceHi());
  }

  public double mean() {
    return mean;
  }

  public double stddev() {
    return stddev;
  }

  public double confidenceLo() {
    return mean - 1.96 * stddev / Math.sqrt(trials);
  }

  public double confidenceHi() {
    return mean + 1.96 * stddev / Math.sqrt(trials);
  }
}
