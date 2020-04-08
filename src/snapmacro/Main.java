package snapmacro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import snapmacro.utils.Settings;
import snapmacro.utils.Theme;
import snapmacro.utils.ThemeManager;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/main_view.fxml"));
        Scene scene = new Scene(root,600, 400);

        //Bind Styles
        Image appIconImage = new Image(Main.class.getResourceAsStream("res/app_icon.png"));
        primaryStage.setTitle("Snap Macro");
        primaryStage.getIcons().add(appIconImage);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.setScene(scene);
        primaryStage.show();

        Settings settings = new Settings();
        ThemeManager.setTheme(scene, Theme.valueOf(settings.getTheme()));
        settings.setThemeChangeListener(theme -> ThemeManager.setTheme(scene, theme));
    }


    public static void main(String[] args) {
        launch(args);
    }
}
