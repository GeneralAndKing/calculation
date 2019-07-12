package gak.ui;

import gak.bean.ChartBean;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.function.*;

import static java.lang.Math.*;

/**
 * 图表主界面
 *
 * @author <a href="https://echocow.cn">EchoCow</a>
 * @date 19-7-9 上午11:39
 */
class ChartPane {
    private Stage stage;
    private ChartBean chartBean;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();


    /**
     * 初始化图表生成
     */
    ChartPane(ChartBean chartBean) {
        stage = new Stage();
        this.chartBean = chartBean;
        init();
        // 初始化图表数据
        series.getData().remove(0, series.getData().size() - 1);
        switch (chartBean.getType()) {
            case "x²":
                initPoint(i -> pow(i, 2));
                break;
            case "x³":
                initPoint(i -> pow(i, 3));
                break;
            case "√x":
                initPoint(Math::sqrt);
                break;
            case "10ˣ":
                initPoint(i -> pow(10, i));
                break;
            case "eˣ":
                initPoint(() -> E, i -> pow(E, i));
                break;
            case "sin(x)":
                initPoint(Math::sin);
                break;
            case "cos(x)":
                initPoint(Math::cos);
                break;
            case "2x":
                initPoint(i -> 2 * i);
                break;
            case "-2x":
                initPoint(i -> -2 * i);
                break;
            default:
                break;
        }

        //数据显示
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        scene.getStylesheets().add(getClass().getResource("chart.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
        for (XYChart.Series<Number, Number> s : lineChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                StackPane node = (StackPane) d.getNode();
                node.setPrefWidth(chartBean.getSize());
                node.setPrefHeight(chartBean.getSize());
                Tooltip tooltip = new Tooltip(
                        "x: " + d.getXValue().toString() + "\n" +
                                chartBean.getType() + ": " + d.getYValue());
                Tooltip.install(node, tooltip);
                node.setCursor(Cursor.OPEN_HAND);
                node.setOnMouseEntered(event -> node.getStyleClass().add("onHover"));
                node.setOnMouseExited(event -> node.getStyleClass().remove("onHover"));
            }
        }
    }


    /**
     * 抽取代码，初始化画布
     */
    private void init() {
        stage.setTitle(chartBean.getType() + " 函数图");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel(chartBean.getType());
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(chartBean.getType() + " 函数图");
        series.setName("点");
    }

    private void initPoint(DoubleFunction<Double> getY) {
        for (double i = chartBean.getStart(); i <= chartBean.getEnd(); i += chartBean.getStep()) {
            series.getData().add(new XYChart.Data<>(i, getY.apply(i)));
        }
    }

    private void initPoint(DoubleSupplier getX, DoubleFunction<Double> getY) {
        for (double i = chartBean.getStart(); i <= chartBean.getEnd(); i += chartBean.getStep()) {
            series.getData().add(new XYChart.Data<>(getX.getAsDouble(), getY.apply(i)));
        }
    }

}
