package snapmacro.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static File createNewFile(String filePath) {
        try {
            File newFolder = new File(filePath);
            newFolder.createNewFile();
            DialogUtils.createInformationDialog("Success",
                    null, "New File Created");
            return newFolder;
        } catch (IOException e) {
            String errorMessage = "Can't Create new file in this path";
            DialogUtils.createErrorDialog("Error",null,errorMessage);
            return null;
        }
    }

    public static File createNewFolder(String filePath) {
        File newFolder = new File(filePath);
        newFolder.mkdir();
        return newFolder;
    }

    public static File openSourceFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        FileChooser.ExtensionFilter snapExtension =
                new FileChooser.ExtensionFilter("Snap Script", "*.ss");
        fileChooser.getExtensionFilters().add(snapExtension);

        return fileChooser.showOpenDialog(null);
    }

    public static File openSourceDir(String title) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(title);
        return chooser.showDialog(null);
    }

    public static File saveAsSourceFile(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        return fileChooser.showSaveDialog(null);
    }

    public static void updateContent(File file, String content) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(content);
            fileWriter.close();
            DialogUtils.createInformationDialog("Success", null, "File content Updated");
        } catch (IOException iox) {
            String errorMessage = "Can't Save this file, Please make sure this file not deleted";
            DialogUtils.createErrorDialog("Error",null, errorMessage);
        }
    }

    public static void deleteFile(File file) {
        file.deleteOnExit();
    }
}