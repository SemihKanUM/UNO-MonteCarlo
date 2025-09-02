package Logic.Bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Logic.Board;
import Logic.Card;
import Logic.KNN;
import Logic.Player;
import it.unimi.dsi.fastutil.Arrays;

import static java.lang.Math.*;

public class MCTSBot {
    // Define hyperparameters
    static double expoitationCoefficient;
    static long runtime; //miliseconds
    static int RollOutSimulations;


    // Define the root-node
    static long totalVisitRoot = 1L;
    Node root;
    Card bestCard;


    public MCTSBot(ArrayList<Card> deck, Card boardcard, Card.Color boardColor, int oponentCards, int[] turns) {
        setHyperparameters();
        this.root = new Node(deck, boardcard, oponentCards, null, boardColor, true,turns);
        search();
    }

    void setHyperparameters() {
        runtime = 120;
        expoitationCoefficient = 0.5;
        RollOutSimulations = 100;
    }

    private void search() {
        long t = System.currentTimeMillis();
        long end = t+runtime;
        // Run for a designated amount of miliseconds
        while(System.currentTimeMillis() < end) {
            root.expand();

            if (totalVisitRoot > Long.MAX_VALUE - 1) {
                break;
            }
        }
    }

    /**
     * Return the card we can best play
     * @return Card
     */
    public Card getBestCard() {
        
        if(root.getBestChild(true) != null){
            bestCard = root.getBestChild(true).playedCard;
        }
        else{
            bestCard = null;
        }

        return bestCard;
    }
}

class Node {

    boolean isRoot;
    int visits;
    double totalScore;
    double ownRollOutScore;

    // This is the state of three game
    State state;
    ArrayList<Node> children;
    final int[] turns;
    final ArrayList<Card> deck;
    final Card boardCard;
    final int oponentCards;
    final Card playedCard;
    final Card.Color boardColor;

    public Node (ArrayList<Card> deck, Card boardCard, int oponentCards, Card playedCard, Card.Color boardColor, boolean isRoot, int[] turns) {
        this.deck = cloneDeck(deck);
        this.turns = new int[turns.length];
        System.arraycopy(turns, 0, turns, 0, turns.length);
        this.boardCard = boardCard;
        this.oponentCards = oponentCards;
        this.playedCard = playedCard;
        this.boardColor = boardColor;
        this.isRoot = isRoot;

        // if(turns.size() != 0){
        //     System.out.println("------");
        //     for (int i = 0; i < turns.get(turns.size()-1).length; i++) {
        //         System.out.print(turns.get(turns.size()-1)[i] + " ");
        //     }
        //     System.out.println();
        //     System.out.println("++++++");

        // }
        
        this.children = new ArrayList<>();
        visits = 0;
        totalScore = 0;

        // Make sure to use this.deck instead of deck, this is important to avoid object cloning the wrong way
        this.state = new State(cloneDeck(deck), this.boardCard, this.oponentCards,this.turns);
    }

    public void expand() {

        if (children.isEmpty()) {

            if (getPlayableDeck().size() <= 0 && visits > 0) {
                // No children bc deck is empty
                // We dont want to do a rollout
                // System.out.println("deck was empty");
                visits++;
                MCTSBot.totalVisitRoot++;
                return;
            }

            // The node has not been visited before and no Rollout have been so far
            // In the beginning own Rollout is the same as total score, bc Node does not have any children yet.
            totalScore = state.RollOut(true);
            ownRollOutScore = totalScore;
            visits++;
            MCTSBot.totalVisitRoot++;
            createChildren();

        } else {
            // this node has children, we need to go find a leaf node before we do the rest
            Node bestChildNode = getBestChild();
            bestChildNode.expand();

            // So we expanded the child node who has now a few happy children of its own. We can look at our own scores now.
            // total score should be the sum of score from our children plus our own rollout score.
            totalScore = ownRollOutScore;
            for (Node child : children) {
                totalScore += child.getTotalScore();
            }

            visits++;
        }
    }

    /**
     * Function that find the best node based on the UCB score of the children
     * @return Node
     */
    public Node getBestChild() {

        if (children.isEmpty()) {
            System.out.println("The node has no children");
            this.printNode();
        }

        double bestValue = -9999;
        Node bestNode = null;

        for (Node node : children) {

            double nodeValue = node.UCB();

            if (nodeValue > bestValue) {
                bestValue = nodeValue;
                bestNode = node;
            }
        }

        return bestNode;
    }

    public Node getBestChild(boolean BasedOnTotalScore) {

        if (children.isEmpty()) {
            System.out.println("The node has no children");
            this.printNode();
        }

        double bestValue = -9999;
        Node bestNode = null;

        for (Node node : children) {

            double nodeValue = node.getTotalScore();

            if (nodeValue > bestValue) {
                bestValue = nodeValue;
                bestNode = node;
            }
        }

        return bestNode;
    }

