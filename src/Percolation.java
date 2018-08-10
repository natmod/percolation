import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int N, topSite, bottomSite;
    private int openSiteNumber;
    private boolean[][] grid;
    private final WeightedQuickUnionUF wqu, backwash;

    public Percolation (int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }

        grid = new boolean[n][n];
        wqu = new WeightedQuickUnionUF(n * n + 2);
        backwash = new WeightedQuickUnionUF(n * n + 2);
        N = n;
        topSite = n * n + 1;
        bottomSite = n * n;
        openSiteNumber = 0;

        for (int i=0; i<n; i++)
        {
            for (int j=0; j<n; j++)
            {
                grid[i][j] = false;
            }
        }
        for (int i=0; i<n; i++){
            wqu.union(i,topSite);
            backwash.union(i, topSite);
        }
        for (int i=n*n-1; i>n*n-1-n; i--){
            wqu.union(i,bottomSite);
        }
    }

    public void open(int row, int col) {
        assertInRange(row, col);

        int i = row - 1;
        int j = col - 1;
        int idx = to1D(row, col);
        if (!isOpen(row, col)) {
            grid[i][j] = true;
            openSiteNumber++;
        }
        //right
        if (i < (N - 1)) {
            if (grid[i + 1][j] == true) {
                wqu.union(idx, to1D(row + 1, col));
                backwash.union(idx, to1D(row + 1, col));
            }
        }
        //left
        if (i > 0) {
            if (grid[i - 1][j] == true) {
                wqu.union(idx, to1D(row - 1, col));
                backwash.union(idx, to1D(row - 1, col));
            }
        }
        //top
        if (j > 0) {
            if (grid[i][j-1] == true) {
                wqu.union(idx, to1D(row, col - 1));
                backwash.union(idx, to1D(row, col - 1));
            }
        }
        //bottom
        if (j < (N - 1)) {
            if (grid[i][j+1] == true) {
                wqu.union(idx, to1D(row, col + 1));
                backwash.union(idx, to1D(row, col + 1));
            }
        }
    }

    public boolean isOpen(int row, int col) {
        assertInRange(row, col);

        int i = row - 1;
        int j = col - 1;
        if (grid[i][j] == true){
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull(int row, int col) {
        assertInRange(row, col);
        int idx = to1D(row, col);
        return isOpen(row, col) && backwash.connected(idx, topSite);
    }

    public int numberOfOpenSites() {
        return openSiteNumber;
    }

    public boolean percolates() {
        return wqu.connected(topSite, bottomSite);
    }

    private int to1D(int row, int col) {
        return (row - 1) * N + (col - 1);
    }

    private void assertInRange(int row, int col) {
        if (row < 1 || row > N || col < 1 || col > N) {
            throw new IndexOutOfBoundsException();
        }
    }

}