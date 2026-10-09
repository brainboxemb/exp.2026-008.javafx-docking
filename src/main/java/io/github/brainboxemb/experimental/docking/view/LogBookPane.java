package io.github.brainboxemb.experimental.docking.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Representative raw logbook view for the Development Client.
 */
public final class LogBookPane extends BorderPane {

    public LogBookPane(
            RawDataModel rawDataModel) {
        TableView<LogBookRow> table =
                new TableView<>(
                        FXCollections.observableArrayList(
                                new LogBookRow(
                                        "12:00:01.230",
                                        "AUTO_REG",
                                        "101",
                                        "antenna-1",
                                        "tag EPC 3008...E21 accepted",
                                        logBookMessage(
                                                41,
                                                "ADD",
                                                "101",
                                                "AUTO",
                                                "12:00:01.230")),
                                new LogBookRow(
                                        "12:00:02.070",
                                        "MAN_REG",
                                        "204",
                                        "operator",
                                        "manual registration",
                                        logBookMessage(
                                                42,
                                                "ADD",
                                                "204",
                                                "MAN",
                                                "12:00:02.070")),
                                new LogBookRow(
                                        "12:00:03.910",
                                        "AUTO_REG",
                                        "318",
                                        "antenna-1",
                                        "tag EPC 3008...A44 accepted",
                                        logBookMessage(
                                                43,
                                                "ADD",
                                                "318",
                                                "AUTO",
                                                "12:00:03.910")),
                                new LogBookRow(
                                        "12:00:04.140",
                                        "DELETE",
                                        "204",
                                        "operator",
                                        "registration marked deleted",
                                        logBookMessage(
                                                44,
                                                "REV",
                                                "204",
                                                "MAN",
                                                "12:00:04.140"))));

        table.getStyleClass().add(
                "experiment-data-table");
        table.setFixedCellSize(24);
        table.setPlaceholder(
                new Label("No logbook events"));

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, previous, selected) -> {
                            if (selected != null) {
                                rawDataModel.show(
                                        "LOGBOOK",
                                        selected.event()
                                                + " · Team "
                                                + selected.teamId(),
                                        selected.rawJson());
                            }
                        });

        TableColumn<LogBookRow, String> time =
                textColumn(
                        "Time",
                        112,
                        LogBookRow::time);
        TableColumn<LogBookRow, String> event =
                textColumn(
                        "Event",
                        92,
                        LogBookRow::event);
        TableColumn<LogBookRow, String> teamId =
                textColumn(
                        "Team ID",
                        74,
                        LogBookRow::teamId);
        TableColumn<LogBookRow, String> source =
                textColumn(
                        "Source",
                        94,
                        LogBookRow::source);
        TableColumn<LogBookRow, String> details =
                textColumn(
                        "Details",
                        280,
                        LogBookRow::details);

        table.getColumns().addAll(
                time,
                event,
                teamId,
                source,
                details);
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        Label mode =
                new Label("RAW");
        mode.getStyleClass().add(
                "experiment-data-status");

        Label count =
                new Label(
                        table.getItems().size()
                                + " events");

        HBox toolbar =
                new HBox(
                        8,
                        mode,
                        count);
        toolbar.setAlignment(
                Pos.CENTER_LEFT);
        toolbar.getStyleClass().add(
                "experiment-data-toolbar");

        setTop(toolbar);
        setCenter(table);
        getStyleClass().add(
                "experiment-data-pane");
    }

    private static TableColumn<LogBookRow, String>
            textColumn(
                    String title,
                    double width,
                    java.util.function.Function
                            <LogBookRow, String> value) {
        TableColumn<LogBookRow, String> column =
                new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setCellValueFactory(
                cell ->
                        new ReadOnlyStringWrapper(
                                value.apply(
                                        cell.getValue())));
        return column;
    }

    private static String logBookMessage(
            long sequence,
            String recordType,
            String teamId,
            String code,
            String time) {
        return """
                {
                  "sequenceNumber": %d,
                  "recordType": "%s",
                  "registrationId": "%s",
                  "locationId": 1,
                  "codes": ["%s"],
                  "effectiveTime": "2026-10-09T%sZ",
                  "recordedAt": "2026-10-09T%sZ"
                }
                """.formatted(
                        sequence,
                        recordType,
                        teamId,
                        code,
                        time,
                        time);
    }

    private record LogBookRow(
            String time,
            String event,
            String teamId,
            String source,
            String details,
            String rawJson) {
    }
}
