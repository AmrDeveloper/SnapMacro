package snapmacro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import snapmacro.utils.AppConst;
import snapmacro.utils.Settings;
import snapmacro.utils.Theme;
import snapmacro.utils.ThemeManager;

public class Main extends Application {

    private static Stage mainStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        mainStage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("views/main_view.fxml"));
        Scene scene = new Scene(root, AppConst.MIN_WIDTH, AppConst.MIN_HEIGHT);

        //Bind Styles
        Image appIconImage = new Image(Main.class.getResourceAsStream("res/app_icon.png"));
        primaryStage.setTitle(AppConst.NAME);
        primaryStage.getIcons().add(appIconImage);
        primaryStage.setMinWidth(AppConst.MIN_WIDTH);
        primaryStage.setMinHeight(AppConst.MIN_HEIGHT);
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

    public static Stage getMainStage(){
        return mainStage;
    }
}
