package Logic;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KNN {

    public static ArrayList<int[]> gameStates = new ArrayList<>();
    public static ArrayList<Integer> winners = new ArrayList<>();

    private KNN() {
        // Private constructor to prevent external instantiation
    }

    public static void initializeKNN() {
        loadArrays();
    }

    public static void loadArrays() {
        String gameStatesFilePath = "gameStates.ser";
        String winnersFilePath = "winners.ser";

        loadGameStates(gameStatesFilePath);
        loadWinners(winnersFilePath);
    }

    public static void saveArrays() {
        String gameStatesFilePath = "gameStates.ser";
        String winnersFilePath = "winners.ser";
        cleanFiles(gameStatesFilePath, winnersFilePath);

        saveGameStates(gameStatesFilePath);
        saveWinners(winnersFilePath);
    }

    public static void cleanFiles(String gameStatesFilePath, String winnersFilePath) {
        cleanFile(gameStatesFilePath);
        cleanFile(winnersFilePath);
    }

    private static void cleanFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // This will create an empty file, effectively clearing its contents
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }

    public static Card findCard(int[] turn, int k) {
        List<int[]> closestTurns = new ArrayList<>();
        List<Double> distances = new ArrayList<>();

        if(gameStates.size() == 0){
            System.out.println("Save Load Error");
        }

        for (int i = 0; i < gameStates.size(); i++) {
            double distance = calculateEuclideanDistance(turn, gameStates.get(i));
            distances.add(distance);
            //System.out.println(distance);
        }

        for (int i = 0; i < k; i++) {
            int minIndex = distances.indexOf(
                    distances.stream().min(new Comparator<Double>() {
                        @Override
                        public int compare(Double d1, Double d2) {
                            return Double.compare(d1, d2);
                        }
                    }).orElseThrow(null)
            );

            closestTurns.add(gameStates.get(minIndex));
            distances.set(minIndex, Double.MAX_VALUE);
            
            if(gameStates.size() != minIndex){
                if(!((gameStates.get(minIndex).length - gameStates.get(minIndex+1).length) > 10)){
                    return new Card(reverseMapIntegerToCard(gameStates.get(minIndex)[gameStates.get(minIndex).length -1],createReverseCardMap()),null);
                }
            }
            
        }
        return null;
    }

    public static ArrayList<Card> findDeck(int[] turn, int cardNumber) {
        List<int[]> closestTurns = new ArrayList<>();
        List<Double> distances = new ArrayList<>();

        ArrayList<Card> deck = new ArrayList<>();

        if(gameStates.size() == 0){
            System.out.println("Save Load Error");
        };

        for (int i = 0; i < gameStates.size(); i++) {
            double distance = calculateEuclideanDistance(turn, gameStates.get(i));
            distances.add(distance);
        }

        for (int i = 0; i < 7; i++) {
            int minIndex = distances.indexOf(
                    distances.stream().min(new Comparator<Double>() {
                        @Override
                        public int compare(Double d1, Double d2) {
                            return Double.compare(d1, d2);
                        }
                    }).orElseThrow(null)
            );

            closestTurns.add(gameStates.get(minIndex));

            if(gameStates.size() != minIndex){
                if(!((gameStates.get(minIndex).length - gameStates.get(minIndex+1).length) > 10)){
                    for (int j = 0; j < cardNumber; j++) {
                        try {
                            deck.add(new Card(reverseMapIntegerToCard(gameStates.get(minIndex)[gameStates.get(minIndex).length - ( 1 + j )],createReverseCardMap()),null));
                        } catch (Exception e) {
                            //
                        }
                    }
                }
            }
            
            distances.set(minIndex, Double.MAX_VALUE);
        }

        return deck;
    }

    private static double calculateSequenceDistance(int[] sequence1, List<int[]> sequence2) {
        // int minLength = Math.min(sequence1.size(), sequence2.size());

        double sum = 0.0;
        for (int i = 0; i < sequence2.size(); i++) {
            sum += calculateEuclideanDistance(sequence1, sequence2.get(i));
        }
        return Math.sqrt(sum);
    }

    private static double calculateEuclideanDistance(int[] state1, int[] state2) {
        int minLength = Math.min(state1.length, state2.length);
        int maxLength = Math.max(state1.length, state2.length);
    
        double sum = 0.0;
    
        for (int i = 0; i < minLength; i++) {
            sum += Math.pow(state1[i] - state2[i], 2);
        }
    
        // Consider the remaining elements in the longer array
        for (int i = minLength; i < maxLength; i++) {
            sum += 100.0;
        }
    
        // Additional weight for equal last elements
        if (state1[state1.length-1] == state2[state2.length-1]) {
            sum -= 1000.0;
        }


        if(sum <= 0){
            return 0;
        }

        return Math.sqrt(sum);
    }
    
    public static String reverseMapIntegerToCard(int mappedValue, Map<Integer, String> reverseCardMap) {
        String cardString = reverseCardMap.get(mappedValue);
        if (cardString != null) {
            return cardString;
        } else {
            throw new IllegalArgumentException("Mapped value not recognized: " + mappedValue);
        }
    }

    public static Map<Integer, String> createReverseCardMap() {
        Map<String, Integer> cardMap = createCardMap();
        Map<Integer, String> reverseCardMap = new HashMap<>();

        for (Map.Entry<String, Integer> entry : cardMap.entrySet()) {
            reverseCardMap.put(entry.getValue(), entry.getKey());
        }

        return reverseCardMap;
    }

    private static Map<String, Integer> createCardMap() {
        Map<String, Integer> cardMap = new HashMap<>();
        // Number cards
        for (int i = 0; i <= 9; i++) {
            cardMap.put("RED " + i, i);
            cardMap.put("BLUE " + i, i + 10);
            cardMap.put("GREEN " + i, i + 20);
            cardMap.put("YELLOW " + i, i + 30);
        }

        cardMap.put("RED SKIP", 40);
        cardMap.put("RED REVERSE", 41);
        cardMap.put("RED DRAW_TWO", 42);
        cardMap.put("BLUE SKIP", 43);
        cardMap.put("BLUE REVERSE", 44);
        cardMap.put("BLUE DRAW_TWO", 45);
        cardMap.put("GREEN SKIP", 46);
        cardMap.put("GREEN REVERSE", 47);
        cardMap.put("GREEN DRAW_TWO", 48);
        cardMap.put("YELLOW SKIP", 49);
        cardMap.put("YELLOW REVERSE", 50);
        cardMap.put("YELLOW DRAW_TWO", 51);


        cardMap.put("WILD WILD", 52);
        cardMap.put("WILD WILD_DRAW_FOUR", 53);

        return cardMap;
    }
    

    public static void main(String[] args) {

        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(0);
        numbers.add(1);

        Board board = new Board(numbers, true);
        board.simulate(100);

        
        for (int i = 0; i < board.games.size(); i++) {
            for (int j = 0; j < board.games.get(i).size(); j++) {                
                KNN.winners.add(board.winners.get(i));
                KNN.gameStates.add(board.games.get(i).get(j));

            }
        }

        System.out.println(gameStates.size());
        KNN.saveArrays();

        // KNN.initializeKNN();
        // System.out.println(KNN.gameStates.size());

        
        for (int i = 0; i < KNN.winners.size(); i++) {
            State.writeArrayListToCSV(new int[]{KNN.winners.get(i)}, "winners.csv");
        }

        // double prob = findProbability(board.turns, 5);
        // System.out.println(prob);
    }

    public static void saveGameStates(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(gameStates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGameStates(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            gameStates = (ArrayList<int[]>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void saveWinners(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(winners);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadWinners(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            winners = (ArrayList<Integer>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}