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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppController implements Initializable {

    public Button btnAddCat;
    public Button btnEditCat;
    private ConnectionManager con = new ConnectionManager();
    private BLLCategory BLLCat = new BLLCategory();
    private BLLMovie bllMov = new BLLMovie();
    private BLLCatMov bllCatMov = new BLLCatMov();
    private ContextMenu rightClickMenu;
    private static final Logger logger = Logger.getLogger(AppController.class.getName());

    @FXML
    private TableView<Movie> tblMovie;
    @FXML
    private TableView<Category> tblCategory;

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
    private TableColumn<Category, String> catColName;
    @FXML
    private TableColumn<Movie, Double> colRating;
    @FXML
    private TableColumn<Movie, String> colFile;
    @FXML
    private TableColumn<Movie, String> colLast;
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
        displayMovies();
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
        rightClickMenu.getItems().add(rightClickMenuRemoveMovie());
        rightClickMenu.getItems().add(rightClickMenuAddCategory());
      //  rightClickMenu.getItems().add(rightClickMenuRemoveCategory());
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
            for (Object item : lvCategories.getItems()) {             // loops lvCategories for each item
                //checks to see if its a Checkbox && Checkbox is selected
                //we use instanceof to make sure that our new Object is a Checkbox
                if (item instanceof CheckBox && ((CheckBox) item).isSelected()) {
                    //if yes it uses .getUserData to collect the ID
                    selectedCategoryIds.add((Integer) ((CheckBox) item).getUserData());
                }
            }
            List<Movie> movies;
            //If nothing is selected, get all movies
            if (selectedCategoryIds.isEmpty()) {
                movies = bllMov.getAllMovies();
                //else get movies for each category selected
            } else {
                List<Integer> movieIds = bllCatMov.getMoviesForCategories(selectedCategoryIds);
                //gets the actual movie object
                movies = bllMov.getMoviesByIds(movieIds);
            }
            //Turns our movie list into an Observable list to observe for changes
            ObservableList<Movie> observableMovies = FXCollections.observableArrayList(movies);
            //populates the tblMovie with the list
            tblMovie.setItems(observableMovies);
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE, "Error retrieving movies for categories: AppController", e);
            showErrorDialog(new MyMoviesExceptions("error retrieving movies for categories" + e.getMessage(), e));
        }
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
   /* public List<Integer> getCategoriesForMovie(int movID) throws MyMoviesExceptions {
        try {
            return bllCatMov.getCategoriesForMovie(movID);
        } catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE,"Error retrieving categories for movie: AppController", e);
            showErrorDialog(new MyMoviesExceptions("Error retrieving categories for movie: AppController - " + e.getMessage(), e));
        }
        return null;
    }*/

    public List<Movie> getMoviesByNameAndCategories(String movName, List<Integer> catIDs) throws MyMoviesExceptions {
        try {
            return bllCatMov.getMoviesByNameAndCategories(movName, catIDs);
        }catch (MyMoviesExceptions e) {
            logger.log(Level.SEVERE,"Error retrieving movies by name and categories: DAO Error", e);
            showErrorDialog(new MyMoviesExceptions("Error retrieving movies by name and categories: AppController - " + e.getMessage(), e));
        }
        return null;
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
    
}
