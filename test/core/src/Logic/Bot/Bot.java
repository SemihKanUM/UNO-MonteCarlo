package Logic.Bot;

import java.util.ArrayList;

import Logic.Board;
import Logic.Card;
import Logic.Card.Color;

public interface Bot {

    // Method for the bot to make a move in the game
    void setState(ArrayList<Card> deck, Card boardCard, Card.Color boardColor, Board board);

    // Method for the bot to make a move in the game
    void makeMove();

    // Method for the bot to make a move in the game
    Card selectCard();

    // Method for the bot to make a move in the game
    Card.Color selectColor();

    // Other methods related to bot behavior can be added as needed
    // For example, methods to initialize the bot, reset its state, etc.
}

