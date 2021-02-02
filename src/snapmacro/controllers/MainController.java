package snapmacro.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import snapmacro.Main;
import snapmacro.lang.*;
import snapmacro.ui.SnapEditor;
import snapmacro.utils.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public class MainController implements Initializable {

    //Actions
    @FXML private ImageView runAction;
    @FXML private ImageView debugAction;
    @FXML private ImageView restartAction;
    @FXML private ImageView stopAction;
    @FXML private ImageView loadAction;
    @FXML private ImageView saveAction;
    @FXML private ImageView cursorAction;
    @FXML private ImageView clearAction;
    @FXML private ImageView themeAction;

    //Views
    @FXML private TextArea resultTextArea;

    //Layouts
    @FXML private TabPane scriptTabPane;
    @FXML private AnchorPane mainLayout;
    @FXML private SplitPane snapLayoutSplitPane;

    //Utils
    private Logger logger;
    private SnapRuntime snapRuntime;
    private CodeArea currentCodeArea;
    private Future<?> currentFutureTask;
    private ExecutorService executorService;

    //Constants
    private boolean showCursorPosition = false;
    private static final String DEBUG_TAG = MainController.class.getSimpleName();
    private static final int THREAD_AVAILABLE_NUMBER = Runtime.getRuntime().availableProcessors();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        executorService = Executors.newFixedThreadPool(THREAD_AVAILABLE_NUMBER);
        logger = Logger.getLogger(DEBUG_TAG);
        snapRuntime = new SnapRuntime(mStreamListener);

        Stage mainStage = Main.getMainStage();
        mainStage.addEventFilter(KeyEvent.KEY_PRESSED, mShortcutListener);

        setupViewsHover();
        setupViewsListeners();
        setupResultTextArea();
        setupSnapScriptTabsLayout();
    }

    private void setupViewsHover(){
        Tooltip.install(runAction, new Tooltip("Run snap Script (F5)"));
        Tooltip.install(debugAction, new Tooltip("Debug snap Script (F6)"));
        Tooltip.install(restartAction, new Tooltip("Restart snap Script (F7)"));
        Tooltip.install(stopAction, new Tooltip("Stop snap Script (F8)"));
        Tooltip.install(loadAction, new Tooltip("Load snap file (F9)"));
        Tooltip.install(saveAction, new Tooltip("Save snap File (F10)"));
        Tooltip.install(cursorAction, new Tooltip("Show cursor Position (F11)"));
        Tooltip.install(clearAction, new Tooltip("Clear debug TextArea (F12)"));
        Tooltip.install(themeAction, new Tooltip("Change Editor Theme (F1)"));
    }

    private void setupViewsListeners(){
        runAction.setOnMouseClicked(this::runSnapScript);
        debugAction.setOnMouseClicked(this::runSnapScriptDebugger);
        restartAction.setOnMouseClicked(this::restartSnapScript);
        stopAction.setOnMouseClicked(this::stopSnapScript);
        loadAction.setOnMouseClicked(this::loadSnapScript);
        saveAction.setOnMouseClicked(this::saveSnapScript);
        cursorAction.setOnMouseClicked(this::showCursorPosition);
        clearAction.setOnMouseClicked(this::clearDebugArea);
        themeAction.setOnMouseClicked(this::changeEditorTheme);
    }

    private void setupResultTextArea(){
        resultTextArea.setEditable(false);
    }

    private void setupSnapScriptTabsLayout(){
        scriptTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        scriptTabPane.setOnDragDropped(this::onCodeLayoutDragDropped);
        scriptTabPane.setOnDragOver(this::onScriptLayoutDragOver);
        scriptTabPane.getSelectionModel().selectedItemProperty().addListener(onTabSelectChangeListener);
    }

    private void onCodeLayoutDragDropped(DragEvent event) {
        List<File> currentDropped = event.getDragboard().getFiles();
        for (File file : currentDropped) {
            String fileName = file.getName();
            if(fileName.endsWith(".ss")) {
                executorService.execute(() -> openSnapScriptNewTab(file));
            }
        }
    }

    private void onScriptLayoutDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    private void openSnapScriptNewTab(File snapFile){
        if(snapFile == null || snapFile.getName().isEmpty()) return;

        Tab snapScriptTab = new Tab(snapFile.getName());
        snapScriptTab.setUserData(snapFile.getPath());

        CodeArea snapCodeArea = new CodeArea();
        SnapEditor editorController = new SnapEditor(snapCodeArea, resultTextArea);
        editorController.editorSettings();

        try{
            StringBuilder code = new StringBuilder();
            Files.readAllLines(snapFile.toPath(), Charset.defaultCharset()).forEach(s -> code.append(s).append("\n"));
            snapCodeArea.replaceText(0, 0, code.toString());

            snapScriptTab.setContent(new VirtualizedScrollPane<>(snapCodeArea));

            Platform.runLater(() -> scriptTabPane.getTabs().add(snapScriptTab));
            editorController.updateSourceFile(snapFile);
        }catch (IOException e){
            DialogUtils.createErrorDialog("Error", null, "Invalid Snap Script file");
        }
    }

    private void runSnapScript(MouseEvent...event){
        resultTextArea.clear();
        if(currentCodeArea == null){
            DialogUtils.createErrorDialog("Error Message",
                    null, "Select Snap script");
            return;
        }
        String scriptText = currentCodeArea.getText();
        snapRuntime.removeDebuggerListener();
        currentFutureTask = executorService.submit(() -> {
            snapRuntime.runSnapCode(scriptText);
            Platform.runLater(() -> resultTextArea.appendText("\nScript is executed\n"));
        });
    }

    private void runSnapScriptDebugger(MouseEvent...event){
        resultTextArea.clear();
        if(currentCodeArea == null){
            DialogUtils.createErrorDialog("Error Message",
                    null, "Select Snap script");
            return;
        }
        String scriptText = currentCodeArea.getText();
        snapRuntime.setDebuggerListener(mDebuggerListener);
        currentFutureTask = executorService.submit(() -> {
            snapRuntime.runSnapCode(scriptText);
            Platform.runLater(() -> resultTextArea.appendText("\nScript is executed\n"));
        });
    }

    private void restartSnapScript(MouseEvent...event){
        resultTextArea.clear();
        if(currentCodeArea == null){
            DialogUtils.createErrorDialog("Error Message",
                    null, "Select Snap script");
            return;
        }
        String scriptText = currentCodeArea.getText();
        currentFutureTask = executorService.submit(() -> {
            snapRuntime.runSnapCode(scriptText);
            Platform.runLater(() -> resultTextArea.appendText("\nScript is executed\n"));
        });
    }

    private void loadSnapScript(MouseEvent...event){
        File snapScript = FileManager.openSourceFile("Load Snap Script");
        openSnapScriptNewTab(snapScript);
    }

    private void stopSnapScript(MouseEvent...event){
        if(currentCodeArea == null){
            DialogUtils.createErrorDialog("Error Message",
                    null, "Select Snap script");
            return;
        }
        currentFutureTask.cancel(true);
    }

    private void saveSnapScript(MouseEvent...event){
        int selectedIndex = scriptTabPane.getSelectionModel().getSelectedIndex();
        if(selectedIndex == -1){
            DialogUtils.createErrorDialog("Error Message",
                    "Invalid File", "Select Snap script");
            return;
        }

        String scriptText = currentCodeArea.getText();
        Tab currentTab = scriptTabPane.getTabs().get(selectedIndex);
        String scriptFilePath = currentTab.getUserData().toString();
        FileManager.updateContent(new File(scriptFilePath), scriptText);
    }

    private void showCursorPosition(MouseEvent...event){
        showCursorPosition = !showCursorPosition;
        if(showCursorPosition) {
            logger.info("Start showing Cursor position");
            final Point[] currentPoint = {null};
            currentFutureTask = executorService.submit(() -> {
                while (showCursorPosition) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Point position = CursorManager.getCursorPosition();
                    if(!position.equals(currentPoint[0])) {
                        Platform.runLater(() -> resultTextArea.appendText("X:" + position.x + "\tY:" + position.y + "\n"));
                        currentPoint[0] = position;
                    }
                }
            });
        }else{
            logger.info("Stop showing Cursor position");
            currentFutureTask.cancel(false);
        }
    }

    private void changeEditorTheme(MouseEvent...event){
        Settings settings = new Settings();
        Theme theme = settings.getThemeEnum();
        theme = (theme == Theme.DARK) ? Theme.WHITE : Theme.DARK;
        settings.setTheme(theme);
    }

    private void clearDebugArea(MouseEvent...event){
        resultTextArea.clear();
    }

    private final ChangeListener<Tab> onTabSelectChangeListener = (observable, oldVal, newVal) -> {
        if (Objects.nonNull(newVal)) {
            if (newVal.getText().endsWith(".ss")) {
                currentCodeArea = (CodeArea) ((Parent) newVal.getContent()).getChildrenUnmodifiable().get(0);
            }
        }
    };

    private final EventHandler<KeyEvent> mShortcutListener = (event) -> {
        KeyCode keyCode = event.getCode();
        switch (keyCode){
            case F1:
                changeEditorTheme();
                break;
            case F5:
                runSnapScript();
                break;
            case F6:
                runSnapScriptDebugger();
                break;
            case F7:
                restartSnapScript();
                break;
            case F8:
                stopSnapScript();
                break;
            case F9:
                loadSnapScript();
                break;
            case F10:
                saveSnapScript();
                break;
            case F11:
                showCursorPosition();
                break;
            case F12:
                clearDebugArea();
                break;
        }
    };

    private final StreamListener mStreamListener = (message) -> {
        resultTextArea.appendText(message);
    };

    private final DebuggerListener mDebuggerListener = (message, type) -> {
        resultTextArea.appendText(message);
    };
}