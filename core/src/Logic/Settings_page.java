package Logic;


import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.ScreenUtils;


public class Settings_page extends ScreenAdapter {
    // Set the desired screen width and height
    private int screenWidth = 1200; // Change this to your desired width
    private final int screenHeight = 900; // Change this to your desired height
    private SpriteBatch batch;
    private Stage gameStage;
    Texture cg;
    ImageButton continueButton;
    private boolean shouldContinue = false;
    MainFile game;
    //REAL PLAYERS SECTION
    Texture minus;
    ImageButton minusButton;
    Texture plus;
    ImageButton plusButton;
    Texture R_number;
    ImageButton R_number_button;
    Texture updated_R_number_texture;
    private String current_real_p = "sett_2.jpg";
    public int number_of_real_players = 2;

    //AI PLAYERS SECTION
    Texture minusAI;
    ImageButton minusButtonAI;
    Texture plusAI;
    ImageButton plusButtonAI;
    Texture AI_number;
    ImageButton AI_number_button;
    Texture updated_AI_number_texture;
    private String current_AI_p = "sett_0 copy.jpg";
    public int number_of_AI_playersMC = 0;

    public int number_of_R_player = 0;

    Texture minusAI2;
    ImageButton minusButtonAI2;
    Texture updated_AI_number_texture2;
    private String current_AI_p2 = "sett_0 copy 2.jpg";
    Texture plusAI2;
    ImageButton plusButtonAI2;
    Texture AI_number2;
    ImageButton AI_number_button2;

    static  Board board;

    public int final_number_of_players;

