package io.github.brainboxemb.experimental.docking;

import com.pixelduke.transit.Style;
import com.pixelduke.transit.TransitStyleClass;
import com.pixelduke.transit.TransitTheme;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.function.Consumer;

/**
 * Default experiment launcher used by NetBeans Run/Debug Project.
 */
public final class DockingExperimentChooserFxApplication
        extends Application {

    private static final Style THEME_STYLE =
            Style.LIGHT;

    @Override
    public void start(Stage stage) {
        JavaFxRuntimeDiagnostics.log(
                "chooser startup");

        Label heading =
                new Label("Choose docking candidate");
        heading.getStyleClass().add(
                "experiment-chooser-heading");

        Label introduction =
                new Label(
                        "Run the same representative workbench "
                        + "with a different docking library.");
        introduction.setWrapText(true);

        VBox snapFxChoice =
                candidateChoice(
                        "SnapFX",
                        "Graph-oriented docking with built-in "
                        + "JSON layout persistence.",
                        event -> openCandidate(
                                stage,
                                DockingExperimentFxApplication
                                        ::showCandidate));

        VBox bentoChoice =
                candidateChoice(
                        "BentoFX",
                        "IDE-style root/branch/leaf model with "
                        + "normal Maven Central dependency.",
                        event -> openCandidate(
                                stage,
                                BentoExperimentFxApplication
                                        ::showCandidate));

        VBox root =
                new VBox(
                        14,
                        heading,
                        introduction,
                        snapFxChoice,
                        bentoChoice);
        root.setAlignment(Pos.TOP_LEFT);
        root.setPadding(
                new Insets(18));
        root.setPrefWidth(430);
        root.getStyleClass().addAll(
                TransitStyleClass.BACKGROUND,
                "experiment-chooser");

        Scene scene =
                new Scene(root);

        URL applicationStylesheet =
                DockingExperimentChooserFxApplication.class
                        .getResource("/experiment.css");

        scene.setUserAgentStylesheet(
                THEME_STYLE.getStyleStylesheetURL());

        if (applicationStylesheet != null) {
            scene.getStylesheets().add(
                    applicationStylesheet.toExternalForm());
        }

        stage.setTitle(
                "JavaFX Docking Experiment");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        new TransitTheme(
                scene,
                THEME_STYLE);
    }

    private static VBox candidateChoice(
            String title,
            String description,
            javafx.event.EventHandler<javafx.event.ActionEvent>
                    action) {
        Button button =
                new Button(title);
        button.setMaxWidth(
                Double.MAX_VALUE);
        button.setOnAction(action);

        Label descriptionLabel =
                new Label(description);
        descriptionLabel.setWrapText(true);
        descriptionLabel.getStyleClass().add(
                "experiment-chooser-description");

        VBox choice =
                new VBox(
                        4,
                        button,
                        descriptionLabel);
        VBox.setVgrow(
                button,
                Priority.NEVER);
        choice.getStyleClass().add(
                "experiment-chooser-choice");

        return choice;
    }

    private static void openCandidate(
            Stage chooserStage,
            Consumer<Stage> candidateStarter) {
        Stage candidateStage =
                new Stage();

        candidateStarter.accept(
                candidateStage);

        chooserStage.close();
    }
}
