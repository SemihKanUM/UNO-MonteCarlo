// package Logic.Bot;

// import Logic.Board;
// import Logic.Card;
// import Logic.Player;
// import com.badlogic.gdx.scenes.scene2d.ui.Tree;
// import jdk.swing.interop.SwingInterOpUtils;

// import javax.swing.plaf.synth.SynthOptionPaneUI;
// import java.util.ArrayList;

// public class main {

//     public static void main(String[] args) {

//         System.out.println("Hello world!");

//         // Create a game
//         Card boardCard = new Card(null, Card.Type.WILD_DRAW_FOUR, Card.Color.WILD, -1);

//         // Giving cards to players
//         ArrayList<Card> P1Deck = new ArrayList<>();
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.YELLOW, 6));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.YELLOW, 5));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.YELLOW, 2));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.YELLOW, 2));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.RED, 1));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.RED, 9));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.GREEN, 8));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.GREEN, 0));
//         P1Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.BLUE, 1));
//         P1Deck.add(new Card(null, Card.Type.DRAW_TWO, Card.Color.GREEN, -1));
//         P1Deck.add(new Card(null, Card.Type.DRAW_TWO, Card.Color.BLUE, -1));

//         ArrayList<Card> P2Deck = new ArrayList<>();
//         P2Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.BLUE, 5));
//         P2Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.BLUE, 6));
//         P2Deck.add(new Card(null, Card.Type.NUMBER, Card.Color.BLUE, 2));

//         Player P1 = new Player(1);
//         P1.setDeck(P1Deck);
//         Player P2 = new Player(2);
//         P2.setDeck(P2Deck);

//         ArrayList<Player> players = new ArrayList<>();
//         players.add(P1);
//         players.add(P2);

//         // The zero is for overloading
//         Board board = new Board(players, true, 0);

//         // Generate and get the main deck
//         ArrayList<Card> maindeck = board.getMaindeck();

//         maindeck = removeCard(maindeck, P1Deck);
//         maindeck = removeCard(maindeck, P2Deck);
//         maindeck = removeCard(maindeck, boardCard);

//         // set and remove the board card
//         maindeck.remove(boardCard);

//         // Update maindeck
//         board.setMaindeck(maindeck);

//         // From hereon we do MCTS
//         MCTSBot MCTS = new MCTSBot(P1Deck, boardCard, P2Deck.size());
//         System.out.println(MCTS.getBestCard());

//         System.out.println(MCTSBot.totalVisitRoot);

//         System.out.println();
//         System.out.println("******** END OF PROGRAM *********");
//     }


//     private static ArrayList<Card> removeCard(ArrayList<Card> maindeck, ArrayList<Card> deck) {
//         for (Card card : deck) {
//             for (int i = 0; i < maindeck.size(); i++) {  // Note the change from <= to <
//                 Card maincard = maindeck.get(i);

//                 if (maincard.getType() == card.getType() &&
//                         maincard.getNumber() == card.getNumber() &&
//                         maincard.getColor() == card.getColor()) {

//                     maindeck.remove(i);
//                     break;
//                 }
//             }
//         }
//         return maindeck;
//     }

//     private static ArrayList<Card> removeCard(ArrayList<Card> maindeck, Card card) {
//         for (int i = 0; i < maindeck.size(); i++) {  // Note the change from <= to <
//             Card maincard = maindeck.get(i);

//             if (maincard.getType() == card.getType() &&
//                     maincard.getNumber() == card.getNumber() &&
//                     maincard.getColor() == card.getColor()) {

//                 maindeck.remove(i);
//                 break;
//             }
//         }
//         return maindeck;
//     }
// }