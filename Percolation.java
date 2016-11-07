
/**
 * This program creates a system of sites N^2 and contains methods to help 
 * determines if the system percolates.
 *
 * Bradley Ogilvie
 */
public class Percolation {

    private WeightedQuickUnionUF percGrid, fullGrid;
    private boolean[] openSites;
    private int size;
    /* 
     The constructor creates two WeightedQuickUnionUFs: one to determine if a 
     site is full and one to determine if the system percolates.  It also 
     creates a boolean array to determine if a site is open or not.
     */

    public Percolation(int N) {
        openSites = new boolean[N * N];
        percGrid = new WeightedQuickUnionUF((N * N) + 2);//determines if the
        //system percolates.
        fullGrid = new WeightedQuickUnionUF((N * N) + 1);//if a site is full
        for (int i = 0; i < (N * N); i++) {
            openSites[i] = false;
        }
        size = N;
    }
    /*
     This method turns the boolean at the position of the given site in the 
     openSites array to true.  It also connects the site to the open sites above
     and below it in the fullGrid and percGrid. If it is in the top row, it also
     connects to the top virtual site in the percGrid and fullGrid.  If it is
     in the bottom row, it connects to the bottom virtual site in the percGrid.
     */

    public void open(int i, int j) {
        if ((i < 1 || i > size) || (j < 1 || j > size)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int p = (i - 1) * size + j;
        openSites[p - 1] = true;
        if (i == 1) {//Connects to top virtual site
            fullGrid.union(0, p);
            percGrid.union(0, p);
        } else //else connects to open site above it
            convertFullUnion(p, i - 1, j);
        if (i == size) {//Connects to bottom virtual site
            percGrid.union(p, (size * size) + 1);
        } else //else connects to open site on bottom
            convertFullUnion(p, i + 1, j);        
        if (j > 1) {//connects to open site on left
            convertFullUnion(p, i, j - 1);
        }
        if (j < size) {//connects to open site on right
            convertFullUnion(p, i, j + 1);
        }
    }

    public boolean isOpen(int i, int j) {//Returns the boolean value in the 
        //openSites array located at the given site's location.
        if ((i < 1 || i > size) || (j < 1 || j > size)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int p = (i - 1) * size + j;
        return openSites[p - 1] == true;
    }

    public boolean isFull(int i, int j) {//If the given site is connected to        
        //the top virtual site in the fullGrid, this method returns true.
        if ((i < 1 || i > size) || (j < 1 || j > size)) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        int p = (i - 1) * size + j;
        return fullGrid.connected(0, p);
    }

    private void convertFullUnion(int p, int i, int j){//This helper method
    //removes some redundant code by converting a coordinate (i, j) into a
        if (isOpen(i, j)) {
            int q = (i - 1) * size + j;//site integer and connecting it to p.
            fullGrid.union(p, q);
            percGrid.union(p, q);
        }
    }

    public boolean percolates() {//If the top virtual site is connected to the 
        //bottom virtual site in the percGrid, this method returns true.
        return percGrid.connected(0, (size * size) + 1);
    }
}
