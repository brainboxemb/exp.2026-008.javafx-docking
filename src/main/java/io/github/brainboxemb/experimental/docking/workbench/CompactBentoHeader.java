package io.github.brainboxemb.experimental.docking.workbench;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;
import software.coley.bentofx.control.Header;
import software.coley.bentofx.control.HeaderPane;
import software.coley.bentofx.dockable.Dockable;

/**
 * BentoFX header with compact metrics for the desktop workbench.
 *
 * <p>BentoFX intentionally exposes a Header factory, so the experiment uses
 * that supported extension point instead of patching the library or relying
 * on private implementation details through reflection.</p>
 */
final class CompactBentoHeader extends Header {

    CompactBentoHeader(
            Dockable dockable,
            HeaderPane parentPane) {
        super(
                dockable,
                parentPane);

        compactHeaderGrid(this);
    }

    private static boolean compactHeaderGrid(
            Node node) {
        if (node instanceof GridPane grid) {
            grid.setPadding(
                    new Insets(
                            2,
                            6,
                            2,
                            6));
            grid.setHgap(4);
            grid.setVgap(4);
            return true;
        }

        if (node instanceof Parent parent) {
            for (Node child
                    : parent.getChildrenUnmodifiable()) {
                if (compactHeaderGrid(child)) {
                    return true;
                }
            }
        }

        return false;
    }
}
