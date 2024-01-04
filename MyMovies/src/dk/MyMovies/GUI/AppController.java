package dk.MyMovies.GUI;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movies;
import dk.MyMovies.BLL.BLLManager;
import dk.MyMovies.DAL.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    ConnectionManager con = new ConnectionManager();
    BLLManager BLL = new BLLManager();

    public TableView<Movies> tblMovie;
    public Button btnAdd;
    public Button btnDelete;
    public Button btnEdit;
    public TableColumn<Movies, Integer> colId;
    public TableColumn<Movies, String> colName;
    public TableColumn<Movies, Double> colRating;
    public TableColumn<Movies, String> colFile;
    public TableColumn<Movies, String> colLast;

    private FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(".mp4 files", "*.mp4");
    private FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter(".mpeg4 files", "*.mpeg4");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con.getConnection();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }
        displayMovies();
    }

    private void displayMovies(){
        colId.setCellValueFactory(new PropertyValueFactory<Movies, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Movies, String>("Name"));
        colRating.setCellValueFactory(new PropertyValueFactory<Movies, Double>("Rating"));
        colFile.setCellValueFactory(new PropertyValueFactory<Movies, String>("FilePath"));
        colLast.setCellValueFactory(new PropertyValueFactory<Movies, String>("LastView"));

        ObservableList<Movies> value = FXCollections.observableArrayList();
        value.setAll(BLL.getAllMovies());
        tblMovie.setItems(value);
    }

    public void addMovie(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Movie");
        chooser.getExtensionFilters().addAll(filter1, filter2); //applying filters so we can only select MP4s and MPEG4s
        File selected = chooser.showOpenDialog(tblMovie.getScene().getWindow()); //opening the filechooser in from our window

        if(selected != null){
            System.out.println(selected.getName());
            System.out.println(selected.getPath());
            selected.getName().substring(0,selected.getName().indexOf('.'));

            //we don't set rating or last time viewed since you can't get that from just the file alone.
            BLL.createMovie(selected.getName(), null, selected.getPath(), null);

        }
    }

    public void deleteMovie(ActionEvent actionEvent) {

    }

    public void editMovie(ActionEvent actionEvent) {

    }
}
