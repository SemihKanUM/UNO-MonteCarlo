package Logic;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Card implements Cloneable{
    private Type type;
    private Card.Color color;
    private int number;
    public boolean clickedCard = false;
    public ImageButton imageButton;

    public Card(TextureRegionDrawable imageUp, Card.Type type, Card.Color color, int number) {
        this.type = type;
        this.color = color;
        this.number = number;
        if(imageUp != null){
            imageButton = new ImageButton(imageUp);
            mouse();
        }
    }

    // Constructor that takes a string representation
    public Card(String cardString, TextureRegionDrawable imageUp) {
        String[] parts = cardString.split(" ");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid card representation: " + cardString);
        }

        this.color = Color.valueOf(parts[0].toUpperCase()); // Assuming color is case-insensitive

        // Check if the card has a number
        if ("0123456789".contains(parts[1])) {
            this.number = Integer.parseInt(parts[1]);
            this.type = Type.NUMBER;
        } else {
            // If not a number, assume it's a special type (WILD, WILD_DRAW_FOUR, etc.)
            this.number = -1;
            this.type = Type.valueOf(parts[1].toUpperCase());
        }

        if (imageUp != null) {
            this.imageButton = new ImageButton(imageUp);
            mouse();
        }
    }

    public Card clone() {
        try {
            // Perform a shallow copy by calling the superclass clone method
            return (Card) super.clone();
        } catch (CloneNotSupportedException e) {
            // Handle the exception if necessary
            return null;
        }
    }
    
    public void mouse(){
        imageButton.addListener(new ClickListener() {
            
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                // Mouse-over effect: Jump animation
                imageButton.addAction(
                    Actions.sequence(
                        Actions.moveBy(0, 20, 0.2f, Interpolation.swingOut),
                        Actions.moveBy(0, -20, 0.2f, Interpolation.swingIn)
                    )
                );
            }
            
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clickedCard = true;
                System.out.println(color + " " + type);
            }
        });
    }

    public void setPosition(float x, float y) {
        if (imageButton != null) {
            imageButton.setPosition(x, y);
        }
    }

    public enum Color {
        RED, BLUE, GREEN, YELLOW, WILD;
    }

    public int getPoints() {
        switch(this.type) {
            case NUMBER:
                return this.number;

            case DRAW_TWO:
            case REVERSE:
            case SKIP:
                return 20;

            case WILD:
            case WILD_DRAW_FOUR:
                return 50;

            default:
                return 0;
        }
    }
    // Enumeration for card types
    public enum Type {
        NUMBER, SKIP, REVERSE, DRAW_TWO, WILD, WILD_DRAW_FOUR;
    }


    @Override
    public String toString() {
        if (type == Type.NUMBER) {
            return color + " " + number;
        } else {
            return color + " " + type;
        }
    }

    public boolean isSameCard(Card card){
        if((card.getType() == getType())  && (card.colorOfCard() == colorOfCard()) && (card.getNumber() == getNumber())){
            return true;
        }
        return false;
    }
    
    public Type getType() {
        return type;
    }

    public Card.Color colorOfCard() {
        return color;
        //player.getDeck(0).colorOfCard()
    }

    public int getNumber() {
        return number;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setColor(Card.Color color) {
        this.color = color;
    }
    public Card.Color getColor() {
        return this.color;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}