    public double UCB() { return totalScore + MCTSBot.expoitationCoefficient * Math.sqrt(Math.log(MCTSBot.totalVisitRoot) / (visits + 1e-4)); }

    public void createChildren() {

        for (Card card : getPlayableDeck()) {
            // Nu hebben we de speelbare kaart en een kopie van het dek zonder die kaart
            ArrayList<Card> nextDeck = getNextDeck(card);

            int[] arrayDeck = Board.mapCardsToIntegers(nextDeck);
            ArrayList<Card> cardlist = new ArrayList<Card>();
            cardlist.add(card);
            int number = Board.mapCardsToIntegers(cardlist)[0];
            int[] combinedArray = concatenateArrays(turns, arrayDeck, number);

            // Zet de volgende kaart die gespeeld zal worden
            Card nextCard = KNN.findCard(combinedArray, 7);
            //System.out.println(nextCard);

            //Card nextCard = setNextCard(card);

            //System.out.println(nextCard);

            
            // Controleer of de tegenstander kaarten heeft om te spelen
            int updatedOpponentCards = findOpponentCardNumber(card.getType());

            // Maak een nieuwe knoop met de bijgewerkte gegevens
            Node nextNode = new Node(nextDeck, nextCard, updatedOpponentCards, card, nextCard.getColor(), false,concatenateArrays(turns, new int[0], number));
            //Node nextNode = new Node(nextDeck, nextCard, updatedOpponentCards, card, nextCard.getColor(), false,turns);

            children.add(nextNode);
        }
    }

    private ArrayList<Card> getPlayableDeck() {

        ArrayList<Card> playableDeck = new ArrayList<>();
        for (Card card : deck) {
            if (isPlayableCard(card, boardCard)) {
                playableDeck.add(card);
            }
        }
        return playableDeck;
    }

    private static int[] concatenateArrays(int[] turns, int[] deck, int number) {
        int combinedLength = turns.length + deck.length + 1;
        int[] combinedArray = new int[combinedLength];

        // Copy elements from turns and deck into combinedArray
        System.arraycopy(turns, 0, combinedArray, 0, turns.length);
        System.arraycopy(deck, 0, combinedArray, turns.length, deck.length);

        // Set the last element to the single integer value
        combinedArray[combinedLength - 1] = number;

        return combinedArray;
    }

    /**
     * This fucntion find the number of card the opponent in the next node will have. This can be valueable for the strategy
     * @param playedType Card.Type
     * @return number of cards our opponent has next turn (Next node).
     */
    private int findOpponentCardNumber(Card.Type playedType) {

        if (playedType == Card.Type.DRAW_TWO) {
            // opponent must draaw 2
            return oponentCards + 2;
        } else if (playedType == Card.Type.WILD_DRAW_FOUR) {
            // opponent must draw 4
            return oponentCards + 4;
        } else if (playedType == Card.Type.SKIP) {
            // opponent loses his turn
            return oponentCards;
        } else if (playedType == Card.Type.REVERSE) {
            // opponent loses his turn
            return oponentCards;
        } else if (oponentCards - 1 <= 0 ) {
            return oponentCards;
        } else {
            return oponentCards - 1;
        }
    }

    private Card setNextCard(Card playedCard) {
        Random rand = new Random();

        if (playedCard.getType() == Card.Type.NUMBER ||
                playedCard.getType() == Card.Type.DRAW_TWO ||
                playedCard.getType() == Card.Type.SKIP ||
                playedCard.getType() == Card.Type.REVERSE) {
            Card nextCard = new Card(null, Card.Type.NUMBER, playedCard.getColor(), rand.nextInt(9));
            return nextCard;
        } else if (playedCard.getType() == Card.Type.WILD ||
                playedCard.getType() == Card.Type.WILD_DRAW_FOUR) {
            int nrBlue = 0;
            int nrRed = 0;
            int nrGreen = 0;
            int nrYellow = 0;

            for (Card card : deck) {
                if (card.getColor() == Card.Color.BLUE) {
                    nrBlue++;
                } else if (card.getColor() == Card.Color.RED) {
                    nrRed++;
                } else if (card.getColor() == Card.Color.GREEN) {
                    nrGreen++;
                } else if (card.getColor() == Card.Color.YELLOW) {
                    nrYellow++;
                }
            }

            Card.Color mostCommonColor = getMostCommonColor(nrBlue, nrRed, nrGreen, nrYellow);

            // Nu kun je een kaart maken met de meest voorkomende kleur
            Card nextCard = new Card(null, Card.Type.NUMBER, mostCommonColor, rand.nextInt(9));
            return nextCard;
        } else {
            System.out.println("An unexpected output has occurred in the setNextCardMethod, this should not happen!");
            return null;
        }
    }

