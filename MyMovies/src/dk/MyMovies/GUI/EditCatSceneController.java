package dk.MyMovies.GUI;

import dk.MyMovies.BLL.BLLCategory;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditCatSceneController {
    public Button btnAddCategory;
    public Button btnCancelCategory;
    public TextField catTextField;
    private AppController appController;
    private BLLCategory BLLCategory;

    // Makes sure BLLCategory is not null
    public EditCatSceneController() {
        BLLCategory = new BLLCategory();
    }

    // Makes sure appController is not null
    public void setAppController(AppController appCtrl){
        appController = appCtrl;
    }

    // Adds text input into the category list
  /*  public void btnAddCategory(ActionEvent actionEvent){
        String text = catTextField.getText();
        BLLCategory.createCategory(text);
        appController.displayCategory();
        Stage stage = (Stage) btnCancelCategory.getScene().getWindow();
        stage.close();
    }*/

    public void btnCancelCategory(ActionEvent actionEvent){
        Stage stage = (Stage) btnCancelCategory.getScene().getWindow();
        stage.close();
    }

}

