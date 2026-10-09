package io.github.brainboxemb.experimental.docking;

import javafx.application.Application;

/**
 * Stable plain-Java entry point for the docking experiment.
 *
 * <p>This class deliberately does not extend {@link Application}. IDEs such as
 * NetBeans and Maven can therefore discover and invoke it as a normal main
 * class without relying on the JVM's special JavaFX launcher path.</p>
 */
public final class DockingExperimentApplication {

    private DockingExperimentApplication() {
    }

    public static void main(String[] args) {
        Application.launch(
                DockingExperimentFxApplication.class,
                args);
    }
}
