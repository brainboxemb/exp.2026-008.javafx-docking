package io.github.brainboxemb.experimental.docking.view;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Raw message/detail view for representative events and selected records.
 */
public final class RawDataPane extends BorderPane {

    public RawDataPane(
            RawDataModel model) {
        Label kind =
                new Label();
        kind.textProperty().bind(
                model.kindProperty());
        kind.getStyleClass().add(
                "experiment-data-status");

        Label source =
                new Label();
        source.textProperty().bind(
                model.sourceProperty());

        HBox toolbar =
                new HBox(
                        8,
                        kind,
                        source);
        toolbar.setAlignment(
                Pos.CENTER_LEFT);
        toolbar.getStyleClass().add(
                "experiment-data-toolbar");

        TextArea raw =
                new TextArea();
        raw.setEditable(false);
        raw.setWrapText(false);
        raw.textProperty().bind(
                model.rawJsonProperty());
        raw.getStyleClass().add(
                "experiment-raw-data");

        model.rawJsonProperty().addListener(
                (observable, oldValue, newValue) ->
                        Platform.runLater(
                                () ->
                                        raw.positionCaret(0)));

        setTop(toolbar);
        setCenter(raw);
        getStyleClass().add(
                "experiment-data-pane");
    }
}
