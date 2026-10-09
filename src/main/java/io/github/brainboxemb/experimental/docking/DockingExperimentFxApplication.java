package io.github.brainboxemb.experimental.docking;

import com.pixelduke.transit.Style;
import com.pixelduke.transit.TransitStyleClass;
import com.pixelduke.transit.TransitTheme;
import io.github.brainboxemb.experimental.docking.workbench.PanelCatalog;
import io.github.brainboxemb.experimental.docking.workbench.SnapFxWorkbench;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.snapfx.persistence.DockLayoutLoadException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

/**
 * JavaFX lifecycle class for the docking experiment.
 *
 * <p>Use {@link DockingExperimentApplication} as the executable main class.</p>
 */
public final class DockingExperimentFxApplication extends Application {

    private static final Style THEME_STYLE =
            Style.LIGHT;

    @Override
    public void start(Stage stage) {
        showCandidate(stage);
    }

    static void showCandidate(Stage stage) {
        PanelCatalog catalog = new PanelCatalog();
        SnapFxWorkbench workbench =
                new SnapFxWorkbench(
                        catalog,
                        Path.of("layout.json"));

        URL applicationStylesheet =
                DockingExperimentFxApplication.class
                        .getResource("/experiment.css");

        workbench.setSceneDecorator(
                scene -> decorateShowingScene(
                        scene,
                        applicationStylesheet));

        Label status = new Label(
                "Default experiment layout");

        Button saveLayout =
                new Button("Save layout");
        saveLayout.setOnAction(event ->
                saveLayout(workbench, status));

        Button loadLayout =
                new Button("Load layout");
        loadLayout.setOnAction(event ->
                loadLayout(workbench, status));

        Region spacer = new Region();
        HBox.setHgrow(
                spacer,
                Priority.ALWAYS);

        HBox toolbar = new HBox(
                6,
                saveLayout,
                loadLayout,
                spacer,
                status);
        toolbar.setPadding(
                new Insets(4, 6, 4, 6));
        toolbar.getStyleClass().add(
                "experiment-toolbar");

        BorderPane root = new BorderPane();
        root.getStyleClass().add(
                TransitStyleClass.BACKGROUND);
        root.setTop(toolbar);
        root.setCenter(
                workbench.buildLayout());

        Scene scene = new Scene(
                root,
                1280,
                820);

        /*
         * Prepare CSS before the Stage owns the Scene. TransitTheme itself
         * also touches the native Windows frame, which is only safe after
         * Stage.show() has created the JavaFX native peer.
         */
        prepareScene(
                scene,
                applicationStylesheet);

        stage.setTitle(
                "JavaFX Docking Experiment — SnapFX + Transit");
        stage.setScene(scene);

        /*
         * Keep SnapFX's documented lifecycle order:
         * setScene -> initialize -> show.
         */
        workbench.initialize(stage);
        stage.show();

        /*
         * Apply Transit after show so Windows native-frame theming sees a
         * valid tkStage/native handle.
         */
        decorateShowingScene(
                scene,
                applicationStylesheet);
    }

    private static void prepareScene(
            Scene scene,
            URL applicationStylesheet) {
        scene.setUserAgentStylesheet(
                THEME_STYLE.getStyleStylesheetURL());

        if (applicationStylesheet == null) {
            return;
        }

        String stylesheet =
                applicationStylesheet.toExternalForm();

        if (!scene.getStylesheets().contains(stylesheet)) {
            scene.getStylesheets().add(stylesheet);
        }
    }

    private static void decorateShowingScene(
            Scene scene,
            URL applicationStylesheet) {
        prepareScene(
                scene,
                applicationStylesheet);

        if (scene.getWindow() == null
                || !scene.getWindow().isShowing()) {
            return;
        }

        new TransitTheme(
                scene,
                THEME_STYLE);
    }

    private static void saveLayout(
            SnapFxWorkbench workbench,
            Label status) {
        try {
            workbench.saveLayout();
            status.setText(
                    "Saved " + workbench.layoutFile());
        } catch (IOException exception) {
            status.setText(
                    "Save failed: "
                    + exception.getMessage());
        }
    }

    private static void loadLayout(
            SnapFxWorkbench workbench,
            Label status) {
        try {
            if (workbench.loadLayout()) {
                status.setText(
                        "Loaded "
                        + workbench.layoutFile());
            } else {
                status.setText(
                        "No saved layout yet");
            }
        } catch (IOException
                | DockLayoutLoadException exception) {
            status.setText(
                    "Load failed: "
                    + exception.getMessage());
        }
    }
}
