package dk.MyMovies.GUI;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.MyMovies.BE.CatMovConnection;
import dk.MyMovies.BE.Category;
import dk.MyMovies.BE.Movie;
import dk.MyMovies.BLL.BLLCatMov;
import dk.MyMovies.BLL.BLLCategory;
import dk.MyMovies.BLL.BLLMovie;
import dk.MyMovies.DAL.ConnectionManager;
import dk.MyMovies.Exceptions.MyMoviesExceptions;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppController implements Initializable {

    public Button btnAddCat;
    public Button btnEditCat;
    @FXML
    private Label lblTimeVal;
    @FXML
    private Slider progressSlider;

    @FXML
    private MediaView mediaView;
    private MediaPlayer player;
    private ConnectionManager con = new ConnectionManager();
    private BLLCategory BLLCat = new BLLCategory();
    private BLLMovie bllMov = new BLLMovie();
    private BLLCatMov bllCatMov = new BLLCatMov();
    private ContextMenu rightClickMenu;
    private static final Logger logger = Logger.getLogger(AppController.class.getName());

    @FXML
    private TableView<CatMovConnection> tblMovie;
    @FXML
    private TableView<Category> tblCategory;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;

    @FXML
    private TableColumn<CatMovConnection, String> colName;
    @FXML
    private TableColumn<CatMovConnection, String> colCat;
    @FXML
    private TableColumn<CatMovConnection, Double> colRating;
    @FXML
    private TableColumn<CatMovConnection, String> colLast;
    @FXML
    private ListView<CheckBox> lvCategories;
    @FXML
    private Slider ratingSlider;
    @FXML
    private Label lblSliderValue;

    private FileChooser.ExtensionFilter filter1 = new FileChooser.ExtensionFilter(".mp4 files", "*.mp4");
    private FileChooser.ExtensionFilter filter2 = new FileChooser.ExtensionFilter(".mpeg4 files", "*.mpeg4");


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con.getConnection();
        } catch (SQLServerException e) {
            throw new RuntimeException(e);
        }
        try {
            displayMovies();
        } catch (MyMoviesExceptions e) {
            throw new RuntimeException(e);
        }
        rightClickMenu();
        rightClickMenuRemoveCategory();
        RatingSlider();
        checkBoxCat();

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
    public void displayMovies() throws MyMoviesExceptions {
        List<CatMovConnection> catMovConnections = bllCatMov.getAllCatMovConnections();

        //Using getCatMovMap method to avoid duplicate code
        Map<Integer, CatMovConnection> catMovMap = getCatMovMap(catMovConnections);

        if (!catMovMap.isEmpty()) {
            tblMovie.getItems().clear();
            tblMovie.getItems().addAll(catMovMap.values());

            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            colLast.setCellValueFactory(new PropertyValueFactory<>("lastView"));
            colCat.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        }
    }


    //Adds a right click menu to delete a movie

    private void rightClickMenu(){
        rightClickMenu = new ContextMenu();
        rightClickMenu.getItems().add(rightClickMenuRemoveMovie());
        rightClickMenu.getItems().add(rightClickMenuAddCategory());
        tblMovie.setContextMenu(rightClickMenu);
    }

    private MenuItem rightClickMenuRemoveMovie() {
        MenuItem removeMovie = new MenuItem("Remove Movie");
        removeMovie.setOnAction(mouseClick -> {
            try {
                deleteMovie(mouseClick);
            } catch (IOException e) {
                throw new RuntimeException("Error with right click menu", e);
            }
        });
        return removeMovie;
    }

    private Menu rightClickMenuAddCategory(){
        Menu addCategoryMenu = new Menu("Add Category");
        try {
            List<Category> categories = BLLCat.getAllCategory();
            for (Category category : categories) {
                // Add Category submenu
                MenuItem categoryItem = new MenuItem(category.getCatName());
                categoryItem.setOnAction(actionEvent -> {
                    Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();
                    if (selectedMovie != null) {
                        try {
                            bllCatMov.addMovieToCategory(category.getCatId(), selectedMovie.getId());
                            // Refresh the movie table to reflect the changes
                            displayMovies();
                        } catch (MyMoviesExceptions e) {
                            logger.log(Level.SEVERE, "Error adding movie to category: AppController", e);
                            showErrorDialog(new MyMoviesExceptions("error adding movie to category" + e.getMessage(), e));
                        }
                    }
                });
                addCategoryMenu.getItems().add(categoryItem);
            }
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error retrieving all categories: AppController", e);
            showErrorDialog(new MyMoviesExceptions("error retrieving all categories" + e.getMessage(), e));
        }
        return addCategoryMenu;
    }


    private void rightClickMenuRemoveCategory(){
        // Delete Category submenu
        Menu deleteCategoryMenu = new Menu("Delete Category");
        rightClickMenu.getItems().add(deleteCategoryMenu);

        rightClickMenu.setOnShowing(event -> {
            Movie selectedMovie = tblMovie.getSelectionModel().getSelectedItem();
            if (selectedMovie != null) {
                deleteCategoryMenu.getItems().clear(); // Clear the old categories
                try {
                    List<CatMovConnection> catMovConnections = bllCatMov.getCategoriesForMovie(selectedMovie.getId());
                    for (CatMovConnection catMovConnection : catMovConnections) {
                        MenuItem categoryItem = new MenuItem(catMovConnection.getName());
                        categoryItem.setOnAction(actionEvent -> {
                            try {
                                bllCatMov.removeMovieFromCategory(catMovConnection.getCatMovID());
                                // Refresh the movie table to reflect the changes
                                displayMovies();
                            } catch (MyMoviesExceptions e) {
                                logger.log(Level.SEVERE, "Error removing movie from category: AppController", e);
                                showErrorDialog(new MyMoviesExceptions("error removing movie from category" + e.getMessage(), e));
                            }
                        });
                        deleteCategoryMenu.getItems().add(categoryItem);
                    }
                } catch (MyMoviesExceptions e) {
                    logger.log(Level.SEVERE, "Error retrieving categories for movie: AppController", e);
                    showErrorDialog(new MyMoviesExceptions("error retrieving categories for movie" + e.getMessage(), e));
                }
            }
        });
    }


    private void checkForUselessMovies() throws IOException {
        try {
            List<Movie> useless = bllMov.getUselessMovies();
            if(!useless.isEmpty()){

                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/DeleteWarningScene.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setAlwaysOnTop(true);
                stage.show();
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

    public void RatingSlider() {
        ratingSlider.valueProperty().addListener((observable, oldValue, newValue) ->
                lblSliderValue.setText("Rating: " + String.format("%.0f", newValue)));
    }

    private void checkBoxCat() {
        try {
            BLLCategory bllCategory = new BLLCategory();
            List<Category> categories = bllCategory.getAllCategory();
            for (Category category : categories) {
                CheckBox checkBox = new CheckBox(category.getCatName());
                checkBox.setUserData(category.getCatId()); // Store the category ID in the CheckBox
                // Add listener to know if the checkbox is checked or not and calls the updateMovieTable method to update the table accordingly
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> updateMovieTable());
                lvCategories.getItems().add(checkBox); // Adds the checkbox to the listview
            }
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error retrieving all categories: AppController", e);
            showErrorDialog(new MyMoviesExceptions("error retrieving all categories" + e.getMessage(), e));
        }
    }

    private void updateMovieTable() {
        try {
            List<Integer> selectedCategoryIds = new ArrayList<>();
            for (Object item : lvCategories.getItems()) {
                if (item instanceof CheckBox && ((CheckBox) item).isSelected()) {
                    selectedCategoryIds.add((Integer) ((CheckBox) item).getUserData());
                }
            }
            List<CatMovConnection> catMovConnections;
            if (selectedCategoryIds.isEmpty()) {
                catMovConnections = bllCatMov.getAllCatMovConnections();
            } else {
                List<Integer> movieIds = bllCatMov.getMoviesForCategories(selectedCategoryIds);
                catMovConnections = bllCatMov.getCatMovConnectionsByIds(movieIds);
            }
            //check getCatMovMap below
            Map<Integer, CatMovConnection> catMovMap = getCatMovMap(catMovConnections);

            catMovConnections = new ArrayList<>(catMovMap.values());

            ObservableList<CatMovConnection> observableMovies = FXCollections.observableArrayList(catMovConnections);
            tblMovie.setItems(observableMovies);
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error retrieving movies for categories: AppController", e);
            showErrorDialog(new MyMoviesExceptions("error retrieving movies for categories" + e.getMessage(), e));
        }
    }

    private Map<Integer, CatMovConnection> getCatMovMap(List<CatMovConnection> catMovConnections) {
        Map<Integer, CatMovConnection> catMovMap = new HashMap<>();
        for (CatMovConnection catMovConnection : catMovConnections) {
            //I am checking to see if my catMovConnection has a key in the map
            if (catMovMap.containsKey(catMovConnection.getId())) {
                //Gets the CatMovConnection connected to this ID
                CatMovConnection existingCatMovConnection = catMovMap.get(catMovConnection.getId());
                // taking our existing CatMovConnection and adding , + the new category
                existingCatMovConnection.setCategoryName(existingCatMovConnection.getCategoryName() + ", " + catMovConnection.getCategoryName());
            } else {
                //If there is no key then add it to map using the ID as its key with .put(key, value);
                catMovMap.put(catMovConnection.getId(), catMovConnection);
            }
        }
        return catMovMap;
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
    ////////////////////Category Stuff////////////////////////
    //////////////////////////////////////////////////////////

    //display cat data on table
    public void addCategory(ActionEvent actionEvent) {
        btnAddCat.setOnMouseClicked(event -> {
            try {
                FXMLLoader editCatScene = new FXMLLoader(getClass().getResource("FXML/EditCatScene.fxml"));
                Parent root = editCatScene.load();
                EditCatSceneController controller = editCatScene.getController();
                controller.setAppController(this);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("Add category");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

   /* public void displayCategory(){
        catColName.setCellValueFactory(new PropertyValueFactory<Category, String>("catName"));
        ObservableList<Category> list = FXCollections.observableArrayList();
        try {
            list.setAll(BLLCat.getAllCategory());
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error retrieving all categories: AppController", e);
            showErrorDialog(new MyMoviesExceptions("error retrieving all categoris" + e.getMessage(), e));
        }
        tblCategory.setItems(list);
    }*/

    /*   public void deleteCategory(ActionEvent mouseClick){
           Category selected = tblCategory.getSelectionModel().getSelectedItem();
           BLLCat.deleteCategory(selected.getCatId());
           displayCategory();
       }
       private void rightClickMenuCategory(){
           rightClickMenu = new ContextMenu();
           MenuItem deleteCategory = new MenuItem("Remove Category");
           deleteCategory.setOnAction(mouseClick -> {
               deleteCategory(mouseClick);
           });
           rightClickMenu.getItems().add(deleteCategory);
           tblCategory.setContextMenu(rightClickMenu); // Setting the context menu to work on the tableview
       }*/
    public void editCategory(ActionEvent actionEvent) {}

    public void Play(ActionEvent actionEvent) {
        player.play();
    }

    public void Pause(ActionEvent actionEvent) {
        player.pause();
    }

    public void Reset(ActionEvent actionEvent) {
        if(player.getStatus() != MediaPlayer.Status.READY){
            player.seek(Duration.ZERO);
            progressSlider.setValue(0);
        }
    }

    public void setMediaPlayer(MouseEvent mouseEvent) {
        Movie selected = tblMovie.getSelectionModel().getSelectedItem();
        if(selected !=null){
            File file = new File(selected.getFilePath());
            if(file.exists()) {
                Media media = new Media(file.toURI().toString());
                player = new MediaPlayer(media);
                mediaView.setMediaPlayer(player);
                setProgressSlider();
            } else {
                System.out.println("File not found: " + selected.getFilePath());
                // Handle the case where the file does not exist
            }
        }
    }

    public void setProgressSlider(){
        if(player != null){
            player.setOnReady(new Runnable() {
                @Override
                public void run() {
                    progressSlider.setMax(player.getTotalDuration().toSeconds()); //setting the max value of the slider to the duration in seconds
                    //duration in mediaplayer is only accessible when status = ready. which is why it needs to be in here
                }
            });

            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    progressSlider.setValue(newValue.toSeconds()); //setting the progressbars value to be the new time value
                }
            });
        }
    }

    public void setMovieTime(MouseEvent mouseEvent) {
        if(player != null){
            player.seek(Duration.seconds(progressSlider.getValue())); //mediaplayer changes to the slider value (which is in seconds)
        }
    }
}