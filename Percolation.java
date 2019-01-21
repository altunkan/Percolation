import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private int numberOfOpenSites;
  private WeightedQuickUnionUF quickFindUF;
  private int[][] ids;
  private int side;
  private int topRoot;
  private int bottomRoot;

  /**
   * Creates Percolation object.
   * @param n one side of grid
   */
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("n cannot be less then or equal to 0");
    }

    side = n;
    ids = new int[3][side * side];
    topRoot = -1;
    bottomRoot = -1;
    quickFindUF = new WeightedQuickUnionUF(side * side);
    numberOfOpenSites = 0;
  }

  /**
   * Open a cell and connect it to its neighbours.
   * @param row row
   * @param col col
   */
  public void open(int row, int col) {
    if (!validInputs(row, col)) {
      throw new IllegalArgumentException("Row and Col must be between 1 and " + side);
    }

    if (isOpen(row, col)) {
      return;
    }
    makeOpen(row, col);
    numberOfOpenSites++;

    int current = getCurrentIdx(row, col);
    boolean connectTop = false;
    boolean connectBottom = false;
    int rootIdx;
    if (row == 1) {
      connectTop = true;
    }

    if (row == side) {
      connectBottom = true;
    }

    //left
    if (col > 1 && isOpen(row, col - 1)) {
      rootIdx = quickFindUF.find(getCurrentIdx(row, col - 1));
      if (ids[1][rootIdx] == 1) {
        connectTop = true;
      }

      if (ids[2][rootIdx] == 1) {
        connectBottom = true;
      }

      quickFindUF.union(current, getCurrentIdx(row, col - 1));
    }

    //bottom
    if (row < side && isOpen(row + 1, col)) {
      rootIdx = quickFindUF.find(getCurrentIdx(row + 1, col));
      if (ids[1][rootIdx] == 1) {
        connectTop = true;
      }

      if (ids[2][rootIdx] == 1) {
        connectBottom = true;
      }
      quickFindUF.union(current, getCurrentIdx(row + 1, col));
    }

    //right
    if (col < side && isOpen(row, col + 1)) {
      rootIdx = quickFindUF.find(getCurrentIdx(row, col + 1));
      if (ids[1][rootIdx] == 1) {
        connectTop = true;
      }

      if (ids[2][rootIdx] == 1) {
        connectBottom = true;
      }
      quickFindUF.union(current, getCurrentIdx(row, col + 1));
    }

    //top
    if (row > 1 && isOpen(row - 1, col)) {
      rootIdx = quickFindUF.find(getCurrentIdx(row - 1, col));
      if (ids[1][rootIdx] == 1) {
        connectTop = true;
      }

      if (ids[2][rootIdx] == 1) {
        connectBottom = true;
      }
      quickFindUF.union(current, getCurrentIdx(row - 1, col));
    }

    if (connectTop) {
      if (topRoot != -1) {
        quickFindUF.union(topRoot, current);
      }
      topRoot = quickFindUF.find(current);
      ids[1][topRoot] = 1;
    }

    if (connectBottom) {
      bottomRoot = quickFindUF.find(current);
      ids[2][bottomRoot] = 1;
    }
  }

  private void makeOpen(int row, int col) {
    ids[0][getCurrentIdx(row, col)] = 1;
  }

  /**
   * Check cell is open.
   * @param row row
   * @param col col
   * @return return true if cell is open.
   */
  public boolean isOpen(int row, int col) {
    if (!validInputs(row, col)) {
      throw new IllegalArgumentException("Row and Col must be between 1 and " + side);
    }
    return ids[0][getCurrentIdx(row, col)] == 1;
  }

  /**
   * Check cell is full.
   * @param row row
   * @param col col
   * @return return true if cell is full.
   */
  public boolean isFull(int row, int col) {
    if (!validInputs(row, col)) {
      throw new IllegalArgumentException("Row and Col must be between 1 and " + side);
    }
    return ids[1][quickFindUF.find(getCurrentIdx(row, col))] == 1;
  }

  private boolean validInputs(int row, int col) {
    if (row < 1 || col > side || row > side || col < 1) {
      return false;
    }
    return true;
  }

  /**
   * Return number of open sites.
   * @return number of open sites
   */
  public int numberOfOpenSites() {
    return numberOfOpenSites;
  }

  /**
   * Checks grid is percolates.
   * @return return true if grid is percolates
   */
  public boolean percolates() {
    if (topRoot == -1) {
      return false;
    }
    return ids[1][topRoot] == ids[2][topRoot];
  }

  private int getCurrentIdx(int row, int col) {
    return side * (row - 1) + (col - 1);
  }
}
