package conwaygame;
import java.security.KeyStore;
import java.util.ArrayList;


import static conwaygame.StdIn.*;

/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.
 * @author Seth Kelley
 * @author Maxwell Goldberg
 */
public class  GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
     * Default Constructor which creates a small 5x5 grid with five alive cells.
     * This variation does not exceed bounds and dies off after four iterations.
     */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
     * Constructor used that will take in values to create a grid with a given number
     * of alive cells
     *
     * @param file is the input file with the initial game pattern formatted as follows:
     *             An integer representing the number of grid rows, say r
     *             An integer representing the number of grid columns, say c
     *             Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
     */
    public GameOfLife(String file) {
        setFile(file);

        StdIn.setFile(file);

        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean[r][c];
        int i = 0;
        while (i < r) {
            int j = 0;
            while (j < c) {
                boolean readIn = StdIn.readBoolean();
                if (readIn != true) {
                    j++;
                    continue;
                }
                grid[i][j] = ALIVE;
                j++;
            }
            i++;
        }
    }

    /**
     * Returns grid
     *
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid() {
        return grid;
    }

    /**
     * Returns totalAliveCells
     *
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells() {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     *
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState(int row, int col) {
        if (grid[row][col] == ALIVE)
            return true;
        else {
            return false;
        }
    }


    /**
     * Returns true if there are any alive cells in the grid
     *
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive() {
        totalAliveCells = 0;
        for (int j = 0; j <= grid.length - 1; j++) {
            for (int k = 0; k <= grid.length - 1; k++) {
                if (grid[j][k] == true) {
                    totalAliveCells++;
                }
            }
        }
        if (totalAliveCells > 0) {
            return true;
        } else _{
            return false;
        }

    }
    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are
     * horizontally, vertically, or diagonally adjacent.
     *
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors(int row, int col) {
        int aliveNeighbors = 0;
        for (boolean b : new boolean[]{grid[(row + grid.length - 1) % grid.length][(col + grid[0].length - 1) % grid[0].length], grid[(row + grid.length) % grid.length][(col + grid[0].length - 1) % grid[0].length], grid[(row + grid.length + 1) % grid.length][(col + grid[0].length - 1) % grid[0].length], grid[(row + grid.length + 1) % grid.length][(col + grid[0].length) % grid[0].length], grid[(row + grid.length + 1) % grid.length][(col + grid[0].length + 1) % grid[0].length], grid[(row + grid.length) % grid.length][(col + grid[0].length + 1) % grid[0].length]}) {
            if(b == ALIVE){
                aliveNeighbors++;
            }
        }
        for (int i : new int[]{col + grid[0].length + 1, col + grid[0].length}) {
            if(grid[(row + grid.length-1) % grid.length][(i)% grid[0].length] == ALIVE) {
                aliveNeighbors++;
            }
        }
        return aliveNeighbors;
    }

    private int getAliveNeighbors(int row, int col, int aliveNeighbors) {
        for (int i : new int[]{row - 1, row, row + 1}) {
            if (getCellState(i, col - 1)) {
                aliveNeighbors++;
            }
        }
        return aliveNeighbors;
    }

    /**
     * Creates a new grid with the next generation of the current grid using
     * the rules for Conway's Game of Life.
     *
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid() {
        boolean[][] NewGrid = new boolean[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                int NumOfNeighbors = numOfAliveNeighbors(i, j);
                if (getCellState(i, j) || NumOfNeighbors != 3) {
                    if (getCellState(i,j) && NumOfNeighbors <= 1) {
                        NewGrid[i][j] = false;
                    } else if (getCellState(i,j) && NumOfNeighbors >= 4) {
                        NewGrid[i][j] = false;

                    } else if (getCellState(i,j) && (NumOfNeighbors == 2 || NumOfNeighbors == 3)) {
                        NewGrid[i][j] = true;
                    }
                } else {
                    NewGrid[i][j] = true;
                }
            }
        }
        for (int j = 0; j < grid.length; j++) {
            System.arraycopy(NewGrid[j], 0, grid[j], 0, grid[0].length);
        }
        return grid;
    }
    public void nextGeneration() {
        computeNewGrid();

    }
    /**
     * Updates the current grid with the grid computed after multiple (n) generations.
     *
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration(int n) {
        for (int i = 0; i < n; i++) {
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     *
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        return 1;
    }
}