    private Card.Color getMostCommonColor(int nrBlue, int nrRed, int nrGreen, int nrYellow) {
        int maxCount = Math.max(nrBlue, Math.max(nrRed, Math.max(nrGreen, nrYellow)));

        if (maxCount == nrBlue) {
            return Card.Color.BLUE;
        } else if (maxCount == nrRed) {
            return Card.Color.RED;
        } else if (maxCount == nrGreen) {
            return Card.Color.GREEN;
        } else {
            return Card.Color.YELLOW;
        }
    }



    /**
     * Function for returning the next deck without messing up the deck in this node
     * @param card the card we want to play
     * @return a new deck
     */
    private ArrayList<Card> getNextDeck(Card card) {

        // Create a copy of the deck
        ArrayList<Card> nextDeck = cloneDeck(this.deck);

        for (Card cardNextDeck : nextDeck) {

            // If the given card and card in the loop are the same
            if (card.getType() == cardNextDeck.getType() &&
                    card.getNumber() == cardNextDeck.getNumber() &&
                    card.getColor() == cardNextDeck.getColor()) {

                // remove that card from the next deck and stop the search
                nextDeck.remove(cardNextDeck);
                break;
            }
        }

        return nextDeck;
    }

    /**
     * Function to make a clone of the deck so that we don't interfere with the original objects
     * @param deck old deck
     * @return new deck
     */
    private ArrayList<Card> cloneDeck(ArrayList<Card> deck) {
        // Function to create a new deck with cloned cards of the old deck.
        ArrayList<Card> newDeck = new ArrayList<>();

        for (Card card: deck) {
            newDeck.add(card.clone());
        }

        return newDeck;
    }

    /**
     * Helper method to determine if a card is playable.
     * Adjust the condition according to your game's rules for a playable card.
     */
    public boolean isPlayableCard(Card playerChosenCard, Card boardCard){

        // check the color of cards
        if (playerChosenCard.colorOfCard() == boardColor){
            return true;
        }

        // check the wild cards
        if (playerChosenCard.getType() == Card.Type.WILD){
            return true;
        } 
        // check the wild draw cards
        if (playerChosenCard.getType() == Card.Type.WILD_DRAW_FOUR)
        {
            return true;
        }

        // check draw2 cards
        // ALLOWS TO COMBOS
        if (boardCard.getType() == Card.Type.DRAW_TWO) {
            if (playerChosenCard.getType() == Card.Type.DRAW_TWO) {
                return true;
            } else if (playerChosenCard.colorOfCard() == boardCard.getColor()) {
                return true;
            } else {
                return false;
            }
        }
        // revers card case
        if (boardCard.getType().equals(Card.Type.REVERSE) && playerChosenCard.getType().equals(Card.Type.REVERSE)){
            return true;
        }
        // check the number of number cards
        if (playerChosenCard.getType().equals(Card.Type.NUMBER) && boardCard.getType().equals(Card.Type.NUMBER)){
            if (playerChosenCard.getNumber() == boardCard.getNumber()){
                return true;
            }
        }
        // check the skip cards
        if (playerChosenCard.getType().equals(Card.Type.SKIP) && boardCard.getType().equals(Card.Type.SKIP)){
            return true;
        }
        return false;
    }

    public void printNode() {
        System.out.println("Cards in deck: " + deck);
        System.out.println("Board card: " + boardCard);
        System.out.println("Board colour: " + boardColor);
        System.out.println("Opponent nr of Cards: " + oponentCards);
        System.out.println("Nr. of children: " + children.size());
        System.out.println("Is this the root: " + isRoot);
    }

    public double getTotalScore() {
        return this.totalScore;
    }
}

class State {
    /** State of the game is three things:
     * own cards
     * Board card
     * Opponent nr of Cards
     * Left over deck
     * We create a new board so that we don't have mixing objects
     */
    ArrayList<Card> deck;
    Card boardCard;
    final int oponentCards;
    ArrayList<Player> players;
    Board board;
    int[] turns;

    public State(ArrayList<Card> deck, Card boardCard, int oponentCards, int[] turns) {
        
        this.deck = deck;
        this.boardCard = boardCard;
        this.oponentCards = oponentCards;
        this.turns = new int[turns.length];
        System.arraycopy(turns, 0, turns, 0, turns.length);

        // Set the board as it should be
        Player randomBot = new MCTSRandomBot(1);
        Player mcBot = new MCTSRandomBot(2);

        players = new ArrayList<>();

        players.add(randomBot);
        players.add(mcBot);
    }


