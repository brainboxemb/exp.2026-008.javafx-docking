package io.github.brainboxemb.experimental.docking;

import javafx.application.Application;

/**
 * Plain Java launcher for the BentoFX candidate.
 */
public final class BentoExperimentApplication {

    private BentoExperimentApplication() {
    }

    public static void main(String[] args) {
        Application.launch(
                BentoExperimentFxApplication.class,
                args);
    }
}
