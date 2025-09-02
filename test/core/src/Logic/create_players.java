package Logic;

import java.util.ArrayList;


//This class is to create list of all the players of UNO
public class create_players {
    //here we will store all the names
    public ArrayList<Integer> all_names_of_players = new ArrayList<>();

    public ArrayList<Integer> names_of_players(int nR, int nAI_ML, int nAI_S){

        for (int i = 1; i <= nR; i++){
        int name = (-1)*(i);
        all_names_of_players.add(name);
        }
        for (int j = 1; j <= nAI_ML; j++){
            int name = 2*j;
            all_names_of_players.add(name);
        }
        for (int k = 1; k <= nAI_S; k++){
            int name = (2*k) + 1;
            all_names_of_players.add(name);
        }

        return  all_names_of_players;
    }
}