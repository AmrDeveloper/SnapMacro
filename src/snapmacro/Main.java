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
        primaryStage.setTitle("Snap Macro");
        Scene scene = new Scene(root,600, 400);
        scene.getStylesheets().add("snapmacro/styles/editor_style.css");

        Image appIconImage = new Image(Main.class.getResourceAsStream("res/app_icon.png"));
        primaryStage.getIcons().add(appIconImage);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
