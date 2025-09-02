package Logic;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import Logic.Bot.Bot;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import Logic.Bot.MonteCarloBot;
import Logic.Bot.RandomBot;

public class Board implements Cloneable {
    public ArrayList<Player> players;
    private int currentIndex;
    public ArrayList<Card> maindeck;
    public Card boardCard;
    private Card.Color boardColor;
    private ArrayList<Card> penaltyCards;
    public boolean chooseColor;
    public boolean setIndex = false;
    public boolean isClockwise = true; // true for clockwise, false for counter-clockwise
    public boolean simulation;
    public int numberOfTurns;
    public int numberOfSimulations;

    public boolean endGame = false;
    public boolean nextPlayer = false;
    public boolean drawCard = false;
    public boolean unoClicked = false;
    public Card playerChoosenCard;
    public boolean canPlay = true;
    public int nextIndex;
    public int winner = -1;

    public ArrayList<int[]> turns = new ArrayList<>();
    public ArrayList<Integer> winners;
    public ArrayList<ArrayList<int[]>> games;
    public State state;

    public ArrayList<Card> playedCards;


    private BufferedWriter csvWriter;


    public Board(ArrayList<Integer> num, boolean simulation) {
        state = new State(this);

        this.simulation = simulation;
        this.maindeck = new ArrayList<>();
        this.players = new ArrayList<>();
        this.penaltyCards = new ArrayList<>();
        this.currentIndex = 0;
        generatemainDeck();
        setPlayers(num);
        distributeCards();

        try {
            // Open a CSV file for writing
            csvWriter = new BufferedWriter(new FileWriter("game_states.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Board(ArrayList<Player> players , Card boardCard){
        state = new State(this);

        this.simulation = true;
        maindeck = new ArrayList<>();
        this.players = players;
        this.penaltyCards = new ArrayList<>();
        this.currentIndex = 0;
        this.boardColor = boardCard.colorOfCard();
        this.boardCard = boardCard;

        generatemainDeck();
    }

    public void setTurns(ArrayList<int[]> turns){
        this.turns = turns;
    }

    public void setNumberOfSimulations(int numberOfsim){
        this.numberOfSimulations = numberOfsim;
    }

    public void simulate(int simAmount) {
        // turns = new ArrayList<>();
        playedCards = new ArrayList<>();
        winners = new ArrayList<>();
        games = new ArrayList<>();


        while(true){
    
            if(endGame || numberOfTurns == 350) {

                for (Player player : players) {
                    if (player.getDeck().size() == 0) {
                        winner = player.getNumber();
                    }
                }

                winners.add(winner);

                games.add(turns);
                if (simAmount != 1){
                    turns = new ArrayList<>();
                    playedCards = new ArrayList<>();
                }
                endGame = false;
                numberOfSimulations++;
                initiliaze();
                state.cleanFiles();

                
    
                if(numberOfSimulations == simAmount)  { 
                    numberOfSimulations = 0;
                    break;
                } else {
                    Collections.shuffle(players);
                    numberOfTurns = 0;
                }
            } else{  
                numberOfTurns++;
                playedCards.add(boardCard);

                ArrayList<Card> printList = new ArrayList<>(playedCards);


                playCard();

    
                for (int index = 0; index < players.get((currentIndex +1)%2).getDeck().size(); index++) { 
                    printList.add(players.get((currentIndex+1) %2).getDeck().get(index));
                }
    
                printList.add(boardCard);

                turns.add(mapCardsToIntegers(printList));

                // Write the game state to CSV file
                State.writeArrayListToCSV(mapCardsToIntegers(printList), "turns.csv");                

                // int[] array  = mapCardsToIntegers(printList);

                // for (int i = 0; i < array.length; i++) {
                //     System.out.print(array[i] + " ");
                // }
                // System.out.println();

                //State.writeArrayListToCSV(printList, "turns.csv");

                //System.out.println(printList);  

                // printArrayList(ArrayList<Card> printList);


                // if(turns.size() != 0){
                //     for (int i = 0; i < turns.get(turns.size()-1).length; i++) {
                //         System.out.print(turns.get(turns.size()-1)[i] + " ");
                //     }
                //     System.out.println();
                // }
            }              
        }
        
        // for (Player player : players) {
        //     if (player.getDeck().size() == 0) {
        //         winner = player.getNumber();
        //     }
        // }

        // System.out.println(turns.size());
        // System.out.println(games.size());
        
        // for (int java = 0; java < games.size(); java++) {
        //     for (int i = 0; i < games.get(java).size(); i++) {
        //         for (int j = 0; j < games.get(java).get(i).length; j++) {
        //             System.out.print(games.get(java).get(i)[j] + " ");
        //         }
        //         System.out.println();
        //     }
        // }
        
    }



    
    public void playCard(){
        Bot bot = (Bot) getCurrentPlayer();
        bot.setState(getCurrentPlayer().getDeck(), boardCard, getBoardColor(),this);
        Card.Color botColor = bot.selectColor();

        if(canPlay){
            if(penaltyCase()){
                nextIndex = (getCurrentIndex()+1)%getPlayers().size();
                canPlay = false;
                nextPlayer = true;
            }
            else if(checkTheCards()){
                nextIndex = (getCurrentIndex()+1)%getPlayers().size();
                Card c = giveCardToPlayer();
                if(checkChoose(c)){
                    nextIndex = playTurn(c);
                    drawCard = false;
                    canPlay = false;
                    nextPlayer = true;
                    if(chooseColor){
                        setBoardColor(botColor);
                    }
                }
                else{
                    nextIndex = (getCurrentIndex()+1)%getPlayers().size();
                    drawCard = false;
                    canPlay = false;
                    nextPlayer = true;
                }
                
            }
            else{
                playerChoosenCard = bot.selectCard();

                if (playerChoosenCard == null) {
                    nextIndex = (getCurrentIndex() + 1) % 2;
                } else {
                    nextIndex = playTurn(playerChoosenCard);
                }

                if (setIndex) {
                    canPlay = false;
                    nextPlayer = true;
                    setIndex = false;
                    if (chooseColor) {
                        setBoardColor(botColor);
                    }
                    chooseColor = false;
                }
            }
        }
        
        if(getCurrentPlayer().getDeck().size() == 1){
            unoClicked = true;
        }

        if(numberOfSimulations != 0){
            state.setState();
            state.saveStateToCSV();
        }
        

        // if(getCurrentPlayer().getNumber() == 1){ // Monte Carlo
        //     state.saveStateToArray();
        //     turns.add(state.getStateArray());
        // }

        if(endGame()){
            endGame = true;
        }
        else{
            switchToNextPlayer();
        }
    }

    public boolean endGame(){
        if(getCurrentPlayer().getDeck().size() == 0){
            canPlay = false;
            return true;
        }
        return false;
    }
    
    @Override
    public Board clone() {
        try {
            // Perform a shallow copy by calling the superclass clone method
            return (Board) super.clone();
        } catch (CloneNotSupportedException e) {
            // Handle the exception if necessary
            return null;
        }
    }


    public boolean canPlay(){
        if(penaltyCase() || checkTheCards()){
            //go the the next player
            return true;
        }
        return false;
    }

    public void initiliaze(){
        this.maindeck = new ArrayList<>();
        this.penaltyCards = new ArrayList<>();
        this.currentIndex = 0;
        generatemainDeck();
        restartDecks();
        distributeCards();
    }

    public void restartDecks(){
        for (int i = 0; i < players.size(); i++) {
            players.get(i).getDeck().clear();
        }
    }

    public ArrayList<Player> getPlayers(){
        return players;
    }
    public ArrayList<Card> getMaindeck() {
        return maindeck;
    }
    public void setMaindeck(ArrayList<Card> maindeck) {
        this.maindeck = maindeck;
    }
    public int getCurrentIndex(){
        return currentIndex;
    }
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public Player getCurrentPlayer(){
        return players.get(currentIndex);
    }

    public void setPlayers(ArrayList<Integer> num) {
        // Change to desired number of players

        for (int number : num) {

            if (number < 0) {
                Player realPlayer = new Player(number);
                players.add(realPlayer);
            } else if (number % 2 == 0) {
                Player monteBot = new MonteCarloBot(number);
                players.add(monteBot);
            } else {
                Player RandomBot = new RandomBot(number);
                players.add(RandomBot);
            }
        }

        // Player monteBot = new MonteCarloBot(1);
        // players.add(monteBot);

        // Player RandomBot = new RandomBot(1);
        // players.add(RandomBot); 

        // Player RandomBot2 = new RandomBot(2);
        // players.add(RandomBot2);

    }

    public static int[] mapCardsToIntegers(List<Card> cardList) {
        Map<String, Integer> cardMap = createCardMap();
        int[] mappedCards = new int[cardList.size()];

        for (int i = 0; i < cardList.size(); i++) {
            Card card = cardList.get(i);
            String cardString = card.toString();
            Integer mappedValue = cardMap.get(cardString);
            if (mappedValue != null) {
                mappedCards[i] = mappedValue;
            } else {
                throw new IllegalArgumentException("Card not recognized: " + cardString);
            }
        }

        return mappedCards;
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

    public void setBoardCard(Card boardCard) {
        this.boardCard = boardCard;
    }
    public Card getBoardCard() {
        return boardCard;
    }
    public void setBoardColor(Card.Color boardColor) {
        this.boardColor = boardColor;
    }
    public Card.Color getBoardColor() {
        return boardColor;
    }
    public ArrayList<Card> getPenaltyCards() {
        return penaltyCards;
    }
    public void setPenaltyCards(ArrayList<Card> penaltyCards) {
        this.penaltyCards = penaltyCards;
    }

    public void generatemainDeck() {
        if(!simulation){
            // Generate number and special cards (0-9) for each color, and (1-9) for each color
            for (int i = 0; i < 2; i++) {
                for (Card.Color color : Card.Color.values()) {

                    if (color != Card.Color.WILD) {
                        
                        for (int number = i; number <= 9; number++) {

                            Texture cardTexture = new Texture(Gdx.files.internal(number + color.toString().toLowerCase() + ".png"));
                            TextureRegion textureRegion = new TextureRegion(cardTexture, 0 , 0 ,cardTexture.getWidth(),cardTexture.getHeight());
                            TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);

                            this.maindeck.add(new Card(textureRegionDrawable, Card.Type.NUMBER , color,  number));
                        }
                        
                        Texture cardTexture = new Texture(Gdx.files.internal(color.toString().toLowerCase() + Card.Type.SKIP.toString().toLowerCase() + ".png"));
                        TextureRegion textureRegion = new TextureRegion(cardTexture, 0 , 0 ,cardTexture.getWidth(),cardTexture.getHeight());
                        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);
                        this.maindeck.add(new Card(textureRegionDrawable,Card.Type.SKIP, color, -1));


                        cardTexture = new Texture(Gdx.files.internal(color.toString().toLowerCase() + Card.Type.REVERSE.toString().toLowerCase() + ".png"));
                        textureRegion = new TextureRegion(cardTexture, 0 , 0 ,cardTexture.getWidth(),cardTexture.getHeight());
                        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
                        this.maindeck.add(new Card(textureRegionDrawable,Card.Type.REVERSE, color, -1));


                        cardTexture = new Texture(Gdx.files.internal(color.toString().toLowerCase() + Card.Type.DRAW_TWO.toString().toLowerCase() + ".png"));
                        textureRegion = new TextureRegion(cardTexture, 0 , 0 ,cardTexture.getWidth(),cardTexture.getHeight());
                        textureRegionDrawable = new TextureRegionDrawable(textureRegion);
                        this.maindeck.add(new Card(textureRegionDrawable,Card.Type.DRAW_TWO, color, -1));
                    }
                }
            }
            // Generate Wild and Wild Draw Four cards
            for (int i = 0; i < 4; i++) {
                Texture cardTexture = new Texture(Gdx.files.internal(Card.Type.WILD.toString().toLowerCase() + ".png"));
                TextureRegion textureRegion = new TextureRegion(cardTexture, 0 , 0 ,cardTexture.getWidth(),cardTexture.getHeight());
                TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureRegion);
                this.maindeck.add(new Card(textureRegionDrawable,Card.Type.WILD, Card.Color.WILD, -1));

                cardTexture = new Texture(Gdx.files.internal(Card.Type.WILD_DRAW_FOUR.toString().toLowerCase() + ".png"));
                textureRegion = new TextureRegion(cardTexture, 0 , 0 ,cardTexture.getWidth(),cardTexture.getHeight());
                textureRegionDrawable = new TextureRegionDrawable(textureRegion);
                this.maindeck.add(new Card(textureRegionDrawable,Card.Type.WILD_DRAW_FOUR, Card.Color.WILD, -1));
            }  
        }
        else{
            // Generate number and special cards (0-9) for each color, and (1-9) for each color
            for (int i = 0; i < 2; i++) {
                for (Card.Color color : Card.Color.values()) {

                    if (color != Card.Color.WILD) {
                        
                        for (int number = i; number <= 9; number++) {

                            this.maindeck.add(new Card(null, Card.Type.NUMBER , color,  number));
                        }
                        this.maindeck.add(new Card(null,Card.Type.SKIP, color, -1));
                        this.maindeck.add(new Card(null,Card.Type.REVERSE, color, -1));
                        this.maindeck.add(new Card(null,Card.Type.DRAW_TWO, color, -1));
                    }
                }
            }
            // Generate Wild and Wild Draw Four cards
            for (int i = 0; i < 4; i++) {
                this.maindeck.add(new Card(null,Card.Type.WILD, Card.Color.WILD, -1));
                this.maindeck.add(new Card(null,Card.Type.WILD_DRAW_FOUR, Card.Color.WILD, -1));
            }
        }
        
        // Shuffle the deck
        Collections.shuffle(this.maindeck);
    }

    public void distributeCards(){

        for (int n = 0; n < 7; n++) {
            for (Player p: players) {
                // get the card to the player
                p.addCard(this.maindeck.get(0));
                this.maindeck.remove(0);
            }
        }

        // set the board card
        while (!(this.maindeck.get(0).getType() == Card.Type.NUMBER))
        {
            boardCard = this.maindeck.get(0);
            this.maindeck.remove(0);
            this.maindeck.add(boardCard);
        }

        boardCard = this.maindeck.get(0);
        boardColor = boardCard.colorOfCard();
        this.maindeck.remove(0);
    }

    public int playTurn(Card playerChoosenCard) {

        if(checkChoose(playerChoosenCard)){
            players.get(currentIndex).removeCard(playerChoosenCard);

            if (playerChoosenCard.getType() == Card.Type.WILD || playerChoosenCard.getType() == Card.Type.WILD_DRAW_FOUR){
                //ask the player choosen color
                applyChoose(playerChoosenCard,Card.Color.RED);
                chooseColor = true;
            }
            else{
                applyChoose(playerChoosenCard, playerChoosenCard.colorOfCard());
            }
            return setIndex(playerChoosenCard, currentIndex);
        }

        return currentIndex;
        
    }

    public boolean penaltyCase(){
        // the draw case

        if (penaltyCards.size() != 0)
        {
            int n = penaltyCards.size();
            for (; n > 0; n--)
            {
                players.get(currentIndex).addCard(penaltyCards.get(0));
                penaltyCards.remove(0);
            }
            return true;
        }
        return  false;
    }

    public boolean checkChoose(Card playerChosenCard){

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
            for (Card card: players.get(currentIndex).getDeck()) {
                if (card.getType() == Card.Type.WILD){
                    continue;
                }
            //    if (checkChoose(card))
            //        return false;
            }

            return true;
        }

        // check draw2 cards
        // ALLOWS TO COMBOS
        if (boardCard.getType() == Card.Type.DRAW_TWO && penaltyCards.size() == 0) {
            if (playerChosenCard.getType() == Card.Type.DRAW_TWO) {
                return true;
            } else if (playerChosenCard.colorOfCard() == boardColor) {
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



    public boolean checkTheCards(){
        // check the player cards
        if (!checkPlayerCards(players.get(currentIndex)))
        {
            return true;
        }
        return false;
    }

    public  Card giveCardToPlayer()
    {   
        if(maindeck.size()==0){
            generatemainDeck();
        }

        Card c = maindeck.get(0);
        players.get(currentIndex).addCard(maindeck.get(0));
        maindeck.remove(0);
        return c;
    }

    private  boolean checkPlayerCards(Player player)
    {
        for (Card card: player.getDeck())
        {
            if (checkChoose(card))
                return true;
        }
        return false;
    }
   
    public void applyChoose(Card playerChoosenCard, Card.Color choosenColor)
    {   
        changeBoardCard(playerChoosenCard);
        boardColor = choosenColor;
        if (playerChoosenCard.getType().equals(Card.Type.WILD_DRAW_FOUR))
        {
            for (int n = 0; n < 4; n++)
            {
                if(maindeck.size() == 0){
                    generatemainDeck();
                }
                penaltyCards.add(maindeck.get(0));
                maindeck.remove(0);
            }
        }
        else if (playerChoosenCard.getType().equals(Card.Type.DRAW_TWO))
        {
            for (int n = 0; n < 2; n++)
            {
                if(maindeck.size() == 0){
                    generatemainDeck();
                }
                penaltyCards.add(maindeck.get(0));
                maindeck.remove(0);
            }
        } 
    }

    private void changeBoardCard(Card newCard)
    {
        boardCard = newCard;
    }

    public int setIndex(Card playerChoosenCard, int currentPlayerindex)
    {
        // skip card case
        if (playerChoosenCard.getType() == Card.Type.SKIP){
            setIndex = true;
            return (currentPlayerindex + 2)%players.size();
        }

        // reverse card case
        else if (playerChoosenCard.getType() == Card.Type.REVERSE)
        {
            currentPlayerindex =  reversePlayers(currentPlayerindex);
            currentIndex = currentPlayerindex;
            setIndex = true;
            return (currentPlayerindex+1)%players.size();
        }
    
        else{
            setIndex = true;
            return (currentPlayerindex+1)%players.size();
        }
    }

    private  int reversePlayers(int currentPlayerIndex)
    {
        // hold the player for swap
        Player holdPlayer = players.get(currentPlayerIndex);
        Collections.reverse(players);
        return currentPlayerIndex = players.indexOf(holdPlayer);   
    }

    public void switchToNextPlayer(){
        if(nextPlayer){
            if(getCurrentPlayer().getDeck().size() == 1){
                if(!unoClicked){
                    if(getMaindeck().size() == 0){
                        generatemainDeck();
                    }
                    getCurrentPlayer().addCard(maindeck.get(0));
                    maindeck.remove(0);
                }
                else{
                    unoClicked = false;
                }
            }
            playerChoosenCard = null;
            setCurrentIndex(nextIndex);
            nextIndex = -1;
            nextPlayer = false;
            canPlay = true;
        }
    }

    
}
