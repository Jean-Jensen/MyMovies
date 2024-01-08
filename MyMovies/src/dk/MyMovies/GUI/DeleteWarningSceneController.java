package dk.MyMovies.GUI;

import dk.MyMovies.BE.Movie;
import dk.MyMovies.BLL.BLLMovie;
import dk.MyMovies.Exceptions.MyMoviesExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class DeleteWarningSceneController implements Initializable {
    @FXML
    private TableView<Movie> tblMov;
    @FXML
    private TableColumn<Movie, Integer> colID;
    @FXML
    private TableColumn<Movie, String> colName;
    @FXML
    private TableColumn<Movie, String> colLast;
    @FXML
    private TableColumn<Movie, Double> colRating;
    private BLLMovie bllMov = new BLLMovie();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayMovies();
    }

    public void displayMovies(){
        colID.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        colRating.setCellValueFactory(new PropertyValueFactory<Movie, Double>("Rating"));
        colLast.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));

        ObservableList<Movie> value = FXCollections.observableArrayList();
        try {
            value.setAll(bllMov.getUselessMovies());
            System.out.println("weeeeeee");
        } catch (MyMoviesExceptions e) {
            throw new RuntimeException(e);
        }
        tblMov.setItems(value);
    }

    public void deleteAllUseless(ActionEvent actionEvent) throws MyMoviesExceptions {
        bllMov.deleteAllUselessMovies();
        closeWindow(actionEvent);
    }

    public void deleteSelected(ActionEvent actionEvent) {

    }

    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) tblMov.getScene().getWindow();
        stage.close();
    }
}
