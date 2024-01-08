package dk.MyMovies.GUI;

import dk.MyMovies.BLL.BLLCategory;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EditCatSceneController {
    public Button btnAddCategory;
    public Button btnCancelCategory;
    public TextField catTextField;
    private AppController appController;
    private BLLCategory BLLCategory;

    public void getAppController(AppController appCtrl){
        appController = appCtrl;
    }
    public void addCategory(ActionEvent actionEvent){
        String text = catTextField.getText();
        BLLCategory.createCategory(text);
        appController.displayCategory();
    }
}

