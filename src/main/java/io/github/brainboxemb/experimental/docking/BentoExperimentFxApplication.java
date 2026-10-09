package io.github.brainboxemb.experimental.docking;

import com.pixelduke.transit.Style;
import com.pixelduke.transit.TransitStyleClass;
import com.pixelduke.transit.TransitTheme;
import io.github.brainboxemb.experimental.docking.workbench.BentoFxWorkbench;
import io.github.brainboxemb.experimental.docking.workbench.PanelCatalog;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import software.coley.bentofx.layout.container.DockContainerLeaf;

import java.net.URL;

/**
 * JavaFX lifecycle for the BentoFX candidate.
 */
public final class BentoExperimentFxApplication extends Application {

    private static final Style THEME_STYLE =
            Style.LIGHT;

    @Override
    public void start(Stage stage) {
        showCandidate(stage);
    }

    static void showCandidate(Stage stage) {
        JavaFxRuntimeDiagnostics.log(
                "before BentoFX workbench",
                DockContainerLeaf.class);

        PanelCatalog catalog = new PanelCatalog();
        BentoFxWorkbench workbench =
                new BentoFxWorkbench(catalog);

        Scene scene = new Scene(
                workbench.root(),
                1280,
                820);

        URL applicationStylesheet =
                BentoExperimentFxApplication.class
                        .getResource("/experiment.css");
        URL bentoStylesheet =
                BentoExperimentFxApplication.class
                        .getResource("/bento-experiment.css");

        prepareScene(
                scene,
                applicationStylesheet,
                bentoStylesheet);

        scene.getRoot().getStyleClass().add(
                TransitStyleClass.BACKGROUND);

        stage.setTitle(
                "JavaFX Docking Experiment — BentoFX + Transit");
        stage.setScene(scene);
        stage.show();

        new TransitTheme(
                scene,
                THEME_STYLE);
    }

    private static void prepareScene(
            Scene scene,
            URL applicationStylesheet,
            URL bentoStylesheet) {
        scene.setUserAgentStylesheet(
                THEME_STYLE.getStyleStylesheetURL());

        addStylesheet(
                scene,
                applicationStylesheet);
        addStylesheet(
                scene,
                bentoStylesheet);
    }

    private static void addStylesheet(
            Scene scene,
            URL stylesheetUrl) {
        if (stylesheetUrl == null) {
            return;
        }

        String stylesheet =
                stylesheetUrl.toExternalForm();

        if (!scene.getStylesheets().contains(stylesheet)) {
            scene.getStylesheets().add(stylesheet);
        }
    }
}
