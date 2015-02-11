import java.lang.IllegalArgumentException;
import java.lang.IndexOutOfBoundsException;

public class Percolation {
	private int[][] grid;
	private WeightedQuickUnionUF uf;
	private int virtualTop;
	private int virtualBottom;
	private boolean virtualTopIsOpen;
	private boolean virtualBottomIsOpen;
	private int N; // inclusive N
	
	private void validate(int n)
	{
		if (n <= 0 || n > N)
		{
			throw new IndexOutOfBoundsException("index " + n + " is not between 0 and " + N);
		}
	}
	private int getIndex(int i, int j)
	{
		validate(i);
		validate(j);
		int index = (i-1)*(N-1)+j;
		return index;
	}
	
	public Percolation(int n)               // create N-by-N grid, with all sites blocked
	{
	   if (n <= 0)
	   {
		   throw new IllegalArgumentException();
	   }
	   N = n + 1;
	   uf = new WeightedQuickUnionUF((N-1)*(N-1)+2); // 0 - top, last element - bottom
	   virtualTop = 0;
	   virtualBottom = (N-1)*(N-1) + 1;
	   // virtual top connections
	   for (int i = 1; i < N; ++i)
	   {
		   uf.union(virtualTop, i);
	   }
	   // virtual bottom connections
	   for (int i = virtualBottom - N + 1; i < virtualBottom; ++i)
	   {
		   uf.union(virtualBottom, i);
	   } 
	   
	   grid = new int [N][N];
	   for (int i = 1; i < N ; ++i)
	   {
		   for (int j = 1; j < N; j++ )
		   {
			   grid[i][j] = 0; // 0 -blocked
		   }
	   }	   	   
	   
	   virtualTopIsOpen = false;
	   virtualBottomIsOpen = false;
   }// Percolation ctor
   public void open(int i, int j)          // open site (row i, column j) if it is not open already
   {
	   validate(i);
	   validate(j);
	   grid[i][j] = 1;
	   
	   //possibly, connect virtualTop
	   if (i == 1)
	   {
		   virtualTopIsOpen = true;
	   }
	   if (i == N-1)
	   {
		   virtualBottomIsOpen = true;
	   }

	   int topY = j - 1;
	   int bottomY = j + 1;
	   int leftX = i - 1;
	   int rightX = i + 1;
	   
	   if (topY > 0 && topY < N)
	   {
		   if (isOpen(i, topY))
		   {
			   uf.union(getIndex(i, topY), getIndex(i, j));
		   }
	   }
	   if (bottomY > 0 && bottomY < N)
	   {
		   if (isOpen(i, bottomY))
		   {
			   uf.union(getIndex(i, j), getIndex(i, bottomY));
		   }
	   }
	   if (leftX > 0 && leftX < N)
	   {
		   if (isOpen(leftX, j))
		   {
			   uf.union(getIndex(i, j), getIndex(leftX, j));
		   }
	   }
	   if (rightX > 0 && rightX < N)
	   {
		   if (isOpen(rightX, j))
		   {
			   uf.union(getIndex(i, j), getIndex(rightX, j));
		   }
	   }
   }//open()
   public boolean isOpen(int i, int j)     // is site (row i, column j) open?
   {
	   validate(i);
	   validate(j);
	   return grid[i][j] == 1;
   }
   public boolean isFull(int i, int j)     // is site (row i, column j) full?
   {
	   validate(i);
	   validate(j);
	   
	   return (uf.connected(getIndex(i, j), virtualTop)) && (isOpen(i, j));
   }
   public boolean percolates()             // does the system percolate?
   {
	   return uf.connected(virtualTop, virtualBottom) && virtualTopIsOpen && virtualBottomIsOpen;
   }

   public static void main(String[] args)   // test client (optional)
   {
   }
}