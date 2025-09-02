package Logic.Bot;

import java.util.ArrayList;

import Logic.Board;
import Logic.Card;
import Logic.Player;
import Logic.Card.Color;

public class RandomBot extends Player implements Bot {

    ArrayList<Card> deck;
    Card boardCard;
    Card.Color boardColor;
    Board board;
    
    public RandomBot(int number) { super(number); }

    @Override
    public void makeMove() {
        //TODO Auto-generated method stub
    }
    

    @Override
    public Card selectCard() {
        // TODO Auto-generated method stub


        for (int i = 0; i < deck.size(); i++) {
            if(board.checkChoose(deck.get(i))){
                return deck.get(i);
            }
        }

        return null;
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
    public Card.Color selectColor(){
        //TO DO Blue as default
        return Card.Color.BLUE;
    }
}
