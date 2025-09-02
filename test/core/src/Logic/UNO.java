 package Logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class UNO extends ScreenAdapter {

    MainFile game;
    Stage stage;
    Texture welcome;
    Texture exit;
    Texture sg;
    ImageButton ExitButton;
    ImageButton StartGame;

    public UNO(MainFile game) {
        this.game = game;
    }

    @Override
    public void show() {
        // Initialize the stage and load textures
        stage = new Stage(new ScreenViewport());
        welcome = new Texture("uno.png"); // Load the welcome image

        // Create and position the UnoP image
        Image UnoP = new Image(welcome);
        UnoP.setPosition(582, 750);

        exit = new Texture("exit.png"); // Load the exit button image
        TextureRegion ExitR = new TextureRegion(exit);
        TextureRegionDrawable ExitRD = new TextureRegionDrawable(ExitR);

        // Create and position the exit button
        ExitButton = new ImageButton(ExitRD);
        ExitButton.setPosition(715, 0);

        sg = new Texture("startgame.png"); // Load the start game button image
        TextureRegion sgR = new TextureRegion(sg);
        TextureRegionDrawable sgRD = new TextureRegionDrawable(sgR);

        // Create and position the start game button
        StartGame = new ImageButton(sgRD);
        StartGame.setPosition(715, 375);

        // Add a click listener to the exit button
        ExitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the application when clicked
            }
        });

        // Add a click listener to the start game button
        StartGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new Settings_page(game)); // Switch to the Settings_page screen
            }
        });

        // Add actors to the stage and set the input processor
        stage.addActor(UnoP);
        stage.addActor(ExitButton);
        stage.addActor(StartGame);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1); // Clear the screen with a white color
        Gdx.gl.glClearColor(1, 0, 0, 1); // Set the clear color to red
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the color buffer

        stage.act(); // Update the stage
        stage.draw(); // Draw the stage
    }

    @Override
    public void hide() {
        stage.dispose(); // Dispose of the stage when the screen is hidden
        Gdx.input.setInputProcessor(null); // Remove the input processor
    }
}
