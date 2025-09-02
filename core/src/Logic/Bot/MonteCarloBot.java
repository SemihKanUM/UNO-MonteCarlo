package Logic.Bot;

import java.lang.reflect.Array;
import java.util.ArrayList;

import Logic.Board;
import Logic.Card;
import Logic.Card.Color;
import Logic.Player;

public class MonteCarloBot extends Player implements Bot{

    ArrayList<Card> deck;
    Card boardCard;
    Card.Color boardColor;
    Board board;
    MCTSBot mcts;
//    public MonteCarloTree monteCarloTree;

    public MonteCarloBot(int number){
        super(number);
    }

    @Override
    public void makeMove() {
        // TODO Auto-generated method stub
    }

    @Override
    public Card selectCard() {
//        TODO Auto-generated method stub
//
       Card card = mcts.getBestCard();

       if(card != null){
           for (Card value : deck) {
               if (value.isSameCard(card)) {
                   return value;
               }
           }
       }

       return null;
    }


    @Override
    public Card.Color selectColor() {
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
        return mostCommonColor;
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

    @Override
    public void setState(ArrayList<Card> deck, Card boardCard, Color boardColor, Board board) {
        // TODO Auto-generated method stub
        this.deck = deck;
        this.boardCard = boardCard;
        this.boardColor = boardColor;
        this.board = board;

        int numberOfOpponentCards = board.getPlayers().get((board.getCurrentIndex()+1)%2).getDeck().size();

        mcts = new MCTSBot(deck,boardCard, boardColor, numberOfOpponentCards,Board.mapCardsToIntegers(board.playedCards));
    }
}