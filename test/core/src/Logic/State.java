package Logic;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVWriter;
import it.unimi.dsi.fastutil.Arrays;



public class State {
    private int simulationNumber;
    private int botNumber;
    private int turnNumber;
    private int currentPlayerScore;
    private int otherPlayerScore;
    private int normalRedCards;
    private int normalGreenCards;
    private int normalBlueCards;
    private int normalYellowCards;
    private int skipCards;
    private int reverseCards;
    private int draw2Cards;
    private int draw4Cards;
    private int wildCards;
    private int normalRedCardsPlayable;
    private int normalGreenCardsPlayable;
    private int normalBlueCardsPlayable;
    private int normalYellowCardsPlayable;
    private int skipCardsPlayable;
    private int reverseCardsPlayable;
    private int draw2CardsPlayable;
    private int draw4CardsPlayable;
    private int wildCardsPlayable;
    private int openCardColor;
    private int openCardType;
    private boolean playable = false;

    private int[] stateArray;
    private Board board;
    private ArrayList<Card> currentDeck;

    public State(Board board){
        this.board = board;
        initializeParameters(); 
    }


    public void saveStateToArray(){
        setState();
        stateArray = new int[]{
            simulationNumber,
            botNumber,
            turnNumber,
            currentPlayerScore,
            otherPlayerScore,
            normalRedCards,
            normalGreenCards,
            normalBlueCards,
            normalYellowCards,
            skipCards,
            reverseCards,
            draw2Cards,
            draw4Cards,
            wildCards,
            normalRedCardsPlayable,
            normalGreenCardsPlayable,
            normalBlueCardsPlayable,
            normalYellowCardsPlayable,
            skipCardsPlayable,
            reverseCardsPlayable,
            draw2CardsPlayable,
            draw4CardsPlayable,
            wildCardsPlayable,
            openCardColor,
            openCardType
        };
    }

    public int[] getStateArray() {
        return stateArray;
    }

