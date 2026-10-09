package io.github.brainboxemb.experimental.docking.view;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * Representative interpreted registration view for the Development Client.
 */
public final class RegistrationsPane extends BorderPane {

    public RegistrationsPane(
            RawDataModel rawDataModel) {
        TableView<RegistrationRow> table =
                new TableView<>(
                        FXCollections.observableArrayList(
                                new RegistrationRow(
                                        "12:00:01.230",
                                        "AUTO",
                                        "101",
                                        "AUTO",
                                        "LIVE"),
                                new RegistrationRow(
                                        "12:00:02.070",
                                        "MAN",
                                        "204",
                                        "MAN",
                                        "LIVE"),
                                new RegistrationRow(
                                        "12:00:03.910",
                                        "AUTO",
                                        "318",
                                        "AUTO",
                                        "LIVE")));

        table.getStyleClass().add(
                "experiment-data-table");
        table.setFixedCellSize(24);
        table.setPlaceholder(
                new Label("No registrations"));

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, previous, selected) -> {
                            if (selected != null) {
                                rawDataModel.show(
                                        "REGISTRATION",
                                        "Team "
                                                + selected.teamId()
                                                + " · "
                                                + selected.type(),
                                        selected.rawJson());
                            }
                        });

        TableColumn<RegistrationRow, String> time =
                textColumn(
                        "Time",
                        112,
                        row -> row.time());
        TableColumn<RegistrationRow, String> type =
                textColumn(
                        "Type",
                        64,
                        row -> row.type());
        TableColumn<RegistrationRow, String> teamId =
                textColumn(
                        "Team ID",
                        74,
                        row -> row.teamId());
        TableColumn<RegistrationRow, String> code =
                textColumn(
                        "Code",
                        64,
                        row -> row.code());

        TableColumn<RegistrationRow, String> state =
                new TableColumn<>("State");
        state.setPrefWidth(86);
        state.setCellValueFactory(
                cell ->
                        cell.getValue()
                                .stateProperty());

        TableColumn<RegistrationRow, RegistrationRow> delete =
                new TableColumn<>("");
        delete.setMinWidth(44);
        delete.setPrefWidth(44);
        delete.setMaxWidth(44);
        delete.setResizable(false);
        delete.setSortable(false);
        delete.setCellValueFactory(
                cell ->
                        new javafx.beans.property.ReadOnlyObjectWrapper<>(
                                cell.getValue()));
        delete.setCellFactory(
                column ->
                        new DeleteCell());

        table.getColumns().addAll(
                time,
                type,
                teamId,
                code,
                state,
                delete);

        table.setRowFactory(
                ignored ->
                        new TableRow<>() {
                            @Override
                            protected void updateItem(
                                    RegistrationRow row,
                                    boolean empty) {
                                super.updateItem(
                                        row,
                                        empty);
                                setOpacity(
                                        !empty
                                                && row != null
                                                && "DELETED".equals(
                                                        row.state())
                                                ? 0.55
                                                : 1.0);
                            }
                        });

        Label mode =
                new Label("LIVE");
        mode.getStyleClass().add(
                "experiment-data-status");

        Label count =
                new Label(
                        table.getItems().size()
                                + " registrations");

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

    private static TableColumn<RegistrationRow, String>
            textColumn(
                    String title,
                    double width,
                    java.util.function.Function
                            <RegistrationRow, String> value) {
        TableColumn<RegistrationRow, String> column =
                new TableColumn<>(title);
        column.setPrefWidth(width);
        column.setCellValueFactory(
                cell ->
                        new ReadOnlyStringWrapper(
                                value.apply(
                                        cell.getValue())));
        return column;
    }

    private static String registrationMessage(
            long sequence,
            String teamId,
            String code,
            String time) {
        return """
                {
                  "sequenceNumber": %d,
                  "recordType": "ADD",
                  "registrationId": "%s",
                  "locationId": 1,
                  "codes": ["%s"],
                  "effectiveTime": "2026-10-09T%sZ",
                  "recordedAt": "2026-10-09T%sZ"
                }
                """.formatted(
                        sequence,
                        teamId,
                        code,
                        time,
                        time);
    }

    private final class DeleteCell
            extends TableCell
                    <RegistrationRow, RegistrationRow> {

        private final Button button =
                new Button("🗑");

        private DeleteCell() {
            button.getStyleClass().add(
                    "experiment-table-action");
            button.setAccessibleText(
                    "Delete registration");
            button.setTooltip(
                    new Tooltip(
                            "Mark registration deleted; "
                                    + "the technical LogBook entry remains."));
            button.setFocusTraversable(false);
        }

        @Override
        protected void updateItem(
                RegistrationRow row,
                boolean empty) {
            super.updateItem(
                    row,
                    empty);

            if (empty || row == null) {
                setGraphic(null);
                return;
            }

            if ("DELETED".equals(
                    row.state())) {
                setGraphic(null);
                return;
            }

            button.setOnAction(
                    event -> {
                        row.markDeleted();
                        rawDataModel.show(
                                "REGISTRATION",
                                "Delete team "
                                        + row.teamId(),
                                row.deleteMessage());
                        setGraphic(null);
                        getTableView().refresh();
                    });

            setGraphic(button);
        }
    }

    private static final class RegistrationRow {

        private final String time;
        private final String type;
        private final String teamId;
        private final String code;
        private final StringProperty state;
        private final String rawJson;

        private RegistrationRow(
                String time,
                String type,
                String teamId,
                String code,
                String state,
                String rawJson) {
            this.time = time;
            this.type = type;
            this.teamId = teamId;
            this.code = code;
            this.state =
                    new SimpleStringProperty(
                            state);
            this.rawJson = rawJson;
        }

        private String time() {
            return time;
        }

        private String type() {
            return type;
        }

        private String teamId() {
            return teamId;
        }

        private String code() {
            return code;
        }

        private String rawJson() {
            return rawJson;
        }

        private String deleteMessage() {
            return """
                    {
                      "recordType": "REV",
                      "registrationId": "%s",
                      "codes": ["MAN"],
                      "effectiveTime": "2026-10-09T%sZ",
                      "reason": "operator delete"
                    }
                    """.formatted(
                            teamId,
                            time);
        }

        private String state() {
            return state.get();
        }

        private StringProperty stateProperty() {
            return state;
        }

        private void markDeleted() {
            state.set("DELETED");
        }
    }
}
