public class PercolationStats {
	
	private double mean;
	private double stdDev;
	private double confLow;
	private double confHigh;
	
	public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
   {
		if (N <= 0 || T <= 0)
		{
			throw new IllegalArgumentException();
		}		
		
		mean = 0;
		stdDev = 0;
		confLow = 0;
		confHigh = 0;
		
		double openSitesSum = 0;
		double[] openSitesExperiment = new double[T];
		//perform experiments
		for (int i = 0; i < T; i++)
		{
			//single experiment
			Percolation perc = new Percolation(N);
			//count openSites in a single experiment
			int openSites = 0;
			//maintain openClosed array to randomize better
			int[] openClosed = new int[N+1];			
			int kk = 0;
			while(!perc.percolates())
			{
				int k = 0;
				int j = 0;
				if (kk < N && kk > 2)
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
				if (!perc.isOpen(k, j))
				{
					perc.open(k, j);
					openSites += 1;
				}				
			}//while loop
			
			openSitesExperiment[i] = (double)openSites/(N*N);
			openSitesSum += openSitesExperiment[i];
		}// end experiments
		
		//we need a FRACTION!! so T*N*N instead of T 
		mean = openSitesSum/T;
		
		// std dev
		double sum = 0;
		for (int i = 0; i < T; i++)
		{
			sum += Math.pow(openSitesExperiment[i] - mean, 2);
		}
		double stdDevSq = sum/(T-1);
		stdDev = Math.pow(stdDevSq, 0.5);	
		double weirdThing = (1.96*stdDev)/Math.sqrt(T);
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
	   int N = StdIn.readInt();
	   int T = StdIn.readInt();	   

	   PercolationStats p = new PercolationStats(N, T);
	   
	   StdOut.print("mean = "); StdOut.println(p.mean());
	   StdOut.print("stddev = "); StdOut.println(p.stddev());
	   StdOut.print("95% confidence interval = "); StdOut.println(p.confidenceLo()+", "+ p.confidenceHi());
   	   
   }//main

}