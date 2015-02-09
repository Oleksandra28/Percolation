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
			int[] openClosed = new int[N+1];
			
			int kk = 0;
			while(!perc.percolates())
			{
				int k = 0;
				int j = 0;
				if (kk < N)
				{
				k = StdRandom.uniform(N-kk) + 1;
				openClosed[k] = N-kk-1;
				openClosed[N-i-1] = k;
				
				j = StdRandom.uniform(N-kk-1) + 1;
				openClosed[j] = N-kk-2;
				openClosed[N-kk-2] = j;	
				kk +=2;
				}
				else
				{
					k = StdRandom.uniform(N) + 1;
					j = StdRandom.uniform(N) + 1;
				}
				
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
		double weirdThing = (1.96*stdDev)/Math.pow(T, 0.5);
		confLow = mean - weirdThing;
		confHigh = mean + weirdThing;
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
	   int T = StdIn.readInt();
	   int N = StdIn.readInt();

	   PercolationStats p = new PercolationStats(N, T);
	   
	   StdOut.print("mean = "); StdOut.println(p.mean());
	   StdOut.print("stddev = "); StdOut.println(p.stddev());
	   StdOut.print("95% confidence interval = "); StdOut.println(p.confidenceLo()+", "+ p.confidenceHi());
   	   
   }//main

}