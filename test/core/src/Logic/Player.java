package Logic;

import java.util.ArrayList;

public class Player {
    private final int number;
    private ArrayList<Card> deck;
    private int score;
    public int numberOfWins = 0;

    public Player(int number) {
        this.number = number;
        deck = new ArrayList<Card>();
    }

    public String getName(){
        if(number < 0){
            return "Real Player " + number;
        }
        else if(number%2 == 0){
            return "Monte Carlo Bot " + number;
        }
        else{
            return "Random Bot " + number;
        }
    }

    public boolean checkCards(Card card){
        return !deck.isEmpty();
    }
    public ArrayList<Card> getDeck() {
        return deck;
    }
    public void addCard(Card card) {
        deck.add(card);
    }
    public void removeCard(Card card) {
        deck.remove(card);
    }

    public int calculatePoints() {
        int totalPoints = 0;
        for(Card card : this.getDeck()) {
            totalPoints += card.getPoints();
        }
        return totalPoints;
    }
    public int getDeckLength() {
        return deck.size();
    }

    public int getNumber() {
        return number;
    }

    public int getScore(){
        return score;
    }

    public void setDeck(ArrayList<Card> deck){
        this.deck = deck;
    }

    public void setScore(int add){
        score+= add;
    }
}