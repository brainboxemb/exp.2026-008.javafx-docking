package io.github.brainboxemb.experimental.docking.workbench;

import javafx.scene.Node;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * Docking-framework-neutral description of one workbench panel.
 *
 * <p>The panel supplies ordinary JavaFX content. SnapFX-specific wrapping is
 * intentionally kept outside this class.</p>
 */
public record WorkbenchPanel(
        String id,
        String title,
        Supplier<Node> contentFactory) {

    public WorkbenchPanel {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(contentFactory, "contentFactory");
    }

    public Node createContent() {
        return contentFactory.get();
    }
}
