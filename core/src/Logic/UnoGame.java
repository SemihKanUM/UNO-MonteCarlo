//  package Logic;

//  import java.util.ArrayList;

//  import com.badlogic.gdx.ApplicationAdapter;
//  import com.badlogic.gdx.Gdx;
//  import com.badlogic.gdx.InputMultiplexer;
//  import com.badlogic.gdx.ScreenAdapter;
//  import com.badlogic.gdx.graphics.GL20;
//  import com.badlogic.gdx.graphics.Texture;
//  import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//  import com.badlogic.gdx.graphics.g2d.TextureRegion;
//  import com.badlogic.gdx.scenes.scene2d.Actor;
//  import com.badlogic.gdx.scenes.scene2d.InputEvent;
//  import com.badlogic.gdx.scenes.scene2d.Stage;
//  import com.badlogic.gdx.scenes.scene2d.ui.Image;
//  import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//  import com.badlogic.gdx.scenes.scene2d.ui.Skin;
//  import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
//  import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//  import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//  import com.badlogic.gdx.utils.viewport.FitViewport;
//  import com.badlogic.gdx.utils.viewport.Viewport;
//  import Logic.Board;
//  import Logic.Card;
//  import Logic.Player;

//  import com.badlogic.gdx.Gdx;
//  import com.badlogic.gdx.ScreenAdapter;
//  import com.badlogic.gdx.graphics.GL20;
//  import com.badlogic.gdx.graphics.Texture;
//  import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//  import com.badlogic.gdx.graphics.g2d.TextureRegion;
//  import com.badlogic.gdx.scenes.scene2d.Actor;
//  import com.badlogic.gdx.scenes.scene2d.InputEvent;
//  import com.badlogic.gdx.scenes.scene2d.Stage;
//  import com.badlogic.gdx.scenes.scene2d.ui.Image;
//  import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
//  import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//  import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//  import com.badlogic.gdx.utils.viewport.FitViewport;
//  import com.badlogic.gdx.utils.viewport.Viewport;

//  public class UnoGame extends ScreenAdapter {
//      private SpriteBatch batch;
//      private Stage gameStage;

//      private Stage[] playerStages; // Separate stages for each player
//      private ImageButton[] playerCardButtons; // Image buttons for player's cards

//      Texture avatarTopTexture;
//      Texture avatarLeftTexture;
//      Texture avatarRightTexture;
//      MainFile game;

//      private static int currentPlayerIndex;
//      private Board board =Settings_page.getBoard();
//      private Node node = new Node();
//      private boolean shouldContinue=false;
//      private boolean pressedGetCard=false;


//      public UnoGame(MainFile game) {
//          this.game = game;
//      }
//      public static int getPlayerindex(){
//          return currentPlayerIndex;
//      }

//      @Override
//      public void show() {
//          batch = new SpriteBatch();
//          // Set the desired screen width and height
//          int screenWidth = 1920; // Change this to your desired width
//          int screenHeight = 1080; // Change this to your desired height

//          // Use a FitViewport to maintain the aspect ratio
//          Viewport viewport = new FitViewport(screenWidth, screenHeight);
//          gameStage = new Stage(viewport);

//          playerStages = new Stage[10]; // Four players
//          playerCardButtons = new ImageButton[10]; // 7 image buttons for player's cards

//          // Load the background image
//          Texture backgroundTexture = new Texture(Gdx.files.internal("Background.jpg"));

//          // Load avatar images for the top, left, and right sides
//          avatarTopTexture = new Texture(Gdx.files.internal("avatar.png"));
//          avatarLeftTexture = new Texture(Gdx.files.internal("avatar.png"));
//          avatarRightTexture = new Texture(Gdx.files.internal("avatar.png"));
//          // Create the background image
//          Image backgroundImage = new Image(backgroundTexture);
//          backgroundImage.setSize(screenWidth, screenHeight);

//          // Add the background image as the first actor to the game stage
//          gameStage.addActor(backgroundImage);

//          // Create Image objects for avatars
//          Image avatarTop = new Image(avatarTopTexture);
//          Image avatarLeft = new Image(avatarLeftTexture);
//          Image avatarRight = new Image(avatarRightTexture);

//          // Position the avatars on the top, left, and right sides
//          avatarTop.setPosition((screenWidth - avatarTop.getWidth()) / 2, screenHeight - avatarTop.getHeight());
//          avatarLeft.setPosition(0, (screenHeight - avatarLeft.getHeight()) / 2);
//          avatarRight.setPosition(screenWidth - avatarRight.getWidth(), (screenHeight - avatarRight.getHeight()) / 2);

