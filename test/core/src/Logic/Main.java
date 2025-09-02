package Logic;

import java.util.ArrayList;
import Logic.Card.*;


public class Main {

    public static void main(String[] args) {
        
        long startTime = System.currentTimeMillis();


        KNN.initializeKNN();
        
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(0);
        numbers.add(1);

        Board board = new Board(numbers, true);
        board.simulate(100);

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        System.out.println("Execution time: " + executionTime + " milliseconds");

    }
}
