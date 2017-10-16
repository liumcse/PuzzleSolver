package Puzzle;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private final int[][] blocks;
    private final int dimension;
    private final int distance;
    private final int count;

    // construct a board from an N-by-N array of tiles
    public Board(int[][] blocks) {
        this.blocks = blocks;
        this.dimension = blocks.length;
        int distance = 0;
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                if (this.blocks[i][j] == 0) continue;
                if (this.blocks[i][j] != this.dimension*i + j + 1) {
                    // the block is in a wrong position
                    // calculate distance in manhattan priority function
                    distance += Math.abs(i - (this.blocks[i][j] - 1) / this.dimension);
                    distance += Math.abs(j - (this.blocks[i][j] - 1) % this.dimension);
                }
            }
        }
        this.distance = distance;
        int count = 0;
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                if (this.blocks[i][j] == 0) continue;
                // the block is in a wrong position
                // increment count in hamming priority function
                if (this.blocks[i][j] != this.dimension * i + (j + 1)) count++;
            }
        }
        this.count = count;
    }

    // return number of blocks out of place
    public int hamming() {
        return this.count;
    }

    // return sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return this.distance;
    }

    // return whether the board has reached its goal state by iterating through all blocks
    public boolean isGoal() {
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++)
                if ((this.blocks[i][j] != this.dimension * i + (j + 1))
                        & (i + j) != 2 * (this.dimension - 1))
                    return false;
        }
        return true;
    }

    // return an Iterable of all neighboring board positions
    public Iterable<Board> neighbors() {
        return neighborsIterator();
    }

    private ArrayList<Board> neighborsIterator() {
        ArrayList<Board> neighbor = new ArrayList<Board>();
        int zeroRow = navigateBlank() / dimension;
        int zeroCol = navigateBlank() % dimension;
        if ((zeroRow == 0) && (zeroCol == 0)) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol)));
        }
        else if ((zeroRow == this.dimension - 1) && (zeroCol == this.dimension - 1)) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol)));

        }
        else if ((zeroRow == this.dimension - 1) && (zeroCol == 0)) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol )));

        }
        else if ((zeroRow == 0) && (zeroCol == this.dimension - 1)) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol)));

        }
        else if (zeroRow == 0) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1)));
        }
        else if (zeroRow == this.dimension - 1) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1)));
        }
        else if (zeroCol == 0) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol)));
        }
        else if (zeroCol == this.dimension - 1) {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol)));
        }
        else {
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol - 1)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow - 1, zeroCol)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow + 1, zeroCol)));
            neighbor.add(new Board(swap(this.blocks, zeroRow, zeroCol, zeroRow, zeroCol + 1)));
        }
        return neighbor;
    }

    public int navigateBlank() {
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++)
                if (this.blocks[i][j] == 0) {
                    return this.dimension*i + j;
                }
        }
        return 0;
    }

    // swap two specified elements
    public void swap(int i1, int j1, int i2, int j2) {
        int temp = blocks[i1][j1];
        blocks[i1][j1] = blocks[i2][j2];
        blocks[i2][j2] = temp;
    }

    private int[][] swap(int[][] array, int i1, int j1, int i2, int j2) {
        int[][] swapArray = new int[this.dimension][this.dimension];
        for (int i = 0; i < this.dimension; i++)
            for (int j = 0; j < this.dimension; j++)
                swapArray[i][j] = array[i][j];
        int tmp = swapArray[i1][j1];
        swapArray[i1][j1] = swapArray[i2][j2];
        swapArray[i2][j2] = tmp;
        return swapArray;
    }

    public Board twin() {
        int random1, random2;
        do {
            Random rand = new Random();
            random1 = rand.nextInt(this.dimension * this.dimension);
            random2 = rand.nextInt(this.dimension * this.dimension);
        } while ((random1 == random2) || (this.blocks[random1 / this.dimension][random1 % this.dimension] == 0)
                || (this.blocks[random2 / this.dimension][random2 % this.dimension] == 0));
        return new Board(swap(this.blocks, random1 / this.dimension, random1 % this.dimension, random2 / this.dimension, random2 % this.dimension));
    }

    public int[][] getBlocks() {
        return blocks;
    }

    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        try {
            Board that = (Board) y;
            if (this.dimension != that.dimension) return false;
            for (int i = 0; i < this.dimension; i++) {
                for (int j = 0; j < this.dimension; j++)
                    if (this.blocks[i][j] != that.blocks[i][j]) return false;
            }
            return true;
        } catch (ClassCastException ex) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(this.dimension + "\n");
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // test
//    public static void main(String[] args) {
//        int[][] block1 = {
//                {0, 2, 5},
//                {8, 1, 4},
//                {6, 3, 7}
//        };
//        int[][] block2 = {
//                {0, 2, 5},
//                {8, 1, 4},
//                {6, 3, 7}
//        };
//        Puzzle.Board board1 = new Puzzle.Board(block1);
//        Puzzle.Board board2 = new Puzzle.Board(block2);
//        System.out.println(board1.equals(board2));
//    }
}
