package Puzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Solver {
    private int minMoves;
    private Node finalSolution;
    private final Board initial;
    private final ArrayList<Board> solutionList;

    private class Node implements Comparable<Node>{
        int moves;
        int priority;
        Board board;
        Node prev;

        private Node (int manhattan, int moves, Board board, Node prev) {
            this.moves = moves;
            this.board = board;
            this.priority = moves + manhattan;
            this.prev = prev;
        }

        @Override
        public int compareTo(Node that) {
            return this.priority - that.priority;
        }
    }

    // find a solution to the initial board
    public Solver(Board initial) {
        this.initial = initial;
        if(this.isSolvable()) {
            Board[] solution = new Board[minMoves + 1];
            solution[minMoves] = finalSolution.board;
            for (int i = 0; i < minMoves; i++) {
                solution[minMoves - i - 1] = finalSolution.prev.board;
                finalSolution = finalSolution.prev;
            }
            solutionList = new ArrayList<>(Arrays.asList(solution));
        }
        else {
            solutionList = null;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        Board originBoard = this.initial;
        Board twinBoard = originBoard.twin();
        PriorityQueue<Node> gameTreeOrigin = new PriorityQueue<>();
        PriorityQueue<Node> gameTreeTwin = new PriorityQueue<>();
        Node minPriorityOrigin = new Node(originBoard.manhattan(), 0, originBoard, null);
        minPriorityOrigin.prev = minPriorityOrigin;
        Node minPriorityTwin = new Node(twinBoard.manhattan(), 0, twinBoard, null);
        minPriorityTwin.prev = minPriorityTwin;
        while (!minPriorityOrigin.board.isGoal() && !minPriorityTwin.board.isGoal()) {
            for (Board neighbors1 : minPriorityOrigin.board.neighbors()) {
                if (neighbors1.equals(minPriorityOrigin.prev.board)) continue;
                gameTreeOrigin.offer(new Node(neighbors1.manhattan(), minPriorityOrigin.moves + 1, neighbors1, minPriorityOrigin));
            }
            for (Board neighbors2 : minPriorityTwin.board.neighbors()) {
                if (neighbors2.equals(minPriorityTwin.prev.board)) continue;
                gameTreeTwin.offer(new Node(neighbors2.manhattan(), minPriorityTwin.moves + 1, neighbors2, minPriorityTwin));
            }
            minPriorityOrigin = gameTreeOrigin.remove();
            minPriorityTwin = gameTreeTwin.remove();
        }
        if (minPriorityOrigin.board.isGoal()) {
            this.minMoves = minPriorityOrigin.moves;
            this.finalSolution = minPriorityOrigin;
            return true;
        }
        else {
            this.minMoves = -1;
            return false;
        }
    }

    // return min number of moves to solve the initial board;
    // -1 if no such solution
    public int moves() {
        return minMoves;
    }

    // return an Iterable of solution
    public Iterable<Board> solution() {
        return solutionList;
    }


    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        Scanner sc = new Scanner(file);

        int n = sc.nextInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = sc.nextInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                System.out.println(board);
        }
    }

}