public class PercolationStats {
	
	private Percolation perc;
	
	private int T;
	
	private double mean;
	private double stdDev;
	private double confLow;
	private double confHigh;
	
	public PercolationStats(int N, int t)     // perform T independent experiments on an N-by-N grid
   {
		if (N <= 0 || t <= 0)
		{
			throw new IllegalArgumentException();
		}
		T = t;
		perc = new Percolation(N);
		
		int totalOpenSites = 0;
		int[] os = new int[T];
		//perform experiments
		for (int i = 0; i < T; i++)
		{
			//single experiment
			perc = new Percolation(N);
			int openSites = 0;
			while(!perc.percolates())
			{				
				int k = StdRandom.uniform(N-i) + 1;
				int j = StdRandom.uniform(N-i) + 1;
				System.out.println(k);
				System.out.println(j);
				if (!perc.isOpen(k, j))
				{
					perc.open(k, j);
					openSites += 1;
				}
				os[i] = openSites;
				totalOpenSites += openSites;
			}
			
		}// end experiments
		mean = totalOpenSites/T;
		
		// std dev
		double sum = 0;
		for (int i = 0; i < T; i++)
		{
			sum += Math.pow(os[i] - mean, 2);
		}
		double stdDevSq = sum/(T-1);
		stdDev = Math.pow(stdDevSq, 0.5);		
   }//ctor
   public double mean()                      // sample mean of percolation threshold
   {
	   return mean;
   }
   public double stddev()                    // sample standard deviation of percolation threshold
   {
	   return stdDev;
   }
   public double confidenceLo()              // low  endpoint of 95% confidence interval
   {
	   return confLow;
   }
   public double confidenceHi()              // high endpoint of 95% confidence interval
   {
	   return confHigh;
   }

   public static void main(String[] args)    // test client (described below)
   {
//	   StdOut.println(" Hello");
//    int N = StdIn.readInt();
//    WeightedQuickUnionUF uf = new WeightedQuickUnionUF(N);
//    while (!StdIn.isEmpty()) {
//        int p = StdIn.readInt();
//        int q = StdIn.readInt();
//        if (uf.connected(p, q)) continue;
//        uf.union(p, q);
//        StdOut.println(p + " " + q);
//    }
//    StdOut.println(uf.count() + " components");
	   int T = 3;
	   int N = 4;
	   
	   PercolationStats p = new PercolationStats(T, N);
	   
	   StdOut.print("mean = "); StdOut.println(p.mean());
	   StdOut.print("stddev = "); StdOut.println(p.stddev());
	   StdOut.print("95% confidence interval = "); StdOut.println(p.confidenceLo()+", "+ p.confidenceHi());
   
	   
   }//main

}