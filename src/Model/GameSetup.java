package Model;

import View.TerritoryView;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Erik Iuhas
 */
public class GameSetup {

    private final HashMap<String, Territory> world_map;
    private final String territory_CSV;
    private final ArrayList<Territory> unclaimed_territory;
    private final HashMap<String, Continent> continentMap;

    /**
     * Model.GameSetup is in charge of calling private methods which
     * distribute the troops to players and then begins to place
     * the troops randomly on the world_map
     * @param players a list of the players, provided by Game object
     */
    public GameSetup(ArrayList<Player> players, Component parent){
        this.territory_CSV = "/resources/TerritoryNeighbours.csv";
        this.world_map = new HashMap<>();
        this.continentMap = new HashMap<>();
        this.unclaimed_territory = new ArrayList<>();
        set_neighbours(read_csv(),parent);
        distribute_troops(players);
        }

    /**
     * By the rules of risk you can have 2-6 players, when there are 3 players
     * each player starts with 35 troops, this value lowers by five for each
     * player new player. 2 players is a special case and will be assigned 50 per person
     * @param players an array list of players
     */
    private void provide_troops(ArrayList<Player> players) {
        int distribute_val;
        if(players.size() == 2){distribute_val = 50;}
        else{distribute_val = 35 - 5 * (players.size() - 3);}
        for (Player x : players) {
            x.setDeployableTroops(distribute_val);
        }
    }

    /**
     * This method is in charge of iterating through the ArrayList of players and placing one troop on the unclaimed
     * territories. After claiming all territories each player begins to randomly distribute troops on their
     * own territories. The random placement is done by placing one troop at a time for a relatively even distributed
     * troop count.
     * @param players ArrayList of players
     */
    private void distribute_troops(ArrayList<Player> players){
        provide_troops(players);
        Player current_player;
        Territory current_territory;
        int troop_num;
        for(int i = 0; unclaimed_territory.size() != 0;i++){
            current_player = players.get(i%players.size());
            current_territory = select_random_territory(unclaimed_territory);
            current_territory.setOccupant(current_player);
            current_territory.setTroops(current_player.placeDeployableTroops(1));
            current_player.addTerritory(current_territory.getTerritoryName(),current_territory);
            unclaimed_territory.remove(current_territory);
        }
        Territory random_territory;
        ArrayList<Territory> territory_list;
        for(Player curr_player : players){
            territory_list = new ArrayList<>(curr_player.getTerritoriesOccupied().values());
            while(curr_player.getDeployableTroops() != 0){
                troop_num = 1;
                random_territory = select_random_territory(territory_list);
                random_territory.setTroops(random_territory.getTroops()+curr_player.placeDeployableTroops(troop_num));
            }
        }
        printWorldInfo();
    }

    /**
     * This method is used for randomly selecting a territory from a list.
     * @param list_of_territories a list of territories
     * @return return the randomly selected territory
     */
    private Territory select_random_territory(ArrayList<Territory> list_of_territories){
        Random number_generator = new Random();
        int territory_num;
        territory_num = number_generator.nextInt(list_of_territories.size()) ;
        return list_of_territories.get(territory_num);
    }

    /**
     * Iterate through ArrayList generated by CSV to obtain neighbours for
     * territories.
     * @param territories ArrayList generated from TerritoryNeighbours.csv
     */
    private void set_neighbours(ArrayList<String[]> territories, Component parent){
        String continent_name;
        String territory_name;
        int x;
        int width;
        int y;
        int height;
        for(String[] temp_territory : territories){
            continent_name = temp_territory[0];
            territory_name = temp_territory[5];
            x = Integer.parseInt(temp_territory[1]);
            y = Integer.parseInt(temp_territory[2]);
            width = Integer.parseInt(temp_territory[3]) - x;
            height = Integer.parseInt(temp_territory[4]) - y;
            Territory added_territory = new Territory(territory_name);
            added_territory.setTerritoryView(territory_name,x,y,width,height,parent);
            world_map.put(territory_name,added_territory);
            if (continentMap.get(continent_name) == null){
                continentMap.put(continent_name,new Continent(continent_name));
            }
            continentMap.get(continent_name).addContinentTerritory(added_territory.getTerritoryName(),added_territory);
        }
        for(String[] temp_territory : territories){
            territory_name = temp_territory[5];
            ArrayList<String> temp_sub = new ArrayList<>(Arrays.asList(temp_territory));
            for(String temp_neighbours : temp_sub.subList(6,temp_sub.size())){
                world_map.get(territory_name).addNeighbour(world_map.get(temp_neighbours));
            }
        }
        unclaimed_territory.addAll(world_map.values());
    }

    /**
     * Read the CSV file and obtain the Neighbours, it does this by reading file TerritoryNeighbour.csv and sorting it
     * into a String[].
     * @return list of territories
     */
    private ArrayList<String[]> read_csv(){
        String row;
        ArrayList<String[]> territory_list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(this.territory_CSV)));
            reader.readLine(); //Skip first line
            while((row = reader.readLine()) != null){
                String[] territories = row.split(",");
                territory_list.add(territories);
            }
        } catch (FileNotFoundException e){
            System.out.println("Error: File not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return territory_list;
    }

    /**
     * Iterates through the world map territories and prints the current state of the world at the start of the game.
     */
    public void printWorldInfo(){
        for(Territory x : world_map.values()){
            x.updateView();
            x.print_info();
        }
    }

    /**
     * After gameSetup ends it's final method is used for returning the world map. The gameSetup method could be used again
     * later if players would like to restart the game.
     * @return Hashmap of Territories
     */
    public HashMap<String, Territory> returnWorldMap(){
        return world_map;
    }

    public HashMap<String,Continent> returnContinentMap() {
        return continentMap;
    }
}

