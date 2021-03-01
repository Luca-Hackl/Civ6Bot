package com.github.lucahackl.civ6bot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChooseReset {


    public String SettingSQL(Connection connection, int numb) {

        List<String> terms = new ArrayList<>();
        String query = "select leader from player" + numb;
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String leader = rs.getString("leader");
                terms.add(leader);  //takes every leader in the table of the player that's left
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        int size = terms.size();

        if (size != 0) { //checks if any values are in list
            String whatleader = Chooser(size, numb, connection);
            return whatleader;

        } else { //if list (table) is empty resets table
            System.out.println("Table is empty");
            Reset(numb, connection);
        }
        return "No valid entry";
    }

    public static String Chooser(int size, int numb, Connection connection) {


        Random specificLeader = new Random();
        int test = specificLeader.nextInt(size); //generates a random number between 1 and length of list
        String query2 = "select leader from player" + numb + " WHERE ID = " + test;
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query2);
            while (rs.next()) {
                String leader = rs.getString("leader");

                String query3 = "DELETE FROM player" + numb + " WHERE ID = " + test; //deletes leader that was chosen to avoid getting the same result next time
                PreparedStatement preparedStmt = connection.prepareStatement(query3);
                preparedStmt.execute();

                return "Player " + numb + ": " + leader;
            }
            rs.close();

            String query4 = "SET @reset = 0;";
            PreparedStatement preparedStmt2 = connection.prepareStatement(query4);

            String query5 = "UPDATE player" + numb + " SET id = @reset:= @reset + 1;";
            PreparedStatement preparedStmt3 = connection.prepareStatement(query5);

            preparedStmt2.execute();
            preparedStmt3.execute(); //resets auto_increment counter back to 0


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return "No valid entry";
    }


    public static String Reset(int numb, Connection connection){

        String[] leaders = {"Alexander", "Amanitore", "Ambiorix", "Ba Trieu", "Basil II", "Katharine von Medici (Erhabenheit)",
                "Katharine von Medici(Schwarze Königin)", "Chandragupta", "Cleopatra", "Cyrus", "Dido", "Eleanor of Aquitaine (England)",
                "Eleanor of Aquitaine (Frankreich)", "Frederick Barbarossa", "Gandhi",
                "Genghis Khan", "Gilgamesh", "Gitarja", "Gorgo", "Hammurabi", "Harald Hardrada", "Hojo Tokimune",
                "Jadwiga", "Jayavarman VII", "John Curtin", "Kristina", "Kublai Khan (China)", "Kublai Khan (Mongolei)", "Kupe", "Lady Six Sky", "Lautaro",
                "Mansa Musa", "Matthias Corvinus", "Menelik II", "Montezuma", "Mvemba a Nzinga", "Pachacuti", "Pedro II",
                "Pericles", "Peter", "Philip II", "Poundmaker", "Qin Shi Huang", "Robert the Bruce", "Saladin", "Seondeok",
                "Shaka", "Simon Bolivar", "Suleiman", "Tamar", "Teddy Roosevelt (Rough Rider)", "Teddy Roosevelt (Bull Moose)",
                "Tomyris", "Trajan", "Victoria", "Wilfrid Laurier", "Wilhelmina"};

        try {
            String query6 = "TRUNCATE TABLE player" + numb;
            PreparedStatement preparedStatement = connection.prepareStatement(query6);
            preparedStatement.execute(); //deletes every value so table is entirely empty
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        for (String leader : leaders) { //executes it once for every name
            try {

                String query7 = "INSERT INTO player" + numb + " " + "(leader)" +
                        "VALUES (?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query7);
                preparedStatement2.setString(1, leader);
                preparedStatement2.executeUpdate(); //refills table with every single leader again
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return "Reset successful";
    }
}

