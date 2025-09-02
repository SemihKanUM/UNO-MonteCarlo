package Logic;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Collections;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import Logic.Board;
import Logic.Card;
import Logic.Player;
import Logic.Bot.Bot;

public class Launch extends ScreenAdapter{

    private SpriteBatch batch;
    private Stage gameStage;
    private Stage[] playerStages; // Separate stages for each player
    private ImageButton[] playerCardButtons; // Image buttons for player's cards
    private Image[] avatars;
    private float avatarWidth = 150; // Adjust this value based on your preference
    private float avatarHeight = 150; // Adjust this value based on your preference
    private float avatarSpacingX = 250; // Adjust this value based on your preference
    private int roundsPlayed = 0;

    Texture[] avatarTextures;
    MainFile game; 
    BitmapFont font;
    OrthographicCamera camera;
    String text = "";
    Card playerChoosenCard;

    Card boardCard;
    int nextIndex;
    ArrayList<Player> playerList;
    boolean canPlay = true;
    boolean nextPlayer = false;
    Card.Color color;
    boolean drawCard = false;
    boolean unoClicked = false;

    private Board board = Settings_page.getBoard();

    public Launch(MainFile game){
        this.game = game;
    }

    @Override
    public void render(float delta) {
        
        // Clear the screen
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Set up input handling for the game stage
        Gdx.input.setInputProcessor(gameStage);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

       // Draw the board card
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        boardCard = board.getBoardCard();
        boardCard.setPosition(screenWidth / 2 - 400, screenHeight / 2 - 300);
        gameStage.addActor(boardCard.imageButton);

        avatars[board.getCurrentIndex()].setSize(avatarWidth+40, avatarHeight+40);

        endGame();
        //Draw the deck of current player
        drawDeck();
        
        if(board.getCurrentPlayer() instanceof Bot){ //Management of Logic for Bots
            
            Bot bot = (Bot) board.getCurrentPlayer();
            bot.setState(board.getCurrentPlayer().getDeck(), board.boardCard, board.getBoardColor(),board);
            playerChoosenCard = bot.selectCard();
            Card.Color botColor = bot.selectColor();
            
            System.out.println("------------");

            //System.out.println(board.getCurrentPlayer().getName() + " " + board.getCurrentPlayer().getDeck().toString() + " Choosen Card: " + playerChoosenCard + " Board Card: " + board.boardCard);
            
            if(board.getCurrentPlayer().getDeck().size()==0){
                System.out.println(board.getCurrentPlayer().getName() + " " + "Winner!!");
            }

            if(board.chooseColor){
                board.setBoardColor(botColor);
                color = null;
                board.chooseColor = false;
            }
            if(canPlay && !board.chooseColor){
                if(board.penaltyCase()){
                    text = "penalty Cards were taken Move to the Next Player";
                    nextIndex = (board.getCurrentIndex()+1)%playerList.size();
                    canPlay = false;
                    nextPlayer = true;
                }
                else if(board.checkTheCards()){
                    text = "Take a Card From Deck";
                    nextIndex = (board.getCurrentIndex()+1)%playerList.size();
                    Card c = board.giveCardToPlayer();
                    if(board.checkChoose(c)){
                        nextIndex = board.playTurn(c);
                        drawCard = false;
                        canPlay = false;
                        nextPlayer = true;
                        text = "Card was drawn and played";
                        if(board.chooseColor){
                            board.setBoardColor(botColor);
                        }
                    }
                    else{
                        nextIndex = (board.getCurrentIndex()+1)%playerList.size();
                        drawCard = false;
                        canPlay = false;
                        nextPlayer = true;
                        text = "Card was drawn";
                    }
                    
                }
                else if(playerChoosenCard != null){
                    nextIndex = board.playTurn(playerChoosenCard);
                    playerChoosenCard.clickedCard = false;
                    if(board.setIndex){
                        canPlay = false;
                        nextPlayer = true;
                        board.setIndex = false;
                        if(board.chooseColor){
                            board.setBoardColor(botColor);
                        }
                    } 
                }
            }

            if(board.getCurrentPlayer().getDeck().size() == 1){
                unoClicked = true;
            }
        }
        else{ // Management of Logic for human players
            chooseCard();
            if(board.chooseColor){
                text = "Choose Color";
                if((color != null)){
                    board.setBoardColor(color);
                    color = null;
                    board.chooseColor = false;
                }
            }  
            if(canPlay && !board.chooseColor){
                if(board.penaltyCase()){
                    text = "penalty Cards were taken Move to the Next Player";
                    nextIndex = (board.getCurrentIndex()+1)%playerList.size();
                    canPlay = false;
                }
                else if(board.checkTheCards()){
                    text = "Take a Card From Deck";
                    nextIndex = (board.getCurrentIndex()+1)%playerList.size();
                    if(drawCard){
                        Card c = board.giveCardToPlayer();
                        if(board.checkChoose(c)){
                            nextIndex = board.playTurn(c);
                            drawCard = false;
                            canPlay = false;
                            text = "Card was drawn and played";
                            if(board.chooseColor){
                                board.setBoardColor(askColor());
                            }
                        }
                        else{
                            nextIndex = (board.getCurrentIndex()+1)%playerList.size();
                            drawCard = false;
                            canPlay = false;
                            text = "Card was drawn";
                        }
                    }
                }
                else if(playerChoosenCard != null){
                    nextIndex = board.playTurn(playerChoosenCard);
                    playerChoosenCard.clickedCard = false;
                    if(board.setIndex){
                        canPlay = false;
                        board.setIndex = false;
                        if(board.chooseColor){
                            board.setBoardColor(askColor());
                        }
                    } 
                }
            }
        }
        
        switchToNextPlayer();

        // Update and draw the game stage
        gameStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        gameStage.draw();

        batch.begin();
        // Increase the font size by setting the scale
        font.getData().setScale(5f); // Adjust the scale factor as needed
		font.draw(batch, "Current Player = " + board.getCurrentPlayer().getNumber(), 10, Gdx.graphics.getHeight()-20);
		font.draw(batch, "Message = " + text, 10, Gdx.graphics.getHeight()-170);
		font.draw(batch, "Board Color =  " + board.getBoardColor(), 10, Gdx.graphics.getHeight()-100);
        font.draw(batch, "UNO = " + unoClicked , 10, Gdx.graphics.getHeight()-230);
        batch.end();
    }

