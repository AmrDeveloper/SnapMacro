package snapmacro.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    //Actions
    @FXML private ImageView runAction;

    @FXML private TabPane scriptTabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupViewsHover();

    }

    private void setupViewsHover(){
        Tooltip.install(runAction, new Tooltip("Run snap Script"));
    }
}
