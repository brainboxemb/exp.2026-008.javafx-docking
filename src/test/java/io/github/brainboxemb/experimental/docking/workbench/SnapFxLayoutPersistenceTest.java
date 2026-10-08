package io.github.brainboxemb.experimental.docking.workbench;

import javafx.scene.Group;
import org.junit.jupiter.api.Test;
import org.snapfx.model.DockContainer;
import org.snapfx.model.DockElement;
import org.snapfx.model.DockGraph;
import org.snapfx.model.DockNode;
import org.snapfx.model.DockPosition;
import org.snapfx.persistence.DockLayoutLoadException;
import org.snapfx.persistence.DockLayoutSerializer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapFxLayoutPersistenceTest {

    @Test
    void stablePanelIdsSurviveSaveLoadRoundTrip()
            throws DockLayoutLoadException {
        DockGraph sourceGraph = new DockGraph();

        DockNode timingNode =
                node(PanelCatalog.TIMING_NODE);
        DockNode registration =
                node(PanelCatalog.REGISTRATION);
        DockNode terminal =
                node(PanelCatalog.TERMINAL);
        DockNode deviceLog =
                node(PanelCatalog.DEVICE_LOG);
        DockNode registrations =
                node(PanelCatalog.REGISTRATIONS);

        sourceGraph.setRoot(timingNode);
        sourceGraph.dock(
                registration,
                timingNode,
                DockPosition.CENTER);
        sourceGraph.dock(
                registrations,
                timingNode,
                DockPosition.RIGHT);
        sourceGraph.dock(
                terminal,
                timingNode,
                DockPosition.BOTTOM);
        sourceGraph.dock(
                deviceLog,
                terminal,
                DockPosition.CENTER);

        DockLayoutSerializer sourceSerializer =
                new DockLayoutSerializer(sourceGraph);

        String json = sourceSerializer.serialize();

        assertTrue(json.contains(PanelCatalog.TIMING_NODE));
        assertTrue(json.contains(PanelCatalog.DEVICE_LOG));

        DockGraph restoredGraph = new DockGraph();
        DockLayoutSerializer restoredSerializer =
                new DockLayoutSerializer(restoredGraph);
        restoredSerializer.setNodeFactory(this::node);
        restoredSerializer.deserialize(json);

        List<String> expectedIds =
                List.of(
                                PanelCatalog.TIMING_NODE,
                                PanelCatalog.REGISTRATION,
                                PanelCatalog.REGISTRATIONS,
                                PanelCatalog.TERMINAL,
                                PanelCatalog.DEVICE_LOG)
                        .stream()
                        .sorted()
                        .toList();

        assertEquals(
                expectedIds,
                collectNodeIds(restoredGraph.getRoot()));

        assertInstanceOf(
                DockContainer.class,
                restoredGraph.getRoot());
    }

    private DockNode node(String id) {
        return new DockNode(
                id,
                new Group(),
                id);
    }

    private static List<String> collectNodeIds(
            DockElement element) {
        List<String> ids = new ArrayList<>();
        collectNodeIds(element, ids);
        ids.sort(String::compareTo);
        return ids;
    }

    private static void collectNodeIds(
            DockElement element,
            List<String> ids) {
        if (element instanceof DockNode node) {
            ids.add(node.getDockNodeId());
            return;
        }

        if (element instanceof DockContainer container) {
            for (DockElement child : container.getChildren()) {
                collectNodeIds(child, ids);
            }
        }
    }
}
