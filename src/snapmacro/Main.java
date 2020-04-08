package snapmacro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/main_view.fxml"));
        Scene scene = new Scene(root,600, 400);

        //Bind Styles
        scene.getStylesheets().add("snapmacro/styles/main_style.css");
        scene.getStylesheets().add("snapmacro/styles/tabs_style.css");
        scene.getStylesheets().add("snapmacro/styles/editor_style.css");
        scene.getStylesheets().add("snapmacro/styles/terminal_style.css");

        Image appIconImage = new Image(Main.class.getResourceAsStream("res/app_icon.png"));
        primaryStage.setTitle("Snap Macro");
        primaryStage.getIcons().add(appIconImage);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
