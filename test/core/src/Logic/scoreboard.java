package Logic;    

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class scoreboard extends ScreenAdapter {
    private int screenWidth = 1200; // Change this to your desired width
    private final int screenHeight = 900; // Change this to your desired height
    private SpriteBatch batch;
    private Stage gameStage;
    private Table scoreboardTable;

    private List<Player> players;
    MainFile game;

    public scoreboard(List<Player> players) {
        this.players = players;
    }
    public scoreboard(MainFile game){this.game = game;}
    @Override
    public void show() {
        batch = new SpriteBatch();

        // setting stage
        Viewport viewport = new FitViewport(screenWidth, screenHeight);
        gameStage = new Stage(viewport);

        // setting background to a picture
        Texture backgroundTexture = new Texture(Gdx.files.internal("score_background.jpg"));

        // setting background
        Image backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(screenWidth, screenHeight);

        // Create the scoreboard table
        scoreboardTable = new Table();
        scoreboardTable.setFillParent(true);
        scoreboardTable.top();

        BitmapFont font = new BitmapFont();
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        // Add the heading 'scoreboard'
        Label headingLabel = new Label("Scoreboard", labelStyle);

        scoreboardTable.add(headingLabel).colspan(3).padTop(100).padBottom(100);
        scoreboardTable.row();

        List<Player> sortedPlayers = new ArrayList<>(players);

        Collections.sort(sortedPlayers, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p2.calculatePoints(), p1.calculatePoints());
            }
        });


        for (int rank = 1; rank <= sortedPlayers.size(); rank++) {
            Player player = sortedPlayers.get(rank - 1);

            int points = (rank == 1) ? 0 : player.calculatePoints() - sortedPlayers.get(0).calculatePoints();

            Label rankLabel = new Label(Integer.toString(rank), labelStyle);
            Label playerNameLabel = new Label(player.getName(), labelStyle);
            Label scoreLabel = new Label(Integer.toString(points), labelStyle);

            scoreboardTable.add(rankLabel).padRight(20);
            scoreboardTable.add(playerNameLabel).padRight(20);
            scoreboardTable.add(scoreLabel).padRight(20);
            scoreboardTable.row();
        }

        // adding created things as actors
        gameStage.addActor(backgroundImage);
        gameStage.addActor(scoreboardTable);

        Gdx.input.setInputProcessor(gameStage);
    }
    @Override
    public void render(float delta) {
        ScreenUtils.clear(1, 1, 1, 1);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update and draw the game stage
        gameStage.act(Gdx.graphics.getDeltaTime());
        gameStage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameStage.dispose();
    }
}