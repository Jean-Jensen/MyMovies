package dk.MyMovies.GUI;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.BLL.BLLCatMov;
import dk.MyMovies.BLL.BLLMovie;
import dk.MyMovies.DAL.ConnectionManager;
import dk.MyMovies.Exceptions.MyMoviesExceptions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppController implements Initializable {

    ConnectionManager con = new ConnectionManager();
    private BLLMovie bllMov = new BLLMovie();
    private BLLCatMov bllCatMov = new BLLCatMov();
    private ContextMenu rightClickMenu;
    private static final Logger logger = Logger.getLogger(AppController.class.getName());

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
        rightClickMenu();

        try {
            checkForUselessMovies();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    //////////////////////////////////////////////////////////
    ////////////////////GUI Stuff/////////////////////////////
    /////////////////////////////////////////////////////////

    //display movie data on table
    public void displayMovies(){
        //colId.setCellValueFactory(new PropertyValueFactory<Movie, Integer>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<Movie, String>("Name"));
        colRating.setCellValueFactory(new PropertyValueFactory<Movie, Double>("Rating"));
        //colFile.setCellValueFactory(new PropertyValueFactory<Movie, String>("FilePath"));
        colLast.setCellValueFactory(new PropertyValueFactory<Movie, String>("LastView"));

        ObservableList<Movie> value = FXCollections.observableArrayList();
        try {
            value.setAll(bllMov.getAllMovies());
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error retrieving all movies: AppController", e);
            showErrorDialog(new MyMoviesExceptions("error retrieving all movies" + e.getMessage(), e));
        }
        tblMovie.setItems(value);
    }

    //Adds a right click menu to delete a movie
    private void rightClickMenu(){
        rightClickMenu = new ContextMenu();
        MenuItem deleteMovie = new MenuItem("Remove Movie");
        deleteMovie.setOnAction(mouseClick -> {
            try {
                deleteMovie(mouseClick);
            } catch (IOException e) {
                throw new RuntimeException("Error with right click menu", e);
            }
        });
        rightClickMenu.getItems().add(deleteMovie);
        tblMovie.setContextMenu(rightClickMenu); // Setting the context menu to work on the tableview
    }

    private void checkForUselessMovies() throws IOException {
        try {
            List<Movie> useless = bllMov.getUselessMovies();
            if(!useless.isEmpty()){

                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/DeleteWarningScene.fxml"));
                Parent root = loader.load();
                openNewWindow(root);
            }
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error retrieving all movies with a rating below 6 that were last opened 2 years ago");
            throw new RuntimeException(e);
        }
    }

    //Error Message Display
    private void showErrorDialog(MyMoviesExceptions e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("This can't be good");
        alert.setHeaderText("Oh No! We ran into a problem!");
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    //////////////////////////////////////////////////////////
    ////////////////////Movie Stuff///////////////////////////
    /////////////////////////////////////////////////////////

    //add new movie
    @FXML
    private void addMovie(ActionEvent actionEvent) throws MyMoviesExceptions {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Movie");
        chooser.getExtensionFilters().addAll(filter1, filter2); //applying filters so we can only select MP4s and MPEG4s
        File selected = chooser.showOpenDialog(tblMovie.getScene().getWindow()); //opening the filechooser in from our window

        if(selected != null){
            String name = selected.getName().substring(0,selected.getName().indexOf('.'));

            //we don't set rating or last time viewed since you can't get that from just the file alone.
            bllMov.createMovie(name, null, selected.getPath(), null);
            displayMovies();
        }
    }

    //delete selected movie (if a movie was selected)
    @FXML
    private void deleteMovie(ActionEvent actionEvent) throws IOException {
        Movie selected = tblMovie.getSelectionModel().getSelectedItem();
        if(selected != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/DeleteMovieScene.fxml"));
            Parent root = loader.load();
            DeleteMovieController controller = loader.getController();
            controller.setData(selected.getId(), selected.getName(), this);
            openNewWindow(root);
        }
    }

    //edit selected movie (if a movie was selected)
    @FXML
    private void editMovie(ActionEvent actionEvent) throws IOException {
        Movie selected = tblMovie.getSelectionModel().getSelectedItem();
        if(selected != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/EditMovieScene.fxml"));
            Parent root = loader.load();
            EditMovieController controller = loader.getController();
            controller.setData(selected.getId(),selected.getName(),String.valueOf(selected.getRating()),selected.getFilePath(), selected.getLastView(), this);
            openNewWindow(root);
        }
    }

    //open a new window (just to avoid repeating code)
    private void openNewWindow(Parent root){
        Scene scene = new Scene(root);
        Stage stag = new Stage();
        stag.setScene(scene);
        stag.show();
    }
    //////////////////////////////////////////////////////////
    ///////////Categories and Movies Connectors///////////////
    /////////////////////////////////////////////////////////

    // Adds a movie to a specific category using the method in BLLCatMov which talks to DAL
    public void addMovieToCategory(int catID, int movID) throws MyMoviesExceptions {
        try {
            bllCatMov.addMovieToCategory(catID, movID);
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error adding movie to category: AppController", e);
            showErrorDialog(new MyMoviesExceptions("Error adding movie to category: AppController - " + e.getMessage(), e));
        }
    }

    // Removes a movie from a specific category using the method in BLLCatMov which talks to DAL
    public void removeMovieFromCategory(int catMovID) throws MyMoviesExceptions {
        try {
            bllCatMov.removeMovieFromCategory(catMovID);
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE,"Error removing movie from category: AppController");
            showErrorDialog(new MyMoviesExceptions("Error removing movie from category: AppController - " + e.getMessage(), e));
        }
    }

    // Retrieves a list of categories for a specific movie using the method in BLLCatMov which talks to DAL
    public List<Integer> getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        try {
            return bllCatMov.getCategoriesForMovie(movID);
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE,"Error retrieving categories for movie: AppController", e);
            showErrorDialog(new MyMoviesExceptions("Error retrieving categories for movie: AppController - " + e.getMessage(), e));
        }
        return null;
    }

    // Retrieves a list of movies for specific categories using the method in BLLCatMov which talks to DAL
    public List<Integer> getMoviesForCategories(List<Integer> catIDs) throws MyMoviesExceptions {
        try {
            return bllCatMov.getMoviesForCategories(catIDs);
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE,"Error retrieving movies for categories: AppController",e);
            showErrorDialog(new MyMoviesExceptions("Error retrieving movies for categories: AppController - " + e.getMessage(), e));
        }
        return null;
    }
}
