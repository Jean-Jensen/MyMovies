package dk.MyMovies;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.GUI.FXML.GraphicalGUI;


public class Main {
    public static void main(String[] args) throws SQLServerException {
        System.out.println("Hello world :)");
        GraphicalGUI.run();
    }
}