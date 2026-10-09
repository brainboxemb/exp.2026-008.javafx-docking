package io.github.brainboxemb.experimental.docking.workbench;

import javafx.geometry.Orientation;
import javafx.geometry.Side;
import software.coley.bentofx.Bento;
import software.coley.bentofx.building.DockBuilding;
import software.coley.bentofx.dockable.Dockable;
import software.coley.bentofx.layout.container.DockContainerBranch;
import software.coley.bentofx.layout.container.DockContainerLeaf;
import software.coley.bentofx.layout.container.DockContainerRootBranch;

/**
 * BentoFX implementation of the representative experiment workbench.
 *
 * <p>The shared panels remain ordinary JavaFX nodes. BentoFX-specific
 * root/branch/leaf structure is isolated here.</p>
 */
public final class BentoFxWorkbench {

    private static final int WORKBENCH_DRAG_GROUP =
            1;

    private final PanelCatalog catalog;
    private final Bento bento =
            new Bento();
    private final DockContainerRootBranch root;

    public BentoFxWorkbench(
            PanelCatalog catalog) {
        this.catalog = catalog;

        bento.stageBuilding()
                .setApplySourceAsOwner(false);
        bento.stageBuilding()
                .setApplyMousePosition(true);

        bento.controlsBuilding()
                .setHeaderFactory(
                        (dockable, parentPane) ->
                                new CompactBentoHeader(
                                        dockable,
                                        parentPane)
                                        .withDragDrop());

        DockBuilding builder =
                bento.dockBuilding();

        root = builder.root("root");

        DockContainerBranch leftColumn =
                builder.branch("left-column");
        DockContainerBranch rightColumn =
                builder.branch("right-column");

        DockContainerLeaf controls =
                builder.leaf("controls");
        DockContainerLeaf deviceLog =
                builder.leaf("device-log");
        DockContainerLeaf terminal =
                builder.leaf("terminal");
        DockContainerLeaf clientLog =
                builder.leaf("client-log");

        DockContainerLeaf registrations =
                builder.leaf("registrations");
        DockContainerLeaf logBook =
                builder.leaf("logbook");
        DockContainerLeaf rawData =
                builder.leaf("raw-data");

        root.setOrientation(
                Orientation.HORIZONTAL);
        leftColumn.setOrientation(
                Orientation.VERTICAL);
        rightColumn.setOrientation(
                Orientation.VERTICAL);

        root.addContainers(
                leftColumn,
                rightColumn);

        leftColumn.addContainers(
                controls,
                deviceLog,
                terminal,
                clientLog);

        rightColumn.addContainers(
                registrations,
                logBook,
                rawData);

        configureLeaf(controls);
        configureLeaf(deviceLog);
        configureLeaf(terminal);
        configureLeaf(clientLog);
        configureLeaf(registrations);
        configureLeaf(logBook);
        configureLeaf(rawData);

        leftColumn.setPruneWhenEmpty(false);
        rightColumn.setPruneWhenEmpty(false);

        /*
         * Match the current SnapFX comparison workspace as closely as
         * practical: roughly equal columns, with more simultaneously visible
         * tool/log areas on the left and three stacked data areas on the right.
         */
        root.setDividerPositions(
                0.50);
        leftColumn.setDividerPositions(
                0.46,
                0.64,
                0.82);
        rightColumn.setDividerPositions(
                0.46,
                0.76);

        controls.addDockables(
                dockable(
                        builder,
                        PanelCatalog.TIMING_NODE),
                dockable(
                        builder,
                        PanelCatalog.REGISTRATION),
                dockable(
                        builder,
                        PanelCatalog.SIMULATION));

        deviceLog.addDockable(
                dockable(
                        builder,
                        PanelCatalog.DEVICE_LOG));

        terminal.addDockable(
                dockable(
                        builder,
                        PanelCatalog.TERMINAL));

        clientLog.addDockable(
                dockable(
                        builder,
                        PanelCatalog.CLIENT_LOG));

        registrations.addDockable(
                dockable(
                        builder,
                        PanelCatalog.REGISTRATIONS));

        logBook.addDockable(
                dockable(
                        builder,
                        PanelCatalog.LOGBOOK));

        rawData.addDockable(
                dockable(
                        builder,
                        PanelCatalog.RAW_DATA));
    }

    public DockContainerRootBranch root() {
        return root;
    }

    private static void configureLeaf(
            DockContainerLeaf leaf) {
        leaf.setSide(
                Side.TOP);

        /*
         * Keep BentoFX's default pruneWhenEmpty=true. When the last dockable
         * moves out of a panel area, the empty leaf should disappear and the
         * parent split should immediately redistribute the freed space.
         */
    }

    private Dockable dockable(
            DockBuilding builder,
            String panelId) {
        WorkbenchPanel panel =
                catalog.get(panelId);

        Dockable dockable =
                builder.dockable(panel.id());
        dockable.setTitle(
                panel.title());
        dockable.setNode(
                panel.createContent());
        dockable.setDragGroupMask(
                WORKBENCH_DRAG_GROUP);

        return dockable;
    }
}
