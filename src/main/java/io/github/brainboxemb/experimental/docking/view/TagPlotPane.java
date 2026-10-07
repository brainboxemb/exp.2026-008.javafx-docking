package io.github.brainboxemb.experimental.docking.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;

/**
 * Small continuously updating chart used to exercise docking/resizing with
 * realtime-like content.
 */
public final class TagPlotPane extends BorderPane {

    private static final int MAX_POINTS = 120;

    private final XYChart.Series<Number, Number> series =
            new XYChart.Series<>();
    private final Timeline updateTimeline;
    private long sample;

    public TagPlotPane() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Sample");
        yAxis.setLabel("Tag activity");
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(20);

        LineChart<Number, Number> chart =
                new LineChart<>(xAxis, yAxis);
        chart.setAnimated(false);
        chart.setCreateSymbols(false);
        chart.setLegendVisible(false);
        chart.setTitle("Realtime tag activity placeholder");
        chart.getData().add(series);

        setCenter(chart);
        getStyleClass().add("experiment-tag-plot");

        updateTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(100),
                        event -> addSample()));
        updateTimeline.setCycleCount(Timeline.INDEFINITE);

        sceneProperty().addListener(
                (observable, oldScene, newScene) -> {
                    if (newScene == null) {
                        updateTimeline.stop();
                    } else {
                        updateTimeline.play();
                    }
                });
    }

    private void addSample() {
        double value =
                50.0
                + 28.0 * Math.sin(sample / 9.0)
                + 12.0 * Math.sin(sample / 3.7);

        series.getData().add(
                new XYChart.Data<>(sample, value));

        if (series.getData().size() > MAX_POINTS) {
            series.getData().removeFirst();
        }

        sample++;
    }
}
