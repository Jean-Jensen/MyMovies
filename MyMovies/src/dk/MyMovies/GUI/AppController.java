package dk.MyMovies.GUI;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.BLL.BLLMovie;
import dk.MyMovies.DAL.ConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    ConnectionManager con = new ConnectionManager();
    BLLMovie BLL = new BLLMovie();

    @FXML
    private TableView<Movie> tblMovie;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;
    @FXML
    private TableColumn<Movie, Integer> colId;
    @FXML
    private TableColumn<Movie, String> colName;
    @FXML
    private TableColumn<Movie, Double> colRating;
    @FXML
    private TableColumn<Movie, String> colFile;
    @FXML
    private TableColumn<Movie, String> colLast;

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
        colId.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        colRating.setCellValueFactory(new PropertyValueFactory<Movie, Double>("Rating"));
        colFile.setCellValueFactory(new PropertyValueFactory<Movie, String>("FilePath"));
        colLast.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));

        ObservableList<Movie> value = FXCollections.observableArrayList();
        value.setAll(BLL.getAllMovies());
        tblMovie.setItems(value);
    }

    @FXML
    private void addMovie(ActionEvent actionEvent) {
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

    @FXML
    private void deleteMovie(ActionEvent actionEvent) throws IOException {
        Movie selected = tblMovie.getSelectionModel().getSelectedItem();
        if(selected != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/DeleteMovieScene.fxml"));
            Parent root = loader.load();
            DeleteMovieController controller = loader.getController();
            controller.setData(selected.getId(), selected.getName());
            openNewWindow(root);
        }
    }

    @FXML
    private void editMovie(ActionEvent actionEvent) throws IOException {
        Movie selected = tblMovie.getSelectionModel().getSelectedItem();
        if(selected != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/EditMovieScene.fxml"));
            Parent root = loader.load();
            EditMovieController controller = loader.getController();
            controller.setData(selected.getId(),selected.getName(),String.valueOf(selected.getRating()),selected.getFilePath(), selected.getLastView());
            openNewWindow(root);
        }
    }

    private void openNewWindow(Parent root){
        Scene scene = new Scene(root);
        Stage stag = new Stage();
        stag.setScene(scene);
        stag.show();
    }

}
