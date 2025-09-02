package Logic;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainFile extends Game {

    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Stage stage;
    boolean simulation = true; //CHANGE THE PARAMATER TO FALSE IF YOU WANT TO PLAY GAME

    @Override
    public void create () {
        batch = new SpriteBatch();
        stage= new Stage(new ScreenViewport());
        shapeRenderer = new ShapeRenderer();
        setScreen(new UNO(this));
        
    }

    @Override
    public void dispose () {
        batch.dispose();
        stage.dispose();
        shapeRenderer.dispose();
    }
}