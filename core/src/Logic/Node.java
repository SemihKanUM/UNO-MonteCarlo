package Logic;

import java.util.ArrayList;

import static Logic.Card.Color;
import static Logic.Card.Color.*;
import static Logic.Card.Type.*;
import static Logic.Card.Type.WILD;

public class Node {

    public int[] state;
    ArrayList<Integer> actions;
    ArrayList<Node> children;
    int N; int T;

    public int nREDskip=0;
    public int nBLUEskip=0;
    public int nYELLOWskip=0;
    public int nGREENskip=0;

    public int nREDrev=0;
    public int nBLUErev=0;
    public int nYELLOWrev=0;
    public int nGREENrev=0;
    public int nREDpl2=0;
    public int nBLUEpl2=0;
    public int nYELLOWpl2=0;
    public int nGREENpl2=0;
    public int nSKIP=0;
    public int nREV=0;
    public int nPL2=0;
    public int nPL4=0;
    public int nCOL=0;
    Node() {
        this.N=0; this.T=0;
        this.state = new int[17];
        this.actions = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    boolean isFullyExpanded() {
        // Check if all possible actions have corresponding child nodes
        // Return true if fully expanded, false otherwise
        return false;
    }

    // I DON'T CLEAR THE ARRAY BEFORE, DEPENDS ON HOW WE WILL USE IT
    public int[] fillState(ArrayList<Card> deck) {


        for (int i = 0; i > deck.size(); i++) {
            Card card = deck.get(i);
            Card.Color color = card.colorOfCard();
            Card.Type type = card.getType();
            if (type == NUMBER || type == SKIP) {
                if (color == RED) {
                    state[1]++;
                } else if (color == BLUE) {
                    state[2]++;
                } else if (color == GREEN) {
                    state[3]++;
                } else if (color == YELLOW) {
                    state[4]++;
                } else {
                }
            } else if (type == SKIP) {
                state[5]++;
                if (color==RED){
                    nREDskip++;
                }else if (color==BLUE){
                    nBLUEskip++;
                }else if (color==GREEN){
                    nGREENskip++;
                }else if (color==YELLOW){
                    nYELLOWskip++;
                }
            } else if (type == REVERSE) {
                state[6]++;
                if (color==RED){
                    nREDrev++;
                }else if (color==BLUE){
                    nBLUErev++;
                }else if (color==GREEN){
                    nGREENrev++;
                }else if (color==YELLOW){
                    nYELLOWrev++;
                }
            } else if (type == DRAW_TWO) {
                state[7]++;
                if (color==RED){
                    nREDpl2++;
                }else if (color==BLUE){
                    nBLUEpl2++;
                }else if (color==GREEN){
                    nGREENpl2++;
                }else if (color==YELLOW){
                    nYELLOWpl2++;
                }
            } else if (type == WILD_DRAW_FOUR) {
                state[8]++;
            } else if (type == WILD) {
                state[9]++;
            }
        }

        Card.Color curretCardCol = Settings_page.getBoard().getBoardColor();
        if (curretCardCol == RED) {
            state[0]=1;
            state[10]=state[1];
            state[14]=nREDskip;
            state[15]=nREDrev;
            state[16]=nREDpl2;
        } else if (curretCardCol == GREEN) {
            state[0]=2;
            state[11]=state[2];
            state[14]=nGREENskip;
            state[15]=nGREENrev;
            state[16]=nGREENpl2;
        } else if (curretCardCol == BLUE) {
            state[0]=3;
            state[12]=state[3];
            state[14]=nBLUEskip;
            state[15]=nBLUErev;
            state[16]=nBLUEpl2;
        } else if (curretCardCol == YELLOW) {
            state[0]=4;
            state[13]=state[4];
            state[14]=nYELLOWskip;
            state[15]=nYELLOWrev;
            state[16]=nYELLOWpl2;
        }


        System.out.print("State : ");
        for (int i = 0; i < state.length; i++) {
            System.out.print(state[i]);
            if (i < state.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println();

    return state;

    }
}

