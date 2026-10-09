package io.github.brainboxemb.experimental.docking.workbench;

import io.github.brainboxemb.experimental.docking.view.LogBookPane;
import io.github.brainboxemb.experimental.docking.view.RegistrationsPane;
import io.github.brainboxemb.experimental.docking.view.TagPlotPane;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Creates representative Development Client panels without depending on a docking framework.
 */
public final class PanelCatalog {

    public static final String TIMING_NODE = "timing-node";
    public static final String REGISTRATION = "registration";
    public static final String SIMULATION = "simulation";
    public static final String TERMINAL = "terminal";
    public static final String DEVICE_LOG = "device-log";
    public static final String CLIENT_LOG = "client-log";
    public static final String REGISTRATIONS = "registrations";
    public static final String LOGBOOK = "logbook";
    public static final String TAG_PLOT = "tag-plot";

    private final Map<String, WorkbenchPanel> panels = new LinkedHashMap<>();

    public PanelCatalog() {
        register(TIMING_NODE, "TimingNode", () -> controlsPane(
                "TimingNode",
                "Representative timing-node controls",
                "Open",
                "Close"));
        register(REGISTRATION, "Registration", () -> controlsPane(
                "Registration",
                "Representative registration controls",
                "AUTO",
                "MAN"));
        register(SIMULATION, "Simulation", () -> controlsPane(
                "Simulation",
                "Representative simulation controls",
                "Start",
                "Stop"));
        register(TERMINAL, "Terminal", () -> logPane(
                "event-timing> help\nCommands: help, status, connect, disconnect\n"));
        register(DEVICE_LOG, "Device Log", () -> logPane(
                "20:12:01.120 - [INFO] - Device connection placeholder\n"
                        + "20:12:01.431 - [INFO] - Antenna 1 ready\n"));
        register(CLIENT_LOG, "Client Log", () -> logPane(
                "20:12:00.910 - [INFO] - Development Client started\n"
                        + "20:12:01.002 - [INFO] - Workbench ready\n"));
        register(
                REGISTRATIONS,
                "Registrations",
                RegistrationsPane::new);
        register(
                LOGBOOK,
                "LogBook",
                LogBookPane::new);
        register(TAG_PLOT, "Tag Plot", TagPlotPane::new);
    }

    public WorkbenchPanel get(String id) {
        WorkbenchPanel panel = panels.get(id);
        if (panel == null) {
            throw new IllegalArgumentException("Unknown workbench panel: " + id);
        }
        return panel;
    }

    public WorkbenchPanel find(String id) {
        return panels.get(id);
    }

    private void register(String id, String title, java.util.function.Supplier<Node> contentFactory) {
        panels.put(id, new WorkbenchPanel(id, title, contentFactory));
    }

    private static Node controlsPane(
            String title,
            String description,
            String firstAction,
            String secondAction) {
        Label heading = new Label(title);
        heading.getStyleClass().add("experiment-panel-heading");

        HBox actions = new HBox(
                6,
                new Button(firstAction),
                new Button(secondAction));

        VBox box = new VBox(
                8,
                heading,
                new Label(description),
                actions);
        box.getStyleClass().add("experiment-tool-panel");
        return box;
    }

    private static Node logPane(String initialText) {
        TextArea area = new TextArea(initialText);
        area.setEditable(false);
        area.setWrapText(false);
        area.getStyleClass().add("experiment-log");
        return area;
    }

}
