package io.github.brainboxemb.experimental.docking;

import javafx.application.Application;

import java.util.Locale;

/**
 * Stable plain-Java entry point for the docking experiment.
 *
 * <p>Without arguments this opens the candidate chooser. Pass {@code snapfx}
 * or {@code bentofx} to launch a candidate directly.</p>
 */
public final class DockingExperimentApplication {

    private DockingExperimentApplication() {
    }

    public static void main(String[] args) {
        Class<? extends Application> applicationClass =
                selectApplication(args);

        Application.launch(
                applicationClass,
                args);
    }

    private static Class<? extends Application> selectApplication(
            String[] args) {
        if (args.length == 0) {
            return DockingExperimentChooserFxApplication.class;
        }

        String candidate =
                args[0].trim().toLowerCase(Locale.ROOT);

        return switch (candidate) {
            case "snapfx", "snap" ->
                    DockingExperimentFxApplication.class;
            case "bentofx", "bento" ->
                    BentoExperimentFxApplication.class;
            default ->
                    DockingExperimentChooserFxApplication.class;
        };
    }
}
