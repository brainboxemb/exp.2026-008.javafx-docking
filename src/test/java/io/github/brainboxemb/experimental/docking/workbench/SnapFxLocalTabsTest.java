package io.github.brainboxemb.experimental.docking.workbench;

import javafx.scene.Group;
import org.junit.jupiter.api.Test;
import org.snapfx.model.DockGraph;
import org.snapfx.model.DockNode;
import org.snapfx.model.DockPosition;
import org.snapfx.model.DockSplitPane;
import org.snapfx.model.DockTabPane;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class SnapFxLocalTabsTest {

    @Test
    void controlTabsStayInsideTheirLocalSplitArea() {
        DockGraph graph =
                new DockGraph();

        DockNode timingNode =
                node(PanelCatalog.TIMING_NODE);
        DockNode registration =
                node(PanelCatalog.REGISTRATION);
        DockNode simulation =
                node(PanelCatalog.SIMULATION);
        DockNode registrations =
                node(PanelCatalog.REGISTRATIONS);
        DockNode terminal =
                node(PanelCatalog.TERMINAL);

        graph.setRoot(timingNode);

        /*
         * Establish the surrounding workspace first.
         */
        graph.dock(
                registrations,
                timingNode,
                DockPosition.RIGHT);
        graph.dock(
                terminal,
                timingNode,
                DockPosition.BOTTOM);

        /*
         * Then replace only the TimingNode leaf with a local tab pane.
         */
        graph.dock(
                registration,
                timingNode,
                DockPosition.CENTER);
        graph.dock(
                simulation,
                timingNode,
                DockPosition.CENTER);

        DockSplitPane root =
                assertInstanceOf(
                        DockSplitPane.class,
                        graph.getRoot());

        DockTabPane controlTabs =
                assertInstanceOf(
                        DockTabPane.class,
                        timingNode.getParent());

        assertSame(
                controlTabs,
                registration.getParent());
        assertSame(
                controlTabs,
                simulation.getParent());

        /*
         * The tab group must remain nested in the split tree instead of
         * becoming the workbench root and spanning every panel.
         */
        assertNotSame(
                root,
                controlTabs);
        assertInstanceOf(
                DockSplitPane.class,
                controlTabs.getParent());
    }

    private static DockNode node(
            String id) {
        return new DockNode(
                id,
                new Group(),
                id);
    }
}
