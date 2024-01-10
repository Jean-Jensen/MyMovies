package dk.MyMovies.GUI;

import dk.MyMovies.BLL.BLLMovie;
import dk.MyMovies.Exceptions.MyMoviesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteMovieController implements Initializable {

    @FXML
    private Label lblMovName;
    private int ID;
    BLLMovie BLL = new BLLMovie();
    AppController control;

    public void deleteMovie(ActionEvent actionEvent) throws MyMoviesExceptions {
        BLL.deleteMovie(ID);
        control.displayMovies();
        closeWindow();
    }
    private void closeWindow(){
        Stage stage = (Stage) lblMovName.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setData(int ID, String name, AppController control){
        lblMovName.setText(name);
        this.ID = ID;
        this.control = control;
    }

}
