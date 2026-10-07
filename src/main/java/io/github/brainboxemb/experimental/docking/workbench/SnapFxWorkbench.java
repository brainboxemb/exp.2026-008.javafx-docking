package io.github.brainboxemb.experimental.docking.workbench;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.snapfx.SnapFX;
import org.snapfx.floating.DockFloatingWindow;
import org.snapfx.model.DockGraph;
import org.snapfx.model.DockNode;
import org.snapfx.model.DockPosition;
import org.snapfx.persistence.DockLayoutLoadException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Thin adapter between the experiment's panel model and SnapFX.
 *
 * <p>Panel implementations stay unaware of SnapFX. Scene decoration is exposed
 * as a generic JavaFX hook so an application theme can also be applied to
 * floating windows without exposing SnapFX window types outside this adapter.</p>
 */
public final class SnapFxWorkbench {

    private final PanelCatalog catalog;
    private final Path layoutFile;
    private final SnapFX snapFX = new SnapFX();

    private Consumer<Scene> sceneDecorator =
            scene -> { };

    public SnapFxWorkbench(
            PanelCatalog catalog,
            Path layoutFile) {
        this.catalog = catalog;
        this.layoutFile = layoutFile;

        snapFX.setNodeFactory(this::createDockNode);
        snapFX.getFloatingWindows().addListener(
                (ListChangeListener<DockFloatingWindow>)
                        this::floatingWindowsChanged);

        createDefaultLayout();
    }

    public Parent buildLayout() {
        return snapFX.buildLayout();
    }

    /**
     * Sets the application-owned decoration applied to every workbench scene.
     *
     * <p>This is deliberately expressed in terms of JavaFX {@link Scene}; the
     * caller does not need to know how SnapFX represents floating windows.</p>
     */
    public void setSceneDecorator(
            Consumer<Scene> sceneDecorator) {
        this.sceneDecorator =
                Objects.requireNonNull(
                        sceneDecorator,
                        "sceneDecorator");
    }

    public void initialize(Stage stage) {
        snapFX.initialize(stage);

        decorateScene(stage.getScene());

        for (DockFloatingWindow floatingWindow
                : snapFX.getFloatingWindows()) {
            decorateFloatingWindow(floatingWindow);
        }
    }

    public Path layoutFile() {
        return layoutFile;
    }

    public void saveLayout() throws IOException {
        Files.writeString(
                layoutFile,
                snapFX.saveLayout());
    }

    public boolean loadLayout()
            throws IOException, DockLayoutLoadException {
        if (!Files.isRegularFile(layoutFile)) {
            return false;
        }

        snapFX.loadLayout(
                Files.readString(layoutFile));
        return true;
    }

    private void floatingWindowsChanged(
            ListChangeListener.Change
                    <? extends DockFloatingWindow> change) {
        while (change.next()) {
            if (!change.wasAdded()) {
                continue;
            }

            for (DockFloatingWindow floatingWindow
                    : change.getAddedSubList()) {
                /*
                 * SnapFX adds the floating-window model before it calls show().
                 * Run on the next JavaFX pulse so the new Stage already owns
                 * its Scene.
                 */
                Platform.runLater(
                        () -> decorateFloatingWindow(
                                floatingWindow));
            }
        }
    }

    private void decorateFloatingWindow(
            DockFloatingWindow floatingWindow) {
        decorateScene(floatingWindow.getScene());
    }

    private void decorateScene(Scene scene) {
        if (scene != null) {
            sceneDecorator.accept(scene);
        }
    }

    private void createDefaultLayout() {
        DockGraph graph = snapFX.getDockGraph();

        DockNode timingNode =
                createDockNode(PanelCatalog.TIMING_NODE);
        DockNode registration =
                createDockNode(PanelCatalog.REGISTRATION);
        DockNode simulation =
                createDockNode(PanelCatalog.SIMULATION);
        DockNode terminal =
                createDockNode(PanelCatalog.TERMINAL);
        DockNode deviceLog =
                createDockNode(PanelCatalog.DEVICE_LOG);
        DockNode clientLog =
                createDockNode(PanelCatalog.CLIENT_LOG);
        DockNode registrations =
                createDockNode(PanelCatalog.REGISTRATIONS);
        DockNode logBook =
                createDockNode(PanelCatalog.LOGBOOK);
        DockNode tagPlot =
                createDockNode(PanelCatalog.TAG_PLOT);

        graph.setRoot(timingNode);

        graph.dock(
                registration,
                timingNode,
                DockPosition.CENTER);
        graph.dock(
                simulation,
                timingNode,
                DockPosition.CENTER);

        graph.dock(
                registrations,
                timingNode,
                DockPosition.RIGHT);

        graph.dock(
                terminal,
                timingNode,
                DockPosition.BOTTOM);
        graph.dock(
                deviceLog,
                terminal,
                DockPosition.CENTER);
        graph.dock(
                clientLog,
                terminal,
                DockPosition.CENTER);

        graph.dock(
                logBook,
                registrations,
                DockPosition.BOTTOM);
        graph.dock(
                tagPlot,
                logBook,
                DockPosition.CENTER);

        snapFX.setRootSplitRatios(
                0.44,
                0.56);
    }

    private DockNode createDockNode(String id) {
        WorkbenchPanel panel = catalog.find(id);
        if (panel == null) {
            return null;
        }

        return new DockNode(
                panel.id(),
                panel.createContent(),
                panel.title());
    }
}