    private Board setBoard() {
        // Create a game board with the players and the current board card.
        board = new Board(players, boardCard.clone());

        // Generate and get the main deck
        ArrayList<Card> maindeck = board.getMaindeck();
        maindeck = removeCard(maindeck, deck);

        // set and remove the board card
        board.setBoardCard(boardCard.clone());
        maindeck = removeCard(maindeck, boardCard);

        

        int[] arrayDeck = Board.mapCardsToIntegers(deck);
        ArrayList<Card> cardlist = new ArrayList<Card>();
        cardlist.add(boardCard);
        int number = Board.mapCardsToIntegers(cardlist)[0];
        int[] combinedArray = concatenateArrays(turns, arrayDeck, number);

        // get the opponent cards

        //ArrayList<Card> opponentDeck = new ArrayList<>(maindeck.subList(0, oponentCards));


        ArrayList<Card> opponentDeck = KNN.findDeck(combinedArray, oponentCards);
        maindeck = removeCard(maindeck, opponentDeck);

        // Update maindeck
        board.setMaindeck(maindeck);

        // Very confusing, needs refactoring!
        if (board.getPlayers().get(0).getNumber() == 1) {
            // first player in this list is the random player
            board.getPlayers().get(0).setDeck(opponentDeck);
            board.getPlayers().get(1).setDeck(cloneDeck(deck));
        } else if (board.getPlayers().get(0).getNumber() == 2) {
            // first player is the MCTS
            board.getPlayers().get(1).setDeck(opponentDeck);
            board.getPlayers().get(0).setDeck(cloneDeck(deck));
        } else {
            System.out.println("Error, we where not able to set the deck for RollOut, this should not happen!");
        }
        return board;
    }


    private static int[] concatenateArrays(int[] turns, int[] deck, int number) {
        int combinedLength = turns.length + deck.length + 1;
        int[] combinedArray = new int[combinedLength];

        // Copy elements from turns and deck into combinedArray
        System.arraycopy(turns, 0, combinedArray, 0, turns.length);
        System.arraycopy(deck, 0, combinedArray, turns.length, deck.length);

        // Set the last element to the single integer value
        combinedArray[combinedLength - 1] = number;

        return combinedArray;
    }


    public double RollOut(boolean multiple) {

        board = setBoard();

        

        //board.setTurns(turns);

        // System.out.println("aaaaaaa");
        
        // for (int i = 0; i < turns.get(turns.size()-1).length; i++) {
        //     System.out.print(turns.get(turns.size()-1)[i] + " ");
        // }
        // System.out.println();

        // System.out.println("bbbbbbbb");



        // for (int i = 0; i < turns.get(turns.size()-1).length; i++) {
        //     System.out.print(turns.get(turns.size()-1)[i] + " ");
        // }
        // System.out.println();

        double nrWins = 0;

        for (int i=0; i<MCTSBot.RollOutSimulations; i++) {

            board = setBoard();
            board.simulate(1);

            // 1 means that the RB won, 2 means that the MCTS won
            int winner = board.winner;

            switch (winner) {
                case 1:
                    // We lost
                    break;
                case 2:
                    // We won
                    nrWins += 1;
                    break;
                default:
                    // Weird things have happened, display this
                    System.out.println("Check the RollOut method in the state-class, an unexpected output has occurred!");
                    break;
            }
        }

        return nrWins/MCTSBot.RollOutSimulations;




        // int k = 7;
        // return KNN.findProbability(turns, k);
    }

    private static ArrayList<Card> removeCard(ArrayList<Card> maindeck, ArrayList<Card> deck) {
        for (Card card : deck) {
            for (int i = 0; i < maindeck.size(); i++) {
                Card maincard = maindeck.get(i);

                if (maincard.getType() == card.getType() &&
                        maincard.getNumber() == card.getNumber() &&
                        maincard.getColor() == card.getColor()) {

                    maindeck.remove(i);
                    break;
                }
            }
        }
        return maindeck;
    }

    private static ArrayList<Card> removeCard(ArrayList<Card> maindeck, Card card) {
        for (int i = 0; i < maindeck.size(); i++) {
            Card maincard = maindeck.get(i);

            if (maincard.getType() == card.getType() &&
                    maincard.getNumber() == card.getNumber() &&
                    maincard.getColor() == card.getColor()) {

                maindeck.remove(i);
                break;
            }
        }
        return maindeck;
    }

    private ArrayList<Card> cloneDeck(ArrayList<Card> deck) {
        // Function to create a new deck with cloned cards of the old deck.
        ArrayList<Card> newDeck = new ArrayList<>();

        for (Card card: deck) {
            newDeck.add(card.clone());
        }
        return newDeck;
    }
}