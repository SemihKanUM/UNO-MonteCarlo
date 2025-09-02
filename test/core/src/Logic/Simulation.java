// package Logic;

// import java.util.*;

// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.ScreenAdapter;
// import com.badlogic.gdx.graphics.g2d.BitmapFont;
// import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// import Logic.Bot.Bot;
// import Logic.Bot.MonteCarloBot;
// import java.io.FileWriter;
// import java.io.IOException;

// public class Simulation{
    
//     public Board board;
//     public boolean nextPlayer = false;
//     public boolean drawCard = false;
//     public boolean unoClicked = false;
//     public Card playerChoosenCard;
//     public Card.Color color;
//     public boolean canPlay = true;
//     public String text;
//     public int nextIndex;
//     public int numberOfSimulations = 0 ;
//     public SpriteBatch batch;
//     public BitmapFont font;
//     public int numberOfTurns;
//     public ArrayList<Integer> simulationResults = new ArrayList<>();
//     public State state;
//     public int simAmount;

//     public Simulation(Board board, int simAmount){
//         this.board = board;
//         this.simAmount = simAmount;
//         state = new State(board, this);
//     }

//     public void setBoard(Board board){
//         this.board = board;
//     }

//     public void setSimAmount(int simAmount){
//         this.simAmount = simAmount;
//     }

//     public void run(){
//             while (true) {
//                 numberOfTurns++;

//                 if (canPlay) {
//                     if (board.penaltyCase()) {
//                         // We can't play, we get cards
//                         text = "penalty Cards were taken Move to the Next Player";
//                         nextIndex = (board.getCurrentIndex() + 1) % board.getPlayers().size();
//                         canPlay = false;
//                         nextPlayer = true;
//                     } else if (board.checkTheCards()) {
//                         text = "Take a Card From Deck";
//                         nextIndex = (board.getCurrentIndex() + 1) % board.getPlayers().size();
//                         Card c = board.giveCardToPlayer();
//                         if (board.checkChoose(c)) {
//                             nextIndex = board.playTurn(c);
//                             drawCard = false;
//                             canPlay = false;
//                             nextPlayer = true;
//                             text = "Card was drawn and played";
//                             if (board.chooseColor) {
//                                 board.setBoardColor(choseBoardColour());
//                             }
//                         } else {
//                             nextIndex = (board.getCurrentIndex() + 1) % board.getPlayers().size();
//                             drawCard = false;
//                             canPlay = false;
//                             nextPlayer = true;
//                             text = "Card was drawn";
//                         }
//                     } else {

//                         try {
//                             Bot bot = (Bot) board.getCurrentPlayer();
//                             bot.setState(board.getCurrentPlayer().getDeck(), board.boardCard, board.getBoardColor(), board);
//                             playerChoosenCard = bot.selectCard();

// //                         if(playerChoosenCard == null){
// //                             for (int i = 0; i < board.getCurrentPlayer().getDeck().size(); i++) {
// //                                 //if(board.getCurrentPlayer().getDeck().get(i))
// //                             }
// //                         }


//                         } catch (Exception e) {
//                             // Handle other exceptions
//                             e.printStackTrace();  // or log the exception
//                         }

//                         if (playerChoosenCard == null) {
//                             nextIndex = (board.getCurrentIndex() + 1) % 2;
//                         } else {
//                             nextIndex = board.playTurn(playerChoosenCard);
//                         }

// //                        playerChoosenCard.clickedCard = false;

//                         if (board.setIndex) {
//                             canPlay = false;
//                             nextPlayer = true;
//                             board.setIndex = false;
//                             if (board.chooseColor) {
//                                 board.setBoardColor(choseBoardColour());
//                             }
//                             board.chooseColor = false;
//                         }
//                     }
//                 }

//                 if (board.getCurrentPlayer().getDeck().size() == 1) {
//                     unoClicked = true;
//                 }

//                 state.setState();
//                 state.saveStateToCSV();


//                 if (endGame() || numberOfTurns == 200) {
//                     //System.out.println(numberOfTurns);
//                     numberOfTurns = 0;
//                     if (numberOfSimulations == simAmount) {
//                         break;
//                     } else {
//                         Collections.shuffle(board.players);
//                         board.initiliaze();
//                     }
//                 } else {
//                     switchToNextPlayer();
//                 }
//             }
//         }

//     public void setPlayerChoosenCard(Card card){
//         playerChoosenCard = card;
//     }

//     public boolean endGame(){
//         if(board.getCurrentPlayer().getDeck().size() == 0){
//             canPlay = false;
//             numberOfSimulations++;
//             return true;
//         }
//         return false;
//     }

//     public void switchToNextPlayer(){
//         if(nextPlayer){
//             if(board.getCurrentPlayer().getDeck().size() == 1){
//                 if(!unoClicked){
//                     if(board.getMaindeck().size() == 0){
//                         board.generatemainDeck();
//                     }
//                     board.getCurrentPlayer().addCard(board.maindeck.get(0));
//                     board.maindeck.remove(0);
//                 }
//                 else{
//                     unoClicked = false;
//                 }
//             }
//             playerChoosenCard = null;
//             board.setCurrentIndex(nextIndex);
//             nextIndex = -1;
//             nextPlayer = false;
//             canPlay = true;
//             text = "";
//         }        
//     }

//     private Card.Color choseBoardColour() {
//         int nrBlue = 0;
//         int nrRed = 0;
//         int nrGreen = 0;
//         int nrYellow = 0;

//         for (Card card : board.getCurrentPlayer().getDeck()) {
//             if (card.getColor() == Card.Color.BLUE) {
//                 nrBlue++;
//             } else if (card.getColor() == Card.Color.RED) {
//                 nrRed++;
//             } else if (card.getColor() == Card.Color.GREEN) {
//                 nrGreen++;
//             } else if (card.getColor() == Card.Color.YELLOW) {
//                 nrYellow++;
//             }
//         }

//         Card.Color mostCommonColor = getMostCommonColor(nrBlue, nrRed, nrGreen, nrYellow);
//         return mostCommonColor;
//     }

//     private Card.Color getMostCommonColor(int nrBlue, int nrRed, int nrGreen, int nrYellow) {
//         int maxCount = Math.max(nrBlue, Math.max(nrRed, Math.max(nrGreen, nrYellow)));

//         if (maxCount == nrBlue) {
//             return Card.Color.BLUE;
//         } else if (maxCount == nrRed) {
//             return Card.Color.RED;
//         } else if (maxCount == nrGreen) {
//             return Card.Color.GREEN;
//         } else {
//             return Card.Color.YELLOW;
//         }
//     }
// }