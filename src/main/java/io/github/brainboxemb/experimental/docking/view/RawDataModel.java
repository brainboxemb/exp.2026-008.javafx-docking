package io.github.brainboxemb.experimental.docking.view;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 * Application-owned detail model shared by event and table selections.
 */
public final class RawDataModel {

    private final ReadOnlyStringWrapper kind =
            new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper source =
            new ReadOnlyStringWrapper();
    private final ReadOnlyStringWrapper rawJson =
            new ReadOnlyStringWrapper();

    public RawDataModel() {
        show(
                "EVENT",
                "TIMING_DATA_COMMITTED · node A",
                """
                {
                  "eventType": "TIMING_DATA_COMMITTED",
                  "occurredAt": "2026-10-09T12:00:03.910Z",
                  "timingNodeId": "A",
                  "timingData": {
                    "sequenceNumber": 43,
                    "recordType": "ADD",
                    "registrationId": "318",
                    "locationId": 1,
                    "codes": ["AUTO"],
                    "effectiveTime": "2026-10-09T12:00:03.910Z",
                    "recordedAt": "2026-10-09T12:00:03.928Z"
                  }
                }
                """);
    }

    public ReadOnlyStringProperty kindProperty() {
        return kind.getReadOnlyProperty();
    }

    public ReadOnlyStringProperty sourceProperty() {
        return source.getReadOnlyProperty();
    }

    public ReadOnlyStringProperty rawJsonProperty() {
        return rawJson.getReadOnlyProperty();
    }

    public void show(
            String kind,
            String source,
            String rawJson) {
        this.kind.set(
                kind == null ? "" : kind);
        this.source.set(
                source == null ? "" : source);
        this.rawJson.set(
                rawJson == null ? "" : rawJson);
    }
}
