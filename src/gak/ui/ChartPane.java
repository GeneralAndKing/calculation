package gak.ui;

import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import static java.lang.Math.*;

/**
 * 图表主界面
 */
public class ChartPane {
    private Stage stage;
    private LineChart<Number, Number> lineChart;
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    /**
     * 初始化图表生成
     */
    public ChartPane(Stage stage, String type) {
        this.stage = stage;
        init(type);
        // 初始化图表数据
        switch (type) {
            case "x²":
                showX2();
                break;
            case "x³":
                showX3();
                break;
            case "√x":
                showSqrtX();
                break;
            case "10ˣ":
                tenX();
                break;
            case "eˣ":
                ex();
                break;
            case "sin(x)":
                sinX();
                break;
            case "cos(x)":
                cosX();
                break;
            case "2x":
                twoX();
                break;
            case "-2x":
                fuTwoX();
                break;
            default:
                break;
        }

        //数据显示
        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * 抽取代码，初始化画布
     */
    private void init(String type) {
        stage.setTitle(type + " 函数图");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel(type);
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(type + " 函数图");
        series.setName("点");
    }

    /**
     * 函数 -2x
     */
    private void fuTwoX() {
        for (double i = -10; i <= 10; i++) {
            series.getData().add(new XYChart.Data<>(i, -2 * i));
        }
    }

    /**
     * 函数 2x
     */
    private void twoX() {
        for (double i = -10; i <= 10; i++) {
            series.getData().add(new XYChart.Data<>(i, 2 * i));
        }
    }

    /**
     * 函数 sin(x)
     */
    private void sinX() {
        for (double i = -10; i <= 10; i += 0.2) {
            series.getData().add(new XYChart.Data<>(i, sin(i)));
        }
    }

    /**
     * 函数 cos(x)
     */
    private void cosX() {
        for (double i = -10; i <= 10; i += 0.2) {
            series.getData().add(new XYChart.Data<>(i, cos(i)));
        }
    }

    /**
     * 函数 eˣ
     */
    private void ex() {
        for (int i = -10; i <= 10; i++) {
            series.getData().add(new XYChart.Data<>(E, pow(E, i)));
        }
    }

    /**
     * 函数 10ˣ
     */
    private void tenX() {
        for (int i = -10; i <= 10; i++) {
            series.getData().add(new XYChart.Data<>(i, pow(10, i)));
        }
    }

    /**
     * 函数 x²
     */
    private void showX2() {
        for (int i = -10; i <= 10; i++) {
            series.getData().add(new XYChart.Data<>(i, pow(i, 2)));
        }
    }

    /**
     * 函数 x³
     */
    private void showX3() {
        double step = 21.0 / 2000;
        for (double i = -10; i <= 10; i = i + step) {
            series.getData().add(new XYChart.Data<>(i, pow(i, 3)));
        }
    }

    /**
     * 函数 √x
     */
    private void showSqrtX() {
        for (double i = 0; i <= 10; i += 0.5) {
            series.getData().add(new XYChart.Data<>(i, sqrt(i)));
        }
    }

}
