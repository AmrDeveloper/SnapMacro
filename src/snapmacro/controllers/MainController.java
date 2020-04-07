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
    @FXML private ImageView debugAction;
    @FXML private ImageView restartAction;
    @FXML private ImageView loadAction;
    @FXML private ImageView saveAction;
    @FXML private ImageView cursorAction;

    //Layouts
    @FXML private TabPane scriptTabPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupViewsHover();
        setViewsListeners();
    }

    private void setupViewsHover(){
        Tooltip.install(runAction, new Tooltip("Run snap Script"));
        Tooltip.install(debugAction, new Tooltip("Debug snap Script"));
        Tooltip.install(restartAction, new Tooltip("Restart snap Script"));
        Tooltip.install(loadAction, new Tooltip("Load snap file"));
        Tooltip.install(saveAction, new Tooltip("Save snap File"));
        Tooltip.install(cursorAction, new Tooltip("Show cursor Position"));
    }

    private void setViewsListeners(){
        runAction.setOnMouseClicked(e -> System.out.println("Run script"));

    }
}
