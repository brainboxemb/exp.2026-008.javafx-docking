package io.github.brainboxemb.experimental.docking;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.snapfx.SnapFX;

public final class DockingExperimentApplication extends Application {

    @Override
    public void start(Stage stage) {
        SnapFX snapFX = new SnapFX();

        snapFX.dock(toolPanel("TimingNode", "Timing node controls"), "TimingNode");
        snapFX.dock(toolPanel("Registrations", "Registration table placeholder"), "Registrations");
        snapFX.dock(logPanel("Terminal"), "Terminal");
        snapFX.dock(logPanel("Device Log"), "Device Log");
        snapFX.dock(logPanel("Client Log"), "Client Log");
        snapFX.dock(toolPanel("LogBook", "LogBook table placeholder"), "LogBook");
        snapFX.dock(toolPanel("Tag Plot", "Realtime tag plot placeholder"), "Tag Plot");

        BorderPane root = new BorderPane(snapFX.buildLayout());
        Scene scene = new Scene(root, 1280, 820);
        new JMetro(Style.LIGHT).setScene(scene);

        stage.setTitle("JavaFX Docking Experiment — SnapFX");
        stage.setScene(scene);
        snapFX.initialize(stage);
        stage.show();
    }

    private static VBox toolPanel(String title, String text) {
        VBox panel = new VBox(8, new Label(title), new Label(text));
        panel.setStyle("-fx-padding: 12;");
        return panel;
    }

    private static TextArea logPanel(String title) {
        TextArea area = new TextArea(title + "\n");
        area.setEditable(false);
        area.setStyle(
                "-fx-font-family: 'Consolas';"
                + "-fx-control-inner-background: #111418;"
                + "-fx-text-fill: #e8e8e8;");
        return area;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
