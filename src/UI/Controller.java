package UI;

import Puzzle.Board;
import Puzzle.Solver;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private Board board;
    private Solver solver;
    private ArrayList<Board> solution;

    @FXML
    private HBox hBox;

    @FXML
    private Label label;

    @FXML
    private ImageView img_01;

    @FXML
    private ImageView img_12;

    @FXML
    private ImageView img_00;

    @FXML
    private ImageView img_11;

    @FXML
    private ImageView img_22;

    @FXML
    private ImageView img_02;

    @FXML
    private ImageView img_10;

    @FXML
    private ImageView img_21;

    @FXML
    private ImageView img_20;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int[][] boardIni = new int[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0}
        };
        board = new Board(boardIni);

        try {
            update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void moveBlock(KeyEvent event) throws IOException {
        KeyCode keyCode = event.getCode();
        int blankLocationX = board.navigateBlank() / 3;
        int blankLocationY = board.navigateBlank() % 3;
        if (keyCode.equals(KeyCode.UP) || keyCode.equals(KeyCode.W)) {
            System.out.println("Up");
            if (blankLocationX == 2) return;
            // swap
            board.swap(blankLocationX, blankLocationY,blankLocationX + 1, blankLocationY);
        }
        else if (keyCode.equals(KeyCode.DOWN) || keyCode.equals(KeyCode.S)) {
            System.out.println("Down");
            if (blankLocationX == 0) return;
            // swap
            board.swap(blankLocationX, blankLocationY,blankLocationX - 1, blankLocationY);
        }
        else if (keyCode.equals(KeyCode.LEFT) || keyCode.equals(KeyCode.A)) {
            System.out.println("Left");
            if (blankLocationY == 2) return;
            // swap
            board.swap(blankLocationX, blankLocationY, blankLocationX, blankLocationY + 1);
        }
        else if (keyCode.equals(KeyCode.RIGHT) || keyCode.equals(KeyCode.D)) {
            System.out.println("RIGHT");
            if (blankLocationY == 0) return;
            // swap
            board.swap(blankLocationX, blankLocationY, blankLocationX, blankLocationY - 1);
        }
        else if (keyCode.equals(KeyCode.ENTER)) next();
        else return;

        update();

        if (board.isGoal()) label.setText("Congratulations!");
        else label.setText("");

//        System.out.println("Updated");
//        System.out.println(board);
    }

    @FXML
    void shuffle() throws IOException {  // to get a randomly generated board
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            arrayList.add(i);
        }
        Collections.shuffle(arrayList);
        int[][] newBoardEntries = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                newBoardEntries[i][j] = arrayList.remove(0);
            }
        }
        board = new Board(newBoardEntries);

        Solver solver = new Solver(board);

        // check whether it's solvable
        if (!solver.isSolvable()) {
            shuffle();
            return;
        }

        label.setText("Shuffled.");

        update();
    }

    @FXML
    void solvePuzzle() {  // to solve the puzzle
        solver = new Solver(board);
        solution = (ArrayList<Board>) solver.solution();
        label.setText("Solution is ready.");
        solution.remove(0);
    }

    @FXML
    void next() throws IOException {
        if (solution == null) {
            label.setText("You didn't ask for solution!");
            return;
        }
        if (solution.isEmpty()) {
            label.setText("No further step.");
            return;
        }

        board = solution.remove(0);
        update();
    }

    private void update() throws IOException {
        // Credits: <div>Icons made by <a href="https://www.flaticon.com/authors/twitter" title="Twitter">Twitter</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a></div>
        ImageView ivPointer;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // link the pointer to ImgView
                if (i == 0) {
                    if (j == 0) ivPointer = img_00;
                    else if (j == 1) ivPointer = img_01;
                    else ivPointer = img_02;
                }
                else if (i == 1) {
                    if (j == 0) ivPointer = img_10;
                    else if (j == 1) ivPointer = img_11;
                    else ivPointer = img_12;
                }
                else {
                    if (j == 0) ivPointer = img_20;
                    else if (j == 1) ivPointer = img_21;
                    else ivPointer = img_22;
                }

                if (board.getBlocks()[i][j] == 0) {
                    ivPointer.setImage(null);
                    continue;
                }
                String fileAddress = "res/output/" + board.getBlocks()[i][j] + ".jpg";
                ivPointer.setImage(new Image(new FileInputStream(fileAddress)));
            }
        }
    }
}