package io.github.brainboxemb.experimental.docking.workbench;

import javafx.scene.Parent;
import javafx.stage.Stage;
import org.snapfx.SnapFX;
import org.snapfx.model.DockGraph;
import org.snapfx.model.DockNode;
import org.snapfx.model.DockPosition;
import org.snapfx.persistence.DockLayoutLoadException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Thin adapter between the experiment's panel model and SnapFX.
 */
public final class SnapFxWorkbench {

    private final PanelCatalog catalog;
    private final Path layoutFile;
    private final SnapFX snapFX = new SnapFX();

    public SnapFxWorkbench(
            PanelCatalog catalog,
            Path layoutFile) {
        this.catalog = catalog;
        this.layoutFile = layoutFile;

        snapFX.setNodeFactory(this::createDockNode);
        createDefaultLayout();
    }

    public Parent buildLayout() {
        return snapFX.buildLayout();
    }

    public void initialize(Stage stage) {
        snapFX.initialize(stage);
    }

    public Path layoutFile() {
        return layoutFile;
    }

    public void saveLayout() throws IOException {
        Files.writeString(layoutFile, snapFX.saveLayout());
    }

    public boolean loadLayout()
            throws IOException, DockLayoutLoadException {
        if (!Files.isRegularFile(layoutFile)) {
            return false;
        }

        snapFX.loadLayout(Files.readString(layoutFile));
        return true;
    }

    private void createDefaultLayout() {
        DockGraph graph = snapFX.getDockGraph();

        DockNode timingNode = createDockNode(PanelCatalog.TIMING_NODE);
        DockNode registration = createDockNode(PanelCatalog.REGISTRATION);
        DockNode simulation = createDockNode(PanelCatalog.SIMULATION);
        DockNode terminal = createDockNode(PanelCatalog.TERMINAL);
        DockNode deviceLog = createDockNode(PanelCatalog.DEVICE_LOG);
        DockNode clientLog = createDockNode(PanelCatalog.CLIENT_LOG);
        DockNode registrations = createDockNode(PanelCatalog.REGISTRATIONS);
        DockNode logBook = createDockNode(PanelCatalog.LOGBOOK);
        DockNode tagPlot = createDockNode(PanelCatalog.TAG_PLOT);

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

        snapFX.setRootSplitRatios(0.44, 0.56);
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
