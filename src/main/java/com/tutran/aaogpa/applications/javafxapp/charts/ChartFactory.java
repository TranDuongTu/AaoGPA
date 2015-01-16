package com.tutran.aaogpa.applications.javafxapp.charts;

import com.tutran.aaogpa.data.models.Student;
import com.tutran.aaogpa.services.Statistician;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ChartFactory {
    private static final int NUMBER_OF_BIN = 20;

    public static BarChart<String, Number> makeHistogram(
            BarChart<String, Number> rawChart,
            Map<String, Map<Student, Double>> studentGPAsPerCategory) {
        // Config axis
        Axis<String> xAxis = rawChart.getXAxis();
        Axis<Number> yAxis = rawChart.getYAxis();
        xAxis.setLabel("Score intervals");
        yAxis.setLabel("Frequency");
        yAxis.setMinHeight(0);

        // categories for x-axis
        double interval = 10.0 / NUMBER_OF_BIN;
        List<String> intervals = new ArrayList<String>();
        for (int i = 0; i < NUMBER_OF_BIN; i++) {
            intervals.add("[" + (i*interval) + ", "
                    + ((i + 1) * interval) + ")");
        }
        String lastInterval = intervals.get(intervals.size() - 1);
        intervals.remove(lastInterval);
        intervals.add(lastInterval.substring(0, lastInterval.length() - 1) + "]");

        // create series
        for (String seriesName : studentGPAsPerCategory.keySet()) {
            List<List<Student>> histogram = Statistician.makeHistogramBins(
                    studentGPAsPerCategory.get(seriesName), NUMBER_OF_BIN);
            XYChart.Series<String, Number> series =
                    new XYChart.Series<String, Number>();
            series.setName(seriesName);
            for (int i = 0; i < NUMBER_OF_BIN; i++) {
                String category = intervals.get(i);
                Number size = histogram.get(i).size();

                series.getData().add(
                        new XYChart.Data<String, Number>(category, size));
            }
            rawChart.getData().add(series);
        }

        return rawChart;
    }

    public static BarChart<String, Number> makeSimpleBarChart(
            BarChart<String, Number> rawChart,
            Map<String, Double> data) {
        Axis<String> xAxis = rawChart.getXAxis();
        Axis<Number> yAxis = rawChart.getYAxis();
        xAxis.setLabel("Elements");
        yAxis.setLabel("Score");
        yAxis.setMinHeight(0);

        XYChart.Series<String, Number> series =
                new XYChart.Series<String, Number>();
        series.setName("Courses Performance");
        for (String element : data.keySet()) {
            Number value = data.get(element);
            series.getData().add(
                    new XYChart.Data<String, Number>(element, value));
        }
        rawChart.getData().add(series);

        return rawChart;
    }
}
