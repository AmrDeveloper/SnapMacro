package snapmacro.utils;

import javafx.scene.control.Alert;

public class DialogUtils {

    public static void createInformationDialog(String title, String header, String content) {
        Alert infoDialog = new Alert(Alert.AlertType.INFORMATION);
        infoDialog.setTitle(title);
        infoDialog.setHeaderText(header);
        infoDialog.setContentText(content);
        infoDialog.showAndWait();
    }

    public static void createWarningDialog(String title, String header, String content){
        Alert warnDialog = new Alert(Alert.AlertType.WARNING);
        warnDialog.setTitle(title);
        warnDialog.setHeaderText(header);
        warnDialog.setContentText(content);
        warnDialog.showAndWait();
    }

    public static void createErrorDialog(String title, String header, String content){
        Alert errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setTitle(title);
        errorDialog.setHeaderText(header);
        errorDialog.setContentText(content);
        errorDialog.showAndWait();
    }
}