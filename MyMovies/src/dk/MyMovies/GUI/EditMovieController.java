package dk.MyMovies.GUI;

import dk.MyMovies.BLL.BLLMovie;
import dk.MyMovies.Exceptions.MyMoviesExceptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Date;

public class EditMovieController {


    BLLMovie BLL = new BLLMovie();

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtRating;
    @FXML
    private TextField txtIMDB;
    @FXML
    private TextField txtFile;
    @FXML
    private DatePicker PckLast;

    private int ID = -1;
    private AppController control;

    public void setData(int ID, String name, String rating, String IMDB, String Path, String date, AppController control){
        this.ID = ID;
        txtName.setText(name);
        txtIMDB.setText(IMDB);
        txtFile.setText(Path);
        if(!rating.isEmpty()){
            txtRating.setText(rating);
        }
        if(!date.isEmpty()){
            System.out.println(date);
            date = date.substring(0,10);
            System.out.println(date);
            PckLast.setValue(Date.valueOf(date).toLocalDate());
        }
        this.control = control;
    }

    public void EditMovie(ActionEvent actionEvent) throws MyMoviesExceptions {
        BLL.editMovie(ID, txtName.getText(),Double.valueOf(txtIMDB.getText()),Double.valueOf(txtRating.getText()), txtFile.getText(), PckLast.getValue().toString());
        control.displayMovies();
        closeWindow();
    }
    private void closeWindow(){
        Stage stage = (Stage) txtName.getScene().getWindow();
        stage.close();
    }
}
