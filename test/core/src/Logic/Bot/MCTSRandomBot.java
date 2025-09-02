package Logic.Bot;

import Logic.Board;
import Logic.Card;
import Logic.Player;

import java.util.ArrayList;
import java.util.Random;

public class MCTSRandomBot extends Player implements Bot {

    ArrayList<Card> deck;
    Card boardCard;
    Card.Color boardColor;
    Board board;

    public MCTSRandomBot(int number) { super(number); }

    @Override
    public void makeMove() {
        //TODO Auto-generated method stub
    }


    @Override
    public Card selectCard() {
        Random rand = new Random();

        // Find out what card are playable
        ArrayList<Card> playableDeck = new ArrayList<>();
        for (Card card : deck) {
            if (board.checkChoose(card)) {
                playableDeck.add(card);
            }
        }

        if (playableDeck.size() > 0) {
            // If we have a draw 4. play it
            for (Card card : playableDeck) {
                if (card.getType() == Card.Type.WILD_DRAW_FOUR) {
                    return card;
                }
            }

            // if wwe have draw two, play it
            for (Card card : playableDeck) {
                if (card.getType() == Card.Type.DRAW_TWO) {
                    return card;
                }
            }

            // Play our skip or reverse, which can give us a second turn. If that is the only card qwe can play
            for (Card card : playableDeck) {
                if (card.getType() == Card.Type.SKIP || card.getType() == Card.Type.REVERSE) {
                    return card;
                }
            }

            // Just take a random card from the deck
            return playableDeck.get(rand.nextInt(playableDeck.size()));
        } else {
            return null;
        }

    }

    @Override
    public void setState(ArrayList<Card> deck, Card boardCard, Card.Color boardColor, Board board) {
        this.deck = deck;
        this.boardCard = boardCard.clone();
        this.boardColor = boardCard.getColor();
        this.board = board.clone();

        // TODO Auto-generated method stub
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
}