    public void switchToNextPlayer(){
        if(nextPlayer){
            if(board.getPlayers().get(board.getCurrentIndex()).getDeck().size() == 1){
                if(!unoClicked){
                    board.players.get(board.getCurrentIndex()).addCard(board.maindeck.get(0));
                    board.maindeck.remove(0);
                }
                else{
                    unoClicked = false;
                }
            }
            avatars[board.getCurrentIndex()].setSize(avatarWidth, avatarHeight);
            playerChoosenCard = null;
            resetCards();
            board.setCurrentIndex(nextIndex);
            nextIndex = -1;
            nextPlayer = false;
            canPlay = true;
            text = "";
        }  
    }

    public void endGame(){
        if(board.getCurrentPlayer().getDeck().size() == 0){
            for (Player player : playerList) {
                if (player.getDeck().size() == 0) {
                    text = "Player " + player.getNumber() + " has won the game!";
                    canPlay = false;
                    game.setScreen(new scoreboard(playerList));
                    return;
                }
            }
        }
    }

    public Card.Color askColor() {
        TextButton[] buttons = new TextButton[4];

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont();
    
        final TextButton redButton = new TextButton("Red", buttonStyle);
        redButton.setPosition(Gdx.graphics.getWidth() / 2 - 500, Gdx.graphics.getHeight() / 2 - 300);
        buttons[0] = redButton;

        final TextButton greenButton = new TextButton("Green", buttonStyle);
        greenButton.setPosition(Gdx.graphics.getWidth() / 2 - 500, Gdx.graphics.getHeight() / 2 - 350);
        buttons[1] = redButton;

        final TextButton blueButton = new TextButton("Blue", buttonStyle);
        blueButton.setPosition(Gdx.graphics.getWidth() / 2 - 500, Gdx.graphics.getHeight() / 2 - 400);
        buttons[2] = redButton;

        final TextButton yellowButton = new TextButton("Yellow", buttonStyle);
        yellowButton.setPosition(Gdx.graphics.getWidth() / 2 - 500, Gdx.graphics.getHeight() / 2 - 450);
        buttons[3] = redButton;

        redButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the red color selection
                color = Card.Color.RED;
                redButton.remove();
                greenButton.remove();
                blueButton.remove();
                yellowButton.remove();
            }
        });
    
        greenButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the green color selection
                color = Card.Color.GREEN;
                redButton.remove();
                greenButton.remove();
                blueButton.remove();
                yellowButton.remove();
            }
        });
       
        blueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the blue color selection
                color = Card.Color.BLUE;
                redButton.remove();
                greenButton.remove();
                blueButton.remove();
                yellowButton.remove();
            }
        });    
        yellowButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Handle the yellow color selection
                color = Card.Color.YELLOW;
                redButton.remove();
                greenButton.remove();
                blueButton.remove();
                yellowButton.remove();
            }
        });
    
        // Add buttons to the game stage
        gameStage.addActor(redButton);
        gameStage.addActor(greenButton);
        gameStage.addActor(blueButton);
        gameStage.addActor(yellowButton);
    
        return color;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        // Set the desired screen width and height
        int screenWidth = Gdx.graphics.getWidth(); // Change this to your desired width
        int screenHeight = Gdx.graphics.getHeight(); // Change this to your desired height

        // Use a FitViewport to maintain the aspect ratio
        Viewport viewport = new FitViewport(screenWidth, screenHeight);
        gameStage = new Stage(viewport);

        batch = new SpriteBatch();
		font = new BitmapFont();

        playerStages = new Stage[10]; // Four players
        playerCardButtons = new ImageButton[10]; // 7 image buttons for player's cards

        // Load the background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("Background2.jpg"));
        // Create the background image
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(screenWidth, screenHeight);
        // Add the background image as the first actor to the game stage
        gameStage.addActor(backgroundImage);

        // Create an array of avatar images
        avatarTextures = new Texture[]{
            new Texture(Gdx.files.internal("Avatar1.png")),
            new Texture(Gdx.files.internal("Avatar2.png")),
            new Texture(Gdx.files.internal("Avatar3.png")),
            new Texture(Gdx.files.internal("Avatar4.png")),
            new Texture(Gdx.files.internal("Avatar5.png")),
            // Add more avatar textures as needed
        };

        // Create an array of Image objects for avatars
        avatars = new Image[board.players.size()];

        for (int i = 0; i < board.players.size(); i++) {
            // Randomly choose an avatar image
            Texture avatarTexture = avatarTextures[MathUtils.random(avatarTextures.length - 1)];
            avatars[i] = new Image(avatarTexture);
            avatars[i].setSize(avatarWidth, avatarHeight);

            // Position the avatars based on the number of players
            float avatarX = calculateAvatarX(i, board.players.size());
            float avatarY = calculateAvatarY(i, board.players.size());
            avatars[i].setPosition(avatarX, avatarY);

            // Add avatar images to the game stage
            gameStage.addActor(avatars[i]);
        }

        playerList = board.getPlayers();
        boardCard = board.getBoardCard();
        
        Texture buttonTexture = new Texture(Gdx.files.internal("nextButton.png"));
        TextureRegion textureREgion = new TextureRegion(buttonTexture,0,0,buttonTexture.getWidth(),buttonTexture.getHeight());
        TextureRegionDrawable textureRegionDrawable = new TextureRegionDrawable(textureREgion);
        final ImageButton nextButton = new ImageButton(textureRegionDrawable);
        nextButton.setPosition(300, 100);

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(!canPlay){
                    nextPlayer = true;
                    nextButton.addAction(
                        Actions.sequence(
                        Actions.moveBy(0, 20, 0.2f, Interpolation.swingOut),
                        Actions.moveBy(0, -20, 0.2f, Interpolation.swingIn)
                    )
                );
                }
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Mouse-over effect: Jump animation
                
            }
        });

        buttonTexture = new Texture(Gdx.files.internal("drawcard.png"));
        textureREgion = new TextureRegion(buttonTexture,0,0,buttonTexture.getWidth(),buttonTexture.getHeight());
        textureRegionDrawable = new TextureRegionDrawable(textureREgion);
        final ImageButton drawButton = new ImageButton(textureRegionDrawable);
        drawButton.setPosition((Gdx.graphics.getWidth()) / 2 - 600 + (14 * drawButton.getMinWidth()),130);
        drawButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                drawCard = true;
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Mouse-over effect: Jump animation
                drawButton.addAction(
                    Actions.sequence(
                        Actions.moveBy(0, 20, 0.2f, Interpolation.swingOut),
                        Actions.moveBy(0, -20, 0.2f, Interpolation.swingIn)
                    )
                );
            }
        });

        buttonTexture = new Texture(Gdx.files.internal("unobutton.png"));
        textureREgion = new TextureRegion(buttonTexture,0,0,buttonTexture.getWidth(),buttonTexture.getHeight());
        textureRegionDrawable = new TextureRegionDrawable(textureREgion);
        final ImageButton unoButton = new ImageButton(textureRegionDrawable);
        unoButton.setPosition((Gdx.graphics.getWidth()) / 2 - 600 + (14 * drawButton.getMinWidth()),300);
        
        unoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                unoClicked = true;
                unoButton.addAction(
                    Actions.sequence(
                        Actions.moveBy(0, 20, 0.2f, Interpolation.swingOut),
                        Actions.moveBy(0, -20, 0.2f, Interpolation.swingIn)
                    )
                );
            }
        });

       gameStage.addActor(drawButton);
       gameStage.addActor(nextButton);
       gameStage.addActor(unoButton);
    }


    // Calculate the X-coordinate for avatars based on the player index and total number of players
    private float calculateAvatarX(int playerIndex, int totalPlayers) {
        // Adjust this calculation based on your preference
        return (Gdx.graphics.getWidth() - totalPlayers * avatarWidth) / 2 + playerIndex * avatarSpacingX;
    }

    // Calculate the Y-coordinate for avatars based on the player index and total number of players
    private float calculateAvatarY(int playerIndex, int totalPlayers) {
        // Adjust this calculation based on your preference
        return (Gdx.graphics.getHeight() - avatarHeight) - 500;
    }

    public void drawDeck(){
        ArrayList<Card> deck = playerList.get(board.getCurrentIndex()).getDeck();
        int order = 0;

        float cardSpacingX = 60f;  // Adjust this value based on your preference
        float cardY = 70f;        // Adjust the Y-coordinate based on your preference
        float deckOffsetX = 600f;

        for (int i = 0; i < deck.size(); i++) {
            float cardX;

            if (order > 9) {
                cardX = (Gdx.graphics.getWidth() - deck.size() * cardSpacingX) / 2 + i * cardSpacingX - deckOffsetX;
            } else {
                cardX = (Gdx.graphics.getWidth() - (deck.size() - 8) * cardSpacingX) / 2 + i * cardSpacingX - deckOffsetX;
            }

            deck.get(i).setPosition(cardX, cardY);
            gameStage.addActor(deck.get(i).imageButton);
            order++;
        }
    }

    public void chooseCard(){
        ArrayList<Card> deck = playerList.get(board.getCurrentIndex()).getDeck();
        for (int i = 0; i < deck.size(); i++) {
            if(deck.get(i).clickedCard){
                playerChoosenCard = deck.get(i);
            }
        }
    }

    public void resetCards(){
        ArrayList<Card> deck = playerList.get(board.getCurrentIndex()).getDeck();
        for (int i = 0; i < deck.size(); i++) {
            deck.get(i).clickedCard = false;
            gameStage.getRoot().removeActor(deck.get(i).imageButton);
        }
    }

    public void setPlayerChoosenCard(Card card){
        playerChoosenCard = card;
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameStage.dispose();
        font.dispose();
       // Dispose of the skin when it's no longer needed
        for (int i = 0; i < avatarTextures.length; i++) {
            avatarTextures[i].dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        super.resize(width, height);
    }
}