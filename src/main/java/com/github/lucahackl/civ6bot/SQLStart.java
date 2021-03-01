package com.github.lucahackl.civ6bot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class SQLStart {


    public static String Start(String msg, int numb) {

        String newmsg = msg.substring(4);

        String dbURL = "";
        String username = "";
        String password = "";

        try (InputStream input = new FileInputStream("config.properties")) { //get resources from config file

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            // get the sql access data
            dbURL = prop.getProperty("dbURL");
            username = prop.getProperty("username");
            password = prop.getProperty("password");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection(dbURL, username, password)) {
            if (newmsg.substring(0, 5).equals("reset")) {
                try {
                    return ChooseReset.Reset(numb, connection);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else if (newmsg.substring(0, 6).equals("player")) {
                try {
                    ChooseReset player = new ChooseReset();
                    String sendString = player.SettingSQL(connection, numb);
                    return sendString;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no valid entry";
    }


}