    public Settings_page(MainFile game) {
        this.game = game;
    }
    @Override
    public void show() {
        batch = new SpriteBatch();

        // setting stage
        Viewport viewport = new FitViewport(screenWidth, screenHeight);
        gameStage = new Stage(viewport);

        // setting background to a picture
        Texture backgroundTexture = new Texture(Gdx.files.internal("final_background.jpg"));

        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(screenWidth, screenHeight);

        //creating "continue" button
        cg = new Texture("sett_continue.png");
        TextureRegion cgR = new TextureRegion(cg);
        TextureRegionDrawable cgRD = new TextureRegionDrawable(cgR);
        continueButton = new ImageButton(cgRD);
        continueButton.setSize(250, 100);
        continueButton.setPosition((screenWidth/2f)-125, 30);

        // adding action listener for button "continue"
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(number_of_AI_playersMC+ number_of_R_player + number_of_real_players > 0 && number_of_AI_playersMC+ number_of_R_player +number_of_real_players < 11) {
                    System.out.print("you may continue");
                    shouldContinue = true;
                }else{
                    System.out.print("you may NOT continue");
                    shouldContinue = false;
                }
            }
        });

        //REAL PLAYERS SECTION
        //creating Real number button
        R_number = new Texture(current_real_p);
        TextureRegion R_number_R = new TextureRegion(R_number);
        TextureRegionDrawable R_number_RD = new TextureRegionDrawable(R_number_R);
        R_number_button = new ImageButton(R_number_RD);
        R_number_button.setSize(70, 70);
        R_number_button.setPosition((screenWidth/12f)+70, 550);

        //creating "minus" button
        minus = new Texture("sett_minus.jpg");
        TextureRegion minusR = new TextureRegion(minus);
        TextureRegionDrawable minusRD = new TextureRegionDrawable(minusR);
        minusButton = new ImageButton(minusRD);
        minusButton.setSize(70, 70);
        minusButton.setPosition((screenWidth/12f), 550);

        // adding action listener for button "minus"
        minusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch(number_of_real_players) {
                    case 1:
                        updated_R_number_texture = new Texture("sett_0.jpg");
                        break;
                    case 2:
                        updated_R_number_texture = new Texture("sett_1.jpg");
                        break;
                    case 3:
                        updated_R_number_texture = new Texture("sett_2.jpg");
                        break;
                    case 4:
                        updated_R_number_texture = new Texture("sett_3.jpg");
                        break;
                    case 5:
                        updated_R_number_texture = new Texture("sett_4.jpg");
                        break;
                    case 6:
                        updated_R_number_texture = new Texture("sett_5.jpg");
                        break;
                    case 7:
                        updated_R_number_texture = new Texture("sett_6.jpg");
                        break;
                    case 8:
                        updated_R_number_texture = new Texture("sett_7.jpg");
                        break;
                    case 9:
                        updated_R_number_texture = new Texture("sett_8.jpg");
                        break;
                    case 10:
                        updated_R_number_texture = new Texture("sett_9.jpg");
                        break;
                }
                number_of_real_players--;
            }
        });

        //creating "plus" button
        plus = new Texture("sett_plus.jpg");
        TextureRegion plusR = new TextureRegion(plus);
        TextureRegionDrawable plusRD = new TextureRegionDrawable(plusR);
        plusButton = new ImageButton(plusRD);
        plusButton.setSize(70, 70);
        plusButton.setPosition((screenWidth/12f)+140, 550);

        // adding action listener for button "plus"
        plusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch(number_of_real_players) {
                    case 0:
                        updated_R_number_texture = new Texture("sett_1.jpg");
                        break;
                    case 1:
                        updated_R_number_texture = new Texture("sett_2.jpg");
                        break;
                    case 2:
                        updated_R_number_texture = new Texture("sett_3.jpg");
                        break;
                    case 3:
                        updated_R_number_texture = new Texture("sett_4.jpg");
                        break;
                    case 4:
                        updated_R_number_texture = new Texture("sett_5.jpg");
                        break;
                    case 5:
                        updated_R_number_texture = new Texture("sett_6.jpg");
                        break;
                    case 6:
                        updated_R_number_texture = new Texture("sett_7.jpg");
                        break;
                    case 7:
                        updated_R_number_texture = new Texture("sett_8.jpg");
                        break;
                    case 8:
                        updated_R_number_texture = new Texture("sett_9.jpg");
                        break;
                    case 9:
                        updated_R_number_texture = new Texture("sett_10.jpg");
                        break;
                }
                number_of_real_players++;
            }
        });

        //AI PLAYERS SECTION
        //creating Real number button
        AI_number = new Texture(current_AI_p);
        TextureRegion AI_number_R = new TextureRegion(AI_number);
        TextureRegionDrawable AI_number_RD = new TextureRegionDrawable(AI_number_R);
        AI_number_button = new ImageButton(AI_number_RD);
        AI_number_button.setSize(70, 70);
        AI_number_button.setPosition((screenWidth/12f)+70, 280);

        //creating "minus" button
        minusAI = new Texture("sett_minus copy.jpg");
        TextureRegion minusAI_R = new TextureRegion(minusAI);
        TextureRegionDrawable minusAI_RD = new TextureRegionDrawable(minusAI_R);
        minusButtonAI = new ImageButton(minusAI_RD);
        minusButtonAI.setSize(70, 70);
        minusButtonAI.setPosition((screenWidth/12f), 280);

        // adding action listener for button "minus"
        minusButtonAI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch(number_of_AI_playersMC) {
                    case 1:
                        updated_AI_number_texture = new Texture("sett_0 copy.jpg");
                        break;
                    case 2:
                        updated_AI_number_texture = new Texture("sett_1 copy.jpg");
                        break;
                    case 3:
                        updated_AI_number_texture = new Texture("sett_2 copy.jpg");
                        break;
                    case 4:
                        updated_AI_number_texture = new Texture("sett_3 copy.jpg");
                        break;
                    case 5:
                        updated_AI_number_texture = new Texture("sett_4 copy.jpg");
                        break;
                    case 6:
                        updated_AI_number_texture = new Texture("sett_5 copy.jpg");
                        break;
                    case 7:
                        updated_AI_number_texture = new Texture("sett_6 copy.jpg");
                        break;
                    case 8:
                        updated_AI_number_texture = new Texture("sett_7 copy.jpg");
                        break;
                    case 9:
                        updated_AI_number_texture = new Texture("sett_8 copy.jpg");
                        break;
                    case 10:
                        updated_AI_number_texture = new Texture("sett_9 copy.jpg");
                        break;
                }
                number_of_AI_playersMC--;
            }
        });

        //creating "plus" button
        plusAI = new Texture("sett_plus copy.jpg");
        TextureRegion plusAI_R = new TextureRegion(plus);
        TextureRegionDrawable plusAI_RD = new TextureRegionDrawable(plusAI_R);
        plusButtonAI = new ImageButton(plusAI_RD);
        plusButtonAI.setSize(70, 70);
        plusButtonAI.setPosition((screenWidth/12f)+140, 280);

        // adding action listener for button "plus"
        plusButtonAI.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch(number_of_AI_playersMC) {
                    case 0:
                        updated_AI_number_texture = new Texture("sett_1 copy.jpg");
                        break;
                    case 1:
                        updated_AI_number_texture = new Texture("sett_2 copy.jpg");
                        break;
                    case 2:
                        updated_AI_number_texture = new Texture("sett_3 copy.jpg");
                        break;
                    case 3:
                        updated_AI_number_texture = new Texture("sett_4 copy.jpg");
                        break;
                    case 4:
                        updated_AI_number_texture = new Texture("sett_5 copy.jpg");
                        break;
                    case 5:
                        updated_AI_number_texture = new Texture("sett_6 copy.jpg");
                        break;
                    case 6:
                        updated_AI_number_texture = new Texture("sett_7 copy.jpg");
                        break;
                    case 7:
                        updated_AI_number_texture = new Texture("sett_8 copy.jpg");
                        break;
                    case 8:
                        updated_AI_number_texture = new Texture("sett_9 copy.jpg");
                        break;
                    case 9:
                        updated_AI_number_texture = new Texture("sett_10 copy.jpg");
                        break;
                }
                number_of_AI_playersMC++;
            }
        });

        //SECOND AI
        //adding minus button
        minusAI2 = new Texture("sett_minus copy 2.jpg");
        TextureRegion minusR_AI2 = new TextureRegion(minusAI2);
        TextureRegionDrawable minusRD_AI2 = new TextureRegionDrawable(minusR_AI2);
        minusButtonAI2 = new ImageButton(minusRD_AI2);
        minusButtonAI2.setSize(70, 70);
        minusButtonAI2.setPosition((screenWidth/12f), 70);

        //adding plus button
        plusAI2 = new Texture("sett_plus copy 2.jpg");
        TextureRegion plusAI_R2 = new TextureRegion(plusAI2);
        TextureRegionDrawable plusAI_RD2 = new TextureRegionDrawable(plusAI_R2);
        plusButtonAI2 = new ImageButton(plusAI_RD2);
        plusButtonAI2.setSize(70, 70);
        plusButtonAI2.setPosition((screenWidth/12f)+140, 70);


        //creating number button
        AI_number2 = new Texture(current_AI_p2);
        TextureRegion AI_number_R2 = new TextureRegion(AI_number2);
        TextureRegionDrawable AI_number_RD2 = new TextureRegionDrawable(AI_number_R2);
        AI_number_button2 = new ImageButton(AI_number_RD2);
        AI_number_button2.setSize(70, 70);
        AI_number_button2.setPosition((screenWidth/12f)+70, 70);

        // adding action listener for button "plus"
        plusButtonAI2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch(number_of_R_player) {
                    case 0:
                        updated_AI_number_texture2 = new Texture("sett_1 copy 2.jpg");
                        break;
                    case 1:
                        updated_AI_number_texture2 = new Texture("sett_2 copy 2.jpg");
                        break;
                    case 2:
                        updated_AI_number_texture2 = new Texture("sett_3 copy 2.jpg");
                        break;
                    case 3:
                        updated_AI_number_texture2 = new Texture("sett_4 copy 2.jpg");
                        break;
                    case 4:
                        updated_AI_number_texture2 = new Texture("sett_5 copy 2.jpg");
                        break;
                    case 5:
                        updated_AI_number_texture2 = new Texture("sett_6 copy 2.jpg");
                        break;
                    case 6:
                        updated_AI_number_texture2 = new Texture("sett_7 copy 2.jpg");
                        break;
                    case 7:
                        updated_AI_number_texture2 = new Texture("sett_8 copy 2.jpg");
                        break;
                    case 8:
                        updated_AI_number_texture2 = new Texture("sett_9 copy 2.jpg");
                        break;
                    case 9:
                        updated_AI_number_texture2 = new Texture("sett_10 copy 2.jpg");
                        break;
                }
                number_of_R_player++;
            }
        });

        // adding action listener for button "minus"
        minusButtonAI2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                switch(number_of_R_player) {
                    case 1:
                        updated_AI_number_texture2 = new Texture("sett_0 copy 2.jpg");
                        break;
                    case 2:
                        updated_AI_number_texture2 = new Texture("sett_1 copy 2.jpg");
                        break;
                    case 3:
                        updated_AI_number_texture2 = new Texture("sett_2 copy 2.jpg");
                        break;
                    case 4:
                        updated_AI_number_texture2 = new Texture("sett_3 copy 2.jpg");
                        break;
                    case 5:
                        updated_AI_number_texture2 = new Texture("sett_4 copy 2.jpg");
                        break;
                    case 6:
                        updated_AI_number_texture2 = new Texture("sett_5 copy 2.jpg");
                        break;
                    case 7:
                        updated_AI_number_texture2 = new Texture("sett_6 copy 2.jpg");
                        break;
                    case 8:
                        updated_AI_number_texture2 = new Texture("sett_7 copy 2.jpg");
                        break;
                    case 9:
                        updated_AI_number_texture2 = new Texture("sett_8 copy 2.jpg");
                        break;
                    case 10:
                        updated_AI_number_texture2 = new Texture("sett_9 copy 2.jpg");
                        break;
                }
                number_of_R_player--;
            }
        });

        updated_R_number_texture = new Texture(current_real_p);
        updated_AI_number_texture = new Texture(current_AI_p);
        updated_AI_number_texture2 = new Texture(current_AI_p2);

        // adding created things as actors
        gameStage.addActor(backgroundImage);
        gameStage.addActor(continueButton);
        gameStage.addActor(minusButton);
        gameStage.addActor(plusButton);
        gameStage.addActor(R_number_button);
        gameStage.addActor(minusButtonAI);
        gameStage.addActor(plusButtonAI);
        gameStage.addActor(AI_number_button);
        gameStage.addActor(minusButtonAI2);
        gameStage.addActor(plusButtonAI2);
        gameStage.addActor(AI_number_button2);
        Gdx.input.setInputProcessor(gameStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the game stage
        gameStage.act(Gdx.graphics.getDeltaTime());

        // Set the "two" button's image to the updated texture
        R_number_button.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(updated_R_number_texture));
        AI_number_button.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(updated_AI_number_texture));
        AI_number_button2.getStyle().imageUp = new TextureRegionDrawable(new TextureRegion(updated_AI_number_texture2));

        gameStage.draw();

        if (shouldContinue) {
            final_number_of_players = number_of_AI_playersMC + number_of_R_player + number_of_real_players;
            //creating arraylist with automatically generated names using method "names_of_players" in class "create_players"

            create_players array = new create_players();
            array.names_of_players(number_of_real_players,number_of_AI_playersMC,number_of_R_player);
            ArrayList<Integer> all_names;
            all_names = array.all_names_of_players;
            board = new Board(all_names,false);

            game.setScreen(new Switch(game));
            shouldContinue = false; // Reset the flag to prevent continuous transitions
        }

    }

    public int getNumberOfRealPlayers() {
        return number_of_real_players;
    }

    public static Board getBoard(){return board;}
    @Override
    public void hide() {
        gameStage.dispose();
        Gdx.input.setInputProcessor(null);
    }
}

