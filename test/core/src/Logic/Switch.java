package Logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Switch extends ScreenAdapter {
    
    MainFile game;
    int player;
    BitmapFont font;
    int fontSize = 24;

    public Switch(MainFile game) {
        this.game = game;
    }

    @Override
    public void show(){
        // Initialize the font and player
        font = new BitmapFont();
        player = 1 ;
        
        // Set up a key press listener for the SPACE key
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.SPACE) {
                    // Switch to the UnoGame screen and handle player logic
                    game.setScreen(new Launch(game));
                    
                   // game.setScreen(new UnoGame(game));
                    Settings_page  Data= new Settings_page(game);
                    int PlayerNumber = Data.getNumberOfRealPlayers();
                    player = player + 1;
                    if (player == PlayerNumber) {
                        player = 1;
                    }
                }
                return true;
            }
        });
        
        // Initialize the font with custom properties (color and size)
        font = new BitmapFont();
        font.setColor(Color.YELLOW);  // Set font color to YELLOW
        font.getData().setScale(fontSize / 10.0f);  // Set font size
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Begin rendering
        game.batch.begin();
        
        // Draw text on the screen
        font.draw(game.batch, "Switch computer with your fella", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.75f);
        font.draw(game.batch, "Player " + player + " turn", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.5f);
        font.draw(game.batch, "Press space to play.", Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.25f);
        
        // End rendering
        game.batch.end();
    }

    @Override
    public void hide(){
        // Remove the input processor when the screen is hidden
        Gdx.input.setInputProcessor(null);
    }
}   