    public void saveStateToCSV() {
        String fileName;
        fileName = "simulation_data.csv";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String data = String.format("%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d%n",
                    simulationNumber,
                    botNumber,
                    turnNumber,
                    currentPlayerScore,
                    otherPlayerScore,
                    normalRedCards,
                    normalGreenCards,
                    normalBlueCards,
                    normalYellowCards,
                    skipCards,
                    reverseCards,
                    draw2Cards,
                    draw4Cards,
                    wildCards,
                    normalRedCardsPlayable,
                    normalGreenCardsPlayable,
                    normalBlueCardsPlayable,
                    normalYellowCardsPlayable,
                    skipCardsPlayable,
                    reverseCardsPlayable,
                    draw2CardsPlayable,
                    draw4CardsPlayable,
                    wildCardsPlayable,
                    openCardColor,
                    openCardType);
    
                writer.write(data);
                } catch (IOException e) {
                    e.printStackTrace();
                }
    }



    
    public void cleanFiles(){
        try (PrintWriter writer = new PrintWriter(new FileWriter("Player1.csv"))) {
            // This will create an empty file, effectively clearing its contents
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("Player2.csv"))) {
            // This will create an empty file, effectively clearing its contents
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }

    public static void writeArrayListToCSV(int[] arrayList, String csvFilePath) {
        try (FileWriter writer = new FileWriter(csvFilePath, true)) {
            // Write the elements to the CSV file
            for (int i = 0; i < arrayList.length; i++) {
                writer.append(Integer.toString(arrayList[i]));

                // Add a comma if it's not the last element in the ArrayList
                if (i < arrayList.length - 1) {
                    writer.append(",");
                }
            }

            // Move to the next line after writing the ArrayList
            writer.append("\n");

            //System.out.println("CSV file has been updated successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    
    public void setState(){
        initializeParameters();
        
        playable = false;

        simulationNumber = board.numberOfSimulations;
        turnNumber = board.numberOfTurns;

        botNumber = board.getCurrentPlayer().getNumber();
        currentPlayerScore = board.getCurrentPlayer().calculatePoints();
        for (int i = 0; i < board.getPlayers().size(); i++) {
            if(board.getCurrentPlayer() != board.getPlayers().get(i)){
                otherPlayerScore = board.getPlayers().get(i).calculatePoints();
            }
        }
        
        // Loop through each color and assign an integer value
        switch (board.getBoardColor()) {
            case RED:
                openCardColor = 1;
                break;
            case BLUE:
                openCardColor = 2;
                break;
            case GREEN:
                openCardColor = 3;
                break;
            case YELLOW:
                openCardColor = 4;
                break;
            case WILD:
                openCardColor = 0;
                break;
            default:
                break;
        }

        switch (board.getBoardCard().getType()) {
            case NUMBER:
                openCardType = 1;
                break;
            case SKIP:
                openCardType = 2;
                break;
            case REVERSE:
                openCardType = 3;
                break;
            case DRAW_TWO:
                openCardType = 4;
                break;
            case WILD:
                openCardType = 5;
                break;
            case WILD_DRAW_FOUR:
                openCardType = 6;
                break;
            default:
                break;
        }

        currentDeck = board.getCurrentPlayer().getDeck();

        for (int i = 0; i < currentDeck.size(); i++) {
            Card card = currentDeck.get(i);

            if(board.checkChoose(card)){
                playable = true;
            }

            // Increment counters based on the card type
            switch (card.getType()) {
                case NUMBER:
                    switch (card.colorOfCard()) {
                        case RED:
                            if(playable){
                                normalRedCardsPlayable++;
                            }
                            normalRedCards++;
                            break;
                        case BLUE:
                            if(playable){
                                normalBlueCardsPlayable++;
                            }
                            normalBlueCards++;
                            break;
                        case GREEN:
                            if(playable){
                                normalGreenCardsPlayable++;
                            }
                            normalGreenCards++;
                            break;
                        case YELLOW:
                            if(playable){
                                normalYellowCardsPlayable++;      
                            }
                            normalYellowCards++;
                            break;
                        default:
                            break;
                        }
                    break;
                case SKIP:
                    if(playable){
                        skipCardsPlayable++;         
                    }
                    skipCards++;
                    break;

                case REVERSE:
                    if(playable){
                        reverseCardsPlayable++;  
                    }
                    reverseCards++;
                    break;

                case DRAW_TWO:
                    if(playable){
                        draw2CardsPlayable++;    
                    }
                    draw2Cards++;
                    break;

                case WILD:
                    if(playable){
                        wildCardsPlayable++;
                    }
                    wildCards++;
                    break;

                case WILD_DRAW_FOUR:
                    if(playable){
                        draw4CardsPlayable++;
                    }
                    draw4Cards++;
                    break;

                default:
                    // Handle other types if needed
                    break;
            }
        }    
    }

    public void initializeParameters() {
        simulationNumber = 0;
        turnNumber = 0;
        currentPlayerScore = 0;
        otherPlayerScore = 0;
        normalRedCards = 0;
        normalGreenCards = 0;
        normalBlueCards = 0;
        normalYellowCards = 0;
        skipCards = 0;
        reverseCards = 0;
        draw2Cards = 0;
        draw4Cards = 0;
        wildCards = 0;
        normalRedCardsPlayable = 0;
        normalGreenCardsPlayable = 0;
        normalBlueCardsPlayable = 0;
        normalYellowCardsPlayable = 0;
        skipCardsPlayable = 0;
        reverseCardsPlayable = 0;
        draw2CardsPlayable = 0;
        draw4CardsPlayable = 0;
        wildCardsPlayable = 0;
        openCardColor = 0;
        openCardType = 0;
        playable = false;
    }


    public int scoreOfotherPlayer(){
        return -1;
    }
}

// Number of Current Simulation   = s
// Number of current turn of simulation s = t
// score of current player = s_c
// score of other player = s_o
// Number of normal red cards = r_n
// Number of normal green cards = g_n
// Number of normal blue cards = b_n
// Number of normal yellow cards = y_n
// number of skip card = sk
// number of reverse cards = rev
// number of draw2 = dr2
// number of draw4 = dr4
// number of wild  = w
// no of normal red cards playable = r_n_p
// no of normal green cards playable = g_n_p
// no of normal blue cards playable = b_n_p
// no of normal yellow cards playable =y_n_p
// no of skip playable = sk_p
// no of reverse playable = rev_p
// no of draw2 playable = dr2_p
// no of draw4 playable = dr4_p
// no of wild playabale = w_p
// color of open card = R
// type of open card = T
// S = (  s, t, s_c, s_o, r_n, g_n, b_n, y_n, sk, rev, dr2, dr4, w , r_n_p,  g_n_p, b_n_p, y_n_p, sk_p, rev_p , dr2_p , dr4_p , w_p, R, T)