//          // Add avatar images to the game stage
//          gameStage.addActor(avatarTop);
//          gameStage.addActor(avatarLeft);
//          gameStage.addActor(avatarRight);

//          Card boardCard = board.boardCard;
//          ArrayList<Player> playerList = board.getPlayers();
//          currentPlayerIndex = board.getCurrentIndex();
//          Texture cardTexture = new Texture(Gdx.files.internal("1blue.png"));


//          System.out.println(currentPlayerIndex);
//          System.out.println(playerList.get(currentPlayerIndex).getDeck());
//          //int[] stateArray = node.fillState(playerList.get(currentPlayerIndex).getDeck());
//          System.out.println("hh");
//          System.out.println(node.fillState(playerList.get(currentPlayerIndex).getDeck()));



//          if (boardCard.getType().equals(Card.Type.NUMBER)) {
//              cardTexture = new Texture(Gdx.files.internal(boardCard.getNumber() + boardCard.getColor().toString().toLowerCase() + ".png"));
//          } else if (boardCard.getType().equals(Card.Type.SKIP)) {
//              cardTexture = new Texture(Gdx.files.internal(boardCard.getColor().toString().toLowerCase() + boardCard.getType().toString().toLowerCase() + ".png"));
//          } else if (boardCard.getType().equals(Card.Type.WILD)) {
//              cardTexture = new Texture(Gdx.files.internal(boardCard.getType().toString().toLowerCase() + ".png"));
//          } else if (boardCard.getType().equals(Card.Type.WILD_DRAW_FOUR)) {
//              cardTexture = new Texture(Gdx.files.internal(boardCard.getType().toString().toLowerCase() + ".png"));
//          } else if (boardCard.getType().equals(Card.Type.DRAW_TWO)) {
//              cardTexture = new Texture(Gdx.files.internal(boardCard.getColor().toString().toLowerCase() + boardCard.getType().toString().toLowerCase() + ".png"));
//          } else if (boardCard.getType().equals(Card.Type.REVERSE)) {
//              cardTexture = new Texture(Gdx.files.internal(boardCard.getColor().toString().toLowerCase() + boardCard.getType().toString().toLowerCase() + ".png"));
//          }
//          //Display

//          final ImageButton BoardCard = new ImageButton(new TextureRegionDrawable(new TextureRegion(cardTexture, 0, 0, cardTexture.getWidth(), cardTexture.getHeight())));     // Create Uno card image buttons for 1 player's stage (adjust positions accordingly)
//          BoardCard.setPosition(screenWidth/2, screenHeight/2); // Set the desired position (x, y) for the boardcard

//          // Add the playerCardvButton to the game stage
//          gameStage.addActor(BoardCard);

//          int num = 0;

//          for (final Card p : playerList.get(currentPlayerIndex).getDeck()) {
//              if (p.getType().equals(Card.Type.NUMBER)) {
//                  cardTexture = new Texture(Gdx.files.internal(p.getNumber() + p.getColor().toString().toLowerCase() + ".png"));
//              } else if (p.getType().equals(Card.Type.SKIP)) {
//                  cardTexture = new Texture(Gdx.files.internal(p.getColor().toString().toLowerCase() + p.getType().toString().toLowerCase() + ".png"));
//              } else if (p.getType().equals(Card.Type.WILD)) {
//                  cardTexture = new Texture(Gdx.files.internal(p.getType().toString().toLowerCase() + ".png"));
//              } else if (p.getType().equals(Card.Type.WILD_DRAW_FOUR)) {
//                  cardTexture = new Texture(Gdx.files.internal(p.getType().toString().toLowerCase() + ".png"));
//              } else if (p.getType().equals(Card.Type.DRAW_TWO)) {
//                  cardTexture = new Texture(Gdx.files.internal(p.getColor().toString().toLowerCase() + p.getType().toString().toLowerCase() + ".png"));
//              } else if (p.getType().equals(Card.Type.REVERSE)) {
//                  cardTexture = new Texture(Gdx.files.internal(p.getColor().toString().toLowerCase() + p.getType().toString().toLowerCase() + ".png"));
//              }
//              // Example: Create a player's card image button
//              final ImageButton playerCardButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(cardTexture, 0, 0, cardTexture.getWidth(), cardTexture.getHeight())));

