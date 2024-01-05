package dk.MyMovies.GUI;

import dk.MyMovies.BLL.BLLMovie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Date;

public class EditMovieController {

    BLLMovie BLL = new BLLMovie();

    @FXML
    private TextField txtName;
    @FXML
    private TextField txtRating;
    @FXML
    private TextField txtFile;
    @FXML
    private DatePicker PckLast;

    private int ID = -1;


    public void setData(int ID, String name, String rating, String Path, String date){
        this.ID = ID;
        txtName.setText(name);
        txtFile.setText(Path);
        if(!rating.isEmpty()){
            txtRating.setText(rating);
        }
        if(!date.isEmpty()){
            PckLast.setValue(Date.valueOf(date).toLocalDate());
        }
    }

    public void EditMovie(ActionEvent actionEvent) {
      //  BLL.editMovie(ID, txtName.getText(),Double.valueOf(txtRating.getText()),txtFile.getText(), PckLast.getValue().toString());
    }
}
