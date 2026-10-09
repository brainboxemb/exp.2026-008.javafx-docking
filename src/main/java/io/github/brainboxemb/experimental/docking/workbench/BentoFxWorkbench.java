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

        DockBuilding builder =
                bento.dockBuilding();

        root = builder.root("root");

        DockContainerBranch workspace =
                builder.branch("workspace");
        DockContainerBranch dataArea =
                builder.branch("data-area");

        DockContainerLeaf controls =
                builder.leaf("controls");
        DockContainerLeaf registrations =
                builder.leaf("registrations");
        DockContainerLeaf detail =
                builder.leaf("detail");
        DockContainerLeaf logs =
                builder.leaf("logs");

        root.setOrientation(
                Orientation.VERTICAL);
        workspace.setOrientation(
                Orientation.HORIZONTAL);
        dataArea.setOrientation(
                Orientation.VERTICAL);

        root.addContainers(
                workspace,
                logs);
        workspace.addContainers(
                controls,
                dataArea);
        dataArea.addContainers(
                registrations,
                detail);

        controls.setSide(
                Side.TOP);
        registrations.setSide(
                Side.TOP);
        detail.setSide(
                Side.TOP);
        logs.setSide(
                Side.TOP);

        controls.setPruneWhenEmpty(false);
        registrations.setPruneWhenEmpty(false);
        detail.setPruneWhenEmpty(false);
        logs.setPruneWhenEmpty(false);
        workspace.setPruneWhenEmpty(false);
        dataArea.setPruneWhenEmpty(false);

        DockContainerBranch.setResizableWithParent(
                controls,
                false);
        DockContainerBranch.setResizableWithParent(
                logs,
                false);

        root.setContainerSizePx(
                logs,
                240);
        workspace.setContainerSizePx(
                controls,
                320);
        dataArea.setContainerSizePx(
                detail,
                300);

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

        registrations.addDockable(
                dockable(
                        builder,
                        PanelCatalog.REGISTRATIONS));

        detail.addDockables(
                dockable(
                        builder,
                        PanelCatalog.LOGBOOK),
                dockable(
                        builder,
                        PanelCatalog.TAG_PLOT));

        logs.addDockables(
                dockable(
                        builder,
                        PanelCatalog.TERMINAL),
                dockable(
                        builder,
                        PanelCatalog.DEVICE_LOG),
                dockable(
                        builder,
                        PanelCatalog.CLIENT_LOG));
    }

    public DockContainerRootBranch root() {
        return root;
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