//            if(num>9){ playerCardButton.setPosition((screenWidth) / 2 - 300 + (num * 80)/5, 0); }
//            else{playerCardButton.setPosition((screenWidth) / 2 - 300 + num * 80, 80); }
//            // Adjust positions

//              gameStage.addActor(playerCardButton);
//         Texture cardTexturedraw = new Texture(Gdx.files.internal("drawcard.png"));

//             final ImageButton drawCardButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(cardTexturedraw, 0, 0, cardTexture.getWidth(), cardTexture.getHeight())));
//           drawCardButton.setPosition(1400, 100);
//              gameStage.addActor(drawCardButton);
//              drawCardButton.addListener(new ClickListener(){
//                  @Override
//                   public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                       // Change the button's appearance when the mouse enters
//                          drawCardButton.setPosition(1400, 120);

//                    }

//                   @Override
//                   public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                       // Change the button's appearance when the mouse exits
//                         drawCardButton.setPosition(1400, 100);
//                    }
//                   @Override
//                   public void clicked(InputEvent event, float x, float y) {
//                       board.giveCardToPlayer();
//                       pressedGetCard=true;
//                   }

//               });

//              // Add a click listener to the card button
//              final boolean g = true;
//              playerCardButton.addListener(new ClickListener() {

//              public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
//                      // Change the button's appearance when the mouse enters
//                          playerCardButton.setPosition(playerCardButton.getX(), playerCardButton.getY()+20);

//                   }

//                  @Override
//                  public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                      // Change the button's appearance when the mouse exits
//                        playerCardButton.setPosition(playerCardButton.getX(),  playerCardButton.getY()-20);
//                   }
//                  @Override
//                  public void clicked(InputEvent event, float x, float y) {


//                      // Handle card button click for the specific player (playerIndex)
//                      if (board.checkChoose(p)) {

//                          board.getPlayers().get(currentPlayerIndex).removeCard(p);

//                          if (p.getType() == Card.Type.WILD || p.getType() == Card.Type.WILD_DRAW_FOUR)
//                              // ask the player choosen color

//                              //board.applyChoose(p, choseColor());
//                              board.applyChoose(p, Card.Color.RED);

//                          else if(p.getType() == Card.Type.DRAW_TWO){
//                              System.out.println(p.getColor());
//                              board.applyChoose(p, p.colorOfCard());
//                          }
//                              else{
//                                   board.applyChoose(p, p.colorOfCard());
//                                  }

//                          // Dispose of the clicked card button
//                          board.setCurrentIndex(board.setIndex(p, currentPlayerIndex));
//                          playerCardButton.remove();



//                          // Create a new Image using the card's texture
//                          Image discardedCardImage = new Image(playerCardButton.getImage().getDrawable());

//                          // Position the discarded card image in the middle of the screen

//                          float screenWidth = 1920;
//                          float screenHeight = 1080;
//                          discardedCardImage.setPosition((screenWidth) / 2, (screenHeight) / 2);

//                          // Add the discarded card image to the game stage
//                          gameStage.addActor(discardedCardImage);

//                      } else {
//                          System.out.println("You cant choose this card");
//                      }

//                  shouldContinue=true;}
//              });
//              num = num + 1;
//          }

//          // Set up input handling for the game stage
//          Gdx.input.setInputProcessor(gameStage);
//      }


//      public Board getBoard(){
//          return board;
//      }



//      @Override
//      public void render(float delta) {


//          // Clear the screen
//          Gdx.gl.glClearColor(0, 1, 0, 1);
//          Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//          // Update and draw the game stage
//          gameStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
//          gameStage.draw();

//          if (shouldContinue){
//              show();
//              shouldContinue=false;
//          }
//          if(pressedGetCard){
//              if(!board.canPlay())
//          {
//              currentPlayerIndex= (currentPlayerIndex+1)%board.getPlayers().size();
//              show();
//          }
//              pressedGetCard=false;}
//          for(Player x:board.getPlayers()){

//              if (x.getDeck().size()==0){
//                  System.out.println("Player "+getPlayerindex()+" won");
//                  dispose();
//              }
//          }
//          // Handle input processing for all stages
//          Gdx.input.setInputProcessor(gameStage);


//      }

//      @Override
//      public void dispose() {
//          batch.dispose();
//          gameStage.dispose();
//         // Dispose of the skin when it's no longer needed

//          // Dispose of avatar textures
//          avatarTopTexture.dispose();
//          avatarLeftTexture.dispose();
//          avatarRightTexture.dispose();
//      }
